package com.qcxk.component.netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@ChannelHandler.Sharable
public class DiscardServerHandler extends ChannelHandlerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiscardServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] b;
        byte[] head, length, idNumber, fun, body;
        //
        String headStr;
        try {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            b = bytes;
            head = Arrays.copyOfRange(b, 0, 1);
            headStr = bytesToHexString(head);
            String bodystr = convertByteBufToString((ByteBuf) msg);
            System.out.println("-----------------------------");
            LOGGER.info("接收内容是");
            LOGGER.info(bodystr);
            ByteBuf in3 = Unpooled.copiedBuffer(bytes);//进行处理
            ctx.writeAndFlush(in3);//返回信息
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("解析报文发生异常");
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public static int byteToInt(byte b) {
        return b & 0xff;
    }

    //System.arraycopy()方法
    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    private byte[] toByteArray(String arg) {
        if (arg != null) {
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (char c : array) {
                if (c != ' ') {
                    NewArray[length] = c;
                    length++;
                }
            }
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[]{};
    }

    public String convertByteBufToString(ByteBuf buf) {
        String str = "初始化";
//        if(buf.hasArray()) { // 处理堆缓冲区
//            byte[] bytes = buf.array();
//            buf.getBytes(buf.readerIndex(), bytes);
//
//            str = bytesToHexString(bytes);
//        } else { // 处理直接缓冲区以及复合缓冲区
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        str = bytesToHexString(bytes);
//        }
        return str;
    }

    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常就关闭
        cause.printStackTrace();
        ctx.close();
    }

}
