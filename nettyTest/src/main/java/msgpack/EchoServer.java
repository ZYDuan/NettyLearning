package msgpack; /**
 * 
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author zyd
 * @date 2019年1月17日 下午5:45:04
 * @ClassName: msgpack.EchoServer
 */
public class EchoServer {
	public void bind(int port) throws Exception {
		// NioEventLoopGroup是个线程组，包含一组nio线程，专门用于网络事件的处理（reactor线程组）
		// 这里一个用于服务端接收客户端的联结，另外一个进行socketchannel的网络读写
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					// 设置TCP连接超时时间
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			ChannelFuture future = bootstrap.bind(port).sync();
			System.out.println(Thread.currentThread().getName() + ",服务器开始监听端口，等待客户端连接.........");

			// 等待服务端监听端口关闭
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
		 */
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			System.out.println(Thread.currentThread().getName() + ",服务器初始化通道...");
			ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
			ch.pipeline().addLast("MessagePack encoder", new MsgpackEncoder());
			ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
			ch.pipeline().addLast("MessagePack Decoder", new MsgpackDecoder());
			ch.pipeline().addLast(new EchoServerHandler());
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 6000;
		if (args != null || args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				port = 6000;
			}
		}

		new EchoServer().bind(port);
	}
}
