package top.yeliusu.websocketserver.websocket.handler;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Package: top.yeliusu.ehcache.demo.websocket.handler
 * @Description：
 * @Author: SongJunWei
 * @Date: 2020/4/24
 * @Modified By:
 */
public class MyChannelHandlerPool {
    public MyChannelHandlerPool(){}

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
