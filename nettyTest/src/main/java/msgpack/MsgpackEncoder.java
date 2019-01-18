package msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @program: nettyTest
 * @author: zyd
 * @description:
 * @create: 2019-01-18 11:04
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {


    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        MessagePack messagePack = new MessagePack();

        /** 序列化对象*/
        byte[] raw = messagePack.write(o);
        byteBuf.writeBytes(raw);
    }
}
