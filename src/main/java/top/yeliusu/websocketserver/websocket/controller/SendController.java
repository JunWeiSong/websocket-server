package top.yeliusu.websocketserver.websocket.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yeliusu.websocketserver.websocket.domain.UserInfo;
import top.yeliusu.websocketserver.websocket.handler.TokenHandler;
import top.yeliusu.websocketserver.websocket.utils.WebSocketResult;

import java.util.Random;
import java.util.UUID;


@RestController
@RequestMapping("/send/user")
public class SendController {


    @PostMapping("/login")
    public WebSocketResult login(String userName, String clazzId) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        UserInfo userInfo = new UserInfo(id, userName, randSex(), clazzId,1);

        //缓存到对应班级
        TokenHandler.cacheClazz(clazzId, userInfo);
        //缓存用户信息
        TokenHandler.put(userInfo);
        System.out.println("当前的新用户为：" + userInfo.toString());
        return WebSocketResult.ok(userInfo);
    }



    /**
     * 随机性别
     *
     * @return 男女
     */
    public static String randSex() {
        return new Random().nextInt(10) < 5 ? "男" : "女";
    }
}
