package top.yeliusu.websocketserver.websocket.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 建议key规则，具有层次感，以冒号（:）分隔开
 * 例如：不同学校，不同年级，不同班级  school2：grade3：clazz4
 * 这样Redis图形化工具中会按照冒号，以文件夹分隔开，便于管理
 *
 *
 * @Package: top.yeliusu.ehcache.demo.redis
 * @Description： redisUtils工具类
 * @Author: SongJunWei
 * @Date: 2020/7/30
 * @Modified By:
 */
@Component
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    @Resource
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }


    //=============================String=============================

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static void put(String key, Object value, Long time) {
        redisTemplate.opsForValue().set(key, value);
        expire(key, time);
    }

    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    public static void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public static Boolean isExit(String key) {
        return redisTemplate.hasKey(key);
    }

    public static void expire(String key, Long time) {

        if (StringUtils.isBlank(key) || time == null) {
            return;
        }
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public static List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 自增，自减
     * @param key 可以
     * @param increment 增量
     * @return
     */
    public static Long increment(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    //===========================hash===============================

    /**
     * 获取hash中指定的键的值
     *  例： 获取 班级1（key）下 学生王五userId（hashKey）的用户信息
     * @param key key
     * @param hashKey 键
     * @return object,很大可能是对象
     */
    public static Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    /**
     * 获取hash中所有的键
     * @param key key
     * @return set集合
     */
    public static Set<Object> hGetKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取hash中所有的值
     * @param key 可以
     * @return list
     */
    public static List<Object> hGetValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 获取hash中所有键值对map
     * @param key 可以
     * @return map
     */
    public static Map<Object,Object> hGetEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 以集合形式添加
     * @param key key
     * @param map map
     */
    public static void hPutAll(String key,Map<String,Object> map) {
        redisTemplate.opsForHash().putAll(key,map);
    }

    /**
     * 单个放入
     * @param key key
     * @param hashKey map的key
     * @param value 值
     */
    public static void hPut(String key,String hashKey,Object value) {
        redisTemplate.opsForHash().put(key,hashKey,value);
    }

    /**
     * 判断hashKey是否存在
     * @param key key
     * @param hashKey 键
     * @return boolean
     */
    public static Boolean hHasKey(String key,String hashKey) {
        return redisTemplate.opsForHash().hasKey(key,hashKey);
    }

    /**
     * 自增map
     * @param key key
     * @param hashKey map的key
     * @param value 值
     * @return Long
     */
    public static Long hIncrement(String key,String hashKey,Long value) {
        return redisTemplate.opsForHash().increment(key,hashKey,value);
    }

    public static void hDel(String key,Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    //==============================Set=====================================

    public static void sPut(String key,Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public static Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public static Object sRandom(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    public static List<Object> sRandom(String key, Integer len) {
        return redisTemplate.opsForSet().randomMembers(key, len);
    }

}
