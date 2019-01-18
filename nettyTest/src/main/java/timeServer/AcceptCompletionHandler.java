/**
 * 
 */
package timeServer;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author zyd
 * @date 2019年1月17日 下午12:45:36 
 * @ClassName: AcceptCompletionHandler 
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler>{

	/* (non-Javadoc)
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
		attachment.asynchronousServerSocketChannel.accept(attachment, this);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		result.read(buffer, buffer, new ReadCompletionHandler(result));
	}

	/* (non-Javadoc)
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 */
	@Override
	public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
		exc.printStackTrace();
		attachment.latch.countDown();
	}


}
