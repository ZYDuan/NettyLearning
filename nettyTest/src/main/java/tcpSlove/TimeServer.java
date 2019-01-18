/**
 * 
 */
package tcpSlove;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author zyd
 * @date 2019年1月17日 下午5:45:04
 * @ClassName: TimeServer
 */
public class TimeServer {
	public void bind(int port) throws Exception {
		// NioEventLoopGroup是个线程组，包含一组nio线程，专门用于网络事件的处理（reactor线程组）
		// 这里一个用于服务端接收客户端的联结，另外一个进行socketchannel的网络读写
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			ChannelFuture future = bootstrap.bind(port).sync();

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
		protected void initChannel(SocketChannel arg0) throws Exception {
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
			arg0.pipeline().addLast(new StringDecoder());
			arg0.pipeline().addLast(new TimeServerHandler());
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

		new TimeServer().bind(port);
	}
}
