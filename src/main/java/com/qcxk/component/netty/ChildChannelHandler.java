package com.qcxk.component.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: think
 * Date: 2019/11/4
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    private DiscardServerHandler discardServerHandler =  new DiscardServerHandler();
    @Override
    public void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(discardServerHandler);
    }
}
