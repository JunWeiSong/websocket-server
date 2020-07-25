package top.yeliusu.websocketserver.websocket.domain;

import lombok.Data;

@Data
public class UserInfo {

    private String id;

    private String name;

    private String sex;

    private String clazzId;

    private Integer onLine;

    public UserInfo() {
    }

    public UserInfo(String id, String name, String sex, String clazzId,Integer onLine) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.clazzId = clazzId;
        this.onLine = onLine;
    }
}
