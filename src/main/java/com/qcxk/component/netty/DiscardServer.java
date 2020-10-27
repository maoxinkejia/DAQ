package com.qcxk.component.netty;

import com.qcxk.service.MessageService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DiscardServer {
    private ChildChannelHandler childChannelHandler;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(5);
    private static DiscardServer discardServer;

    private static Channel serverChannel;

    public DiscardServer(MessageService messageService) {
        childChannelHandler = new ChildChannelHandler(messageService);
    }

    public DiscardServer() {
    }

    public static void startServer(MessageService messageService) {
        threadPool.submit(() -> {
            try {
                discardServer = new DiscardServer(messageService);
                discardServer.run(7878);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 关闭当前server
     */
    public static void closeServer() {
        if (serverChannel != null) {
            log.info("close server");
            serverChannel.close();
            serverChannel = null;
            discardServer = null;
        }
    }

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        log.info("准备运行端口：{}", port);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(childChannelHandler);
            //绑定端口，同步等待成功
            ChannelFuture f = bootstrap.bind(port).sync();
            serverChannel = f.channel();
            //等待服务监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            //退出，释放线程资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
