/**
 * 
 */
package tcpSlove;

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
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
	private int counter;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("The netty time server receive order : " + body + " ; the counter is : " +  ++counter);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()
				: "BAD ORDER";
		currentTime += System.getProperty("line.separator");
		ByteBuf resq = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resq);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
