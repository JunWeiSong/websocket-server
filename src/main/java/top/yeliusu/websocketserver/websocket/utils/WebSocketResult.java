package top.yeliusu.websocketserver.websocket.utils;

import lombok.Data;

@Data
public class WebSocketResult {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public WebSocketResult() {

    }

    public static WebSocketResult ok(Object data) {
        return new WebSocketResult(data);
    }

    public static WebSocketResult ok() {
        return new WebSocketResult(null);
    }

    public static WebSocketResult build(Integer status, String msg) {
        return new WebSocketResult(status, msg, null);
    }

    public WebSocketResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public WebSocketResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }
}
