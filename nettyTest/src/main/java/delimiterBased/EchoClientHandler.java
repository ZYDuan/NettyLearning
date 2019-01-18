/**
 * 
 */
package delimiterBased;

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
public class EchoClientHandler extends ChannelInboundHandlerAdapter{
	private int counter;
	static final String ECHO_STR = "Hello+$_";
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for(int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_STR.getBytes()));
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		System.out.println("This is: " +  ++counter + " times receive client : " + msg + "]");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws UnsupportedEncodingException {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
