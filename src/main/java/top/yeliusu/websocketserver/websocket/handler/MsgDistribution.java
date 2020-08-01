package top.yeliusu.websocketserver.websocket.handler;

import com.google.gson.Gson;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import top.yeliusu.websocketserver.websocket.domain.UserInfo;
import top.yeliusu.websocketserver.websocket.domain.vo.SendMsgVO;

import java.util.List;

/**
 * 消息分发
 * <p>
 * TODO 如果缓存中不存在用户，即用户未登录，数据需要存入数据库或者缓存，待用户上线后再次执行发送操作
 */
public class MsgDistribution {

    public static void msgJudge(SendMsgVO sendMsgVO) {
        if (ObjectUtils.isEmpty(sendMsgVO)) {
            return;
        }

        if (sendMsgVO.getMessageType().equals("1")) {
            //向班级发送群聊消息
            sendMsgToClazz(sendMsgVO);
        } else if (sendMsgVO.getMessageType().equals("0")) {
            //向个人发送消息
            sendMsgToOneSelf(sendMsgVO);
        }else if (sendMsgVO.getMessageType().equals("2")) {
            //心跳
            sendMsgToOneSelf(sendMsgVO);
        } else {
            //系统通知
            ServerHandler.sendMessage(new Gson().toJson(sendMsgVO));
        }
    }

    /**
     * 向班级发送消息
     *
     * @param sendMsgVO
     */
    private static void sendMsgToClazz(SendMsgVO sendMsgVO) {
        List<UserInfo> clazzUser = TokenHandler.getClazzUser(sendMsgVO.getToId());
        Gson gson = new Gson();
        for (UserInfo userInfo : clazzUser) {
            //向在线的用户发送信息
            if (userInfo.getOnLine() == 1) {
                //1.判断用户是否在聊天界面

                //2.在:发送消息
                sendMsgVO.setMsgId(msgId(sendMsgVO.getToId()));
                ServerHandler.sendMessageOne(gson.toJson(sendMsgVO), userInfo.getId(),sendMsgVO.getToId());
                //3.不在:存到缓存中，发送消息个数
            }
        }
    }

    /**
     * 向个人发送消息
     *
     * @param sendMsgVO
     */
    private static void sendMsgToOneSelf(SendMsgVO sendMsgVO) {
        TokenHandler.get(sendMsgVO.getFromId()).setOnLine(1);
        UserInfo userInfo = TokenHandler.get(sendMsgVO.getToId());
        if (ObjectUtils.isNotEmpty(userInfo) && userInfo.getOnLine() == 1) {
            //向在线用户发送信息
            ServerHandler.sendMessageOne(new Gson().toJson(sendMsgVO), sendMsgVO.getToId(),sendMsgVO.getFromId());
        }
    }

    private static String msgId(String toId){
        return System.currentTimeMillis()+toId;
    }

}
