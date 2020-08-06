package top.yeliusu.websocketserver.websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yeliusu.websocketserver.websocket.domain.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.yeliusu.websocketserver.websocket.redis.RedisUtils.*;

@RestController
@RequestMapping("/redis/")
public class RedisController {

    @GetMapping("list/push")
    public void redisListPush() {

        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId("id"+i);
            userInfo.setOnLine(1);
            userInfo.setClazzId("班级1");
            userInfo.setName("姓名"+i);
            userInfo.setSex(SendController.randSex());
            list.add(userInfo);
        }
        lPush("user",list);

        System.out.println(lGet("user",0,-1));
    }

    @GetMapping("list/pushAll")
    public void redisListPushAll() {

        List<UserInfo> list = new ArrayList<>();
        for (int i = 9; i < 12; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId("id"+i);
            userInfo.setOnLine(1);
            userInfo.setClazzId("班级1");
            userInfo.setName("姓名"+i);
            userInfo.setSex(SendController.randSex());
            list.add(userInfo);
        }
        lPushAll("user",list);

        System.out.println(lGet("user",0,-1));
    }

    @GetMapping("map/incr")
    public Long redisMapIncr() {
        hIncrement("user_click","userId:k2j34j16j",1L);
        return hIncrement("user_click","userId:g5s4d6e8w",1L);
    }

    @GetMapping("put")
    public void redisGet(String id) {
        //1.redis true   return

        //2.false   查询数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setId("id");
        userInfo.setOnLine(1);
        userInfo.setClazzId("班级1");
        userInfo.setName("姓名");
        userInfo.setSex(SendController.randSex());
        put(id,userInfo);
        System.out.println(get("id"));

        // 3.存放到redis
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
        hPutAll("clazz",mapAll);


        System.out.println(hGet("clazz", "id0"));
        System.out.println(hGetKeys("clazz"));
        System.out.println(hGetValues("clazz"));
        //get 获取所有键值对
        return hGetEntries("clazz");
    }
}
