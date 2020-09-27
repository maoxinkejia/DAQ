package com.qcxk.component.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    private DiscardServerHandler discardServerHandler = new DiscardServerHandler();

    @Override
    public void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(discardServerHandler);
    }
}
