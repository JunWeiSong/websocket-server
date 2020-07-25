package top.yeliusu.websocketserver.websocket.handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.Channel;
import org.apache.commons.collections4.MapUtils;
import top.yeliusu.websocketserver.websocket.domain.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Package: top.yeliusu.ehcache.demo.utils
 * @Description： token处理器
 * @Author: SongJunWei
 * @Date: 2020/4/24
 * @Modified By:
 */
public class TokenHandler {

    /**
     * 缓存Channel握手信息，以用以后的信息发送
     */
    public static Map<String, Channel> channelCache = new ConcurrentHashMap<>();

    /***缓存班级用户信息*/
    private static Map<String, ConcurrentHashMap<String,UserInfo>> clazzMap = new ConcurrentHashMap<>();

    /***缓存用户token*/
    private static Cache<String, UserInfo> tokenCache = CacheBuilder
            .newBuilder()
            .maximumSize(10000) //最大数量
            .concurrencyLevel(100) // 设置并发级别为100
            .recordStats() // 开启缓存统计
            .expireAfterWrite(2, TimeUnit.HOURS) //过期时间2小时
            .build();

    /**
     * 缓存放入用户信息
     *
     * @param userInfo 用户信息
     */
    public static void put(UserInfo userInfo) {
        tokenCache.put(userInfo.getId(), userInfo);
    }

    /**
     * 缓存获取用户信息
     *
     * @param key 用户Id
     */
    public static UserInfo get(String key) {
        return tokenCache.getIfPresent(key);
    }

    public static void cacheClazz(String clazzId,UserInfo userInfo){
        Map<String, UserInfo> userInfoMap = clazzMap.get(clazzId);
        if (MapUtils.isEmpty(userInfoMap)){
            //缓存无班级信息
            ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<>();
            map.put(userInfo.getId(), userInfo);
            clazzMap.put(clazzId,map);
            return;
        }
        userInfoMap.put(userInfo.getId(), userInfo);
    }

    public static List<UserInfo> getClazzUser(String clazzId){
       Map<String, UserInfo> userInfoMap = clazzMap.get(clazzId);
       return new ArrayList<>(userInfoMap.values());
    }

    public static UserInfo getClazzOneUser(String clazzId,String userId){
        Map<String, UserInfo> userInfoMap = clazzMap.get(clazzId);
        return userInfoMap.get(userId);
    }
}
