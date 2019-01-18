/**
 * 
 */
package tcpSlove;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zyd
 * @date 2019年1月17日 下午6:34:05 
 * @ClassName: TimeClientHandler 
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter{
	private int counter;
	private byte[] req;
	public TimeClientHandler() {
		this.req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ByteBuf messgae = null;
		for(int i = 0; i < 100; i++) {
			messgae = Unpooled.buffer(req.length);
			messgae.writeBytes(this.req);
			ctx.writeAndFlush(messgae);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		String body = (String) msg;
		System.out.println("Netty-Client now is : " + body + " ; the counter is : " + ++counter);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
