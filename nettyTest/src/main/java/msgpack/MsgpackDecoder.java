package msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @program: nettyTest
 * @author: zyd
 * @description:
 * @create: 2019-01-18 11:11
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);

        MessagePack messagePack = new MessagePack();
        list.add(messagePack.read(array));
    }
}
