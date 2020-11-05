package com.qcxk.component.netty;


import com.qcxk.model.message.Message;
import com.qcxk.service.MessageService;
import com.qcxk.util.BusinessUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class DiscardServerHandler extends ChannelHandlerAdapter {

    private MessageService messageService;

    public DiscardServerHandler(MessageService messageService) {
        this.messageService = messageService;
    }

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
            messageService.addOriginalData(bodyStr, messages.get(0).getDeviceNum());

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
        log.info("response messages size: {}", responseList.size());

        for (String response : responseList) {
            log.info("response message: {}", response);
            ByteBuf in3 = Unpooled.copiedBuffer(convertString2Int(response));
            ctx.writeAndFlush(in3);
        }
    }

    private byte[] convertString2Int(String str) {
        int len = (str.length() + 1) / 2;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            //把十六进制转为十进制整型
            int dec_num = Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
            b[i] = (byte) dec_num;
        }
        return b;
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