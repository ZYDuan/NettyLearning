/**
 * 
 */
package timeServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * @author zyd
 * @date 2019年1月17日 下午12:45:00
 * @ClassName: ReadCompletionHandler
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel channel;

	public ReadCompletionHandler(AsynchronousSocketChannel channel) {
		if (this.channel == null)
			this.channel = channel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		attachment.get(body);
		try {
			String req = new String(body, "UTF-8");
			System.out.println("The time server receive order : " + req);
			String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)
					? new Date(System.currentTimeMillis()).toString()
					: "BAD ORDER";
			doWrite(currentTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param currentTime
	 */
	private void doWrite(String currentTime) {
		if (currentTime != null && currentTime.trim().length() > 0) {
			byte[] bytes = currentTime.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

				@Override
				public void completed(Integer result, ByteBuffer buffer) {
					if (buffer.hasRemaining())
						channel.write(buffer, buffer, this);

				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						channel.close();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable,
	 * java.lang.Object)
	 */
	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			this.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
