/**
 * 
 */
package delimiterBased;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zyd
 * @date 2019年1月17日 下午5:53:38
 * @ClassName: TimeServerHandler
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	private int counter = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("This is: " +  ++counter + " times receive client : " + body + "]");
		body += "$_";
		ByteBuf resq = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(resq);
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
