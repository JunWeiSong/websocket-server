package top.yeliusu.websocketserver.websocket.domain.vo;

import lombok.Data;

@Data
public class SendMsgVO {

    private String fromId;

    private String fromName;

    private String toId;

    private String message;

    private String clazzId;


    public SendMsgVO() {
    }

    public SendMsgVO(String message, String clazzId) {
        this.message = message;
        this.clazzId = clazzId;
    }
}
