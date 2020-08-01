package top.yeliusu.websocketserver.websocket.handler;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import top.yeliusu.websocketserver.websocket.domain.UserInfo;
import top.yeliusu.websocketserver.websocket.domain.vo.SendMsgVO;
import top.yeliusu.websocketserver.websocket.utils.ActiveMqUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static top.yeliusu.websocketserver.websocket.utils.ActiveMqUtils.getMessage;


/**
 * @Package: top.yeliusu.ehcache.demo.websocket.handler
 * @Description：
 * @Author: SongJunWei
 * @Date: 2020/4/23
 * @Modified By:
 */
public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //添加到channelGroup通道组
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
        System.out.println("与客户端建立连接，通道开启！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("与客户端断开连接，通道关闭！");
        Set<String> collect = TokenHandler.channelCache.entrySet().stream().filter(v ->
                Objects.equals(v.getValue(), ctx.channel())).map(Map.Entry::getKey).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(collect)) {
            for (String key : collect) {
                //断开连接
                TokenHandler.channelCache.remove(key);
            }
        }
        //从channelGroup 通道组移除
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，处理参数
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            String urlParams = uri.substring(uri.lastIndexOf("/ws/") + 4);
            String[] split = urlParams.split("/");
            UserInfo user = TokenHandler.get(split[0]);
            if (ObjectUtils.isEmpty(user)){
                return;
            }
            //将握手信息进行缓存，方便下次取用
            //todo 以【用户id/班级id 】   存储用户与班级的通道信息
            // todo 同理，【fromId/toId】 存储用户和toId的通道信息
            TokenHandler.channelCache.put(urlParams, ctx.channel());
//            getMessage(user.getId());
            //重新设置路径，因为传递了参数
            request.setUri("/ws");
        } else if (msg instanceof TextWebSocketFrame) {
            //正常的TEXT消息类型
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            System.out.println("服务器收到客户端数据：" + frame.text());
            SendMsgVO sendMsgVO = new Gson().fromJson(frame.text(), SendMsgVO.class);
            MsgDistribution.msgJudge(sendMsgVO);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        System.out.println("客户端收到服务器数据：" + msg.text());
    }

    public static void sendMessage(String message) {
        //收到信息后，群发给所有channel
        MyChannelHandlerPool.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
    }

    public static void sendMessageOne(String message, String userId,String toId) {
        //收到信息后，群发给所有channel
        Channel channel = TokenHandler.channelCache.get(userId+"/"+toId);
        if (channel!=null) {
            MyChannelHandlerPool.channelGroup.find(channel.id()).writeAndFlush(new TextWebSocketFrame(message));
        }else {
            System.out.println(userId+"此用户不在班级聊天界面");
            ActiveMqUtils.sendMessage(userId,message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();

        if(channel.isActive()){ctx.close();}
    }
}