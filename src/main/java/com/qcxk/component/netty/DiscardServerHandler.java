package com.qcxk.component.netty;


import com.qcxk.model.Message;
import com.qcxk.service.MessageService;
import com.qcxk.util.BusinessUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class DiscardServerHandler extends ChannelHandlerAdapter {

    @Autowired
    private MessageService messageService;

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String bodyStr = convertByteBufToString((ByteBuf) msg);
            log.info("receive message: {}", bodyStr);

            if (!BusinessUtil.verifyReceiveMsg(bodyStr)) {
                return;
            }

            List<Message> messages = messageService.parse2Msg(bodyStr, new ArrayList<>());

            messageService.processMsg(messages);

            List<String> responseList = messageService.responseMessage(messages);

            callBack(context, responseList);
        } catch (Exception e) {
            log.info("解析报文发生异常", e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void callBack(ChannelHandlerContext ctx, List<String> responseList) {
        for (String response : responseList) {
            ByteBuf in3 = Unpooled.copiedBuffer(response.getBytes());
            ctx.writeAndFlush(in3);
        }
    }

    private String convertByteBufToString(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        return bytesToHexString(bytes);
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 出现异常就关闭
        cause.printStackTrace();
        ctx.close();
    }
}