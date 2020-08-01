package top.yeliusu.websocketserver.websocket.domain.vo;

import lombok.Data;

@Data
public class SendMsgVO {

    private String msgId;

    private String fromId;

    private String fromName;

    private String toId;
    /***消息类型 0：普通消息，1：班级消息，2：心跳*/
    private String messageType;

    private String message;

    public SendMsgVO() {
    }

    public SendMsgVO(String message) {
        this.message = message;
    }
}
