package msgpack; /**
 *
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author zyd
 * @date 2019年1月17日 下午6:30:51
 * @ClassName: msgpack.EchoClient
 */
public class EchoClient {

    public static void main(String[] args) throws Exception {
        int port = 6000;
        if (args != null || args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {
                port = 6000;
            }
        }

        new EchoClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /**
                             * 为了处理半包消息，添加如下两个 Netty 内置的编解码器
                             * LengthFieldPrepender：前置长度域编码器——放在MsgpackEncoder编码器前面
                             * LengthFieldBasedFrameDecoder：长度域解码器——放在MsgpackDecoder解码器前面
                             * 关于 长度域编解码器处理半包消息，本文不做详细讲解，会有专门篇章进行说明
                             */
                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            ch.pipeline().addLast("MessagePack encoder", new MsgpackEncoder());
                            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            ch.pipeline().addLast("MessagePack Decoder", new MsgpackDecoder());
                            ch.pipeline().addLast(new EchoClientHandler(10));
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
