package com.qcxk.component.netty;

import com.qcxk.service.MessageService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    private DiscardServerHandler discardServerHandler;

    public ChildChannelHandler(MessageService messageService) {
        this.discardServerHandler = new DiscardServerHandler(messageService);
    }

    @Override
    public void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(discardServerHandler);
    }
}
