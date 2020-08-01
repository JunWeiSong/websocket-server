package top.yeliusu.websocketserver.websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yeliusu.websocketserver.websocket.domain.UserInfo;
import top.yeliusu.websocketserver.websocket.redis.RedisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/redis/")
public class RedisController {

    @GetMapping("map/incr")
    public Long redisMapIncr() {
        RedisUtils.hIncrement("user_click","userId:k2j34j16j",1L);
        return RedisUtils.hIncrement("user_click","userId:g5s4d6e8w",1L);
    }

    @GetMapping("map/put")
    public Map<Object, Object> redisMapPut() {
        Map<String, Object> mapAll = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            UserInfo userInfo = new UserInfo();
           userInfo.setId("id"+i);
           userInfo.setOnLine(1);
           userInfo.setClazzId("班级1");
           userInfo.setName("姓名"+i);
           userInfo.setSex(SendController.randSex());
            mapAll.put("id"+i,userInfo);
        }
        RedisUtils.hPutAll("clazz",mapAll);


        System.out.println(RedisUtils.hGet("clazz", "id0"));
        System.out.println(RedisUtils.hGetKeys("clazz"));
        System.out.println(RedisUtils.hGetValues("clazz"));
        //get 获取所有键值对
        return RedisUtils.hGetEntries("clazz");
    }
}
