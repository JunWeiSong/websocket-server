package top.yeliusu.websocketserver.websocket.handler;

import com.google.gson.Gson;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import top.yeliusu.websocketserver.websocket.domain.UserInfo;
import top.yeliusu.websocketserver.websocket.domain.vo.SendMsgVO;

import java.util.List;

/**
 * 消息分发
 *
 * TODO 如果缓存中不存在用户，即用户未登录，数据需要存入数据库或者缓存，待用户上线后再次执行发送操作
 */
public class MsgDistribution {

    public static void msgJudge(SendMsgVO sendMsgVO ){
        if (ObjectUtils.isEmpty(sendMsgVO)) {
            return;
        }

        if (StringUtils.isNotBlank(sendMsgVO.getClazzId())) {
            //向班级发送群聊消息
            sendMsgToClazz(sendMsgVO);
        }else if (StringUtils.isNotBlank(sendMsgVO.getToId())){
            //向个人发送消息
            sendMsgToOneSelf(sendMsgVO);
        }else {
            //系统通知
            ServerHandler.sendMessage(new Gson().toJson(sendMsgVO));
        }
    }

    /**
     * 向班级发送消息
     * @param sendMsgVO
     */
    private static void sendMsgToClazz(SendMsgVO sendMsgVO){
        List<UserInfo> clazzUser = TokenHandler.getClazzUser(sendMsgVO.getClazzId());
        Gson gson = new Gson();
        for (UserInfo userInfo : clazzUser) {
            if (userInfo.getId().equals(sendMsgVO.getFromId())) {
                userInfo.setOnLine(1);
            }
            if (userInfo.getOnLine()==1) {
                //向在线的用户发送信息
                ServerHandler.sendMessageOne(gson.toJson(sendMsgVO),userInfo.getId());
            }
        }
    }

    /**
     * 向个人发送消息
     * @param sendMsgVO
     */
    private static void sendMsgToOneSelf(SendMsgVO sendMsgVO ){
        TokenHandler.get(sendMsgVO.getFromId()).setOnLine(1);
        UserInfo userInfo = TokenHandler.get(sendMsgVO.getToId());
        if (ObjectUtils.isNotEmpty(userInfo) && userInfo.getOnLine()==1) {
            //向在线用户发送信息
            ServerHandler.sendMessageOne(new Gson().toJson(sendMsgVO),sendMsgVO.getToId());
        }
    }

}
