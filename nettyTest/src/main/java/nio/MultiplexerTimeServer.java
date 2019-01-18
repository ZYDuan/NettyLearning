/**
 * 
 */
package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zyd
 * @date 2019年1月17日 上午9:55:45 
 * @ClassName: MultiplexerTimeServer 
 * 多路复用类，是一个负责轮询多路复用器selctor，可以处理多个客户端的并发接入的线程
 */
public class MultiplexerTimeServer implements Runnable{
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private volatile boolean stop;
	
	public MultiplexerTimeServer(int port) {
		try {
			selector = Selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(new InetSocketAddress(port), 1024);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The nio time server is start in port : " + port);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop() {
		this.stop = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectorkeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectorkeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()) {
					key = it.next();
					it.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						if(key != null) {
							key.cancel();
							if(key.channel() != null)
								key.channel().close();
						}
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		if(selector != null) {
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param key
	 */
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()) {
			if(key.isAcceptable()) {
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			
			if(key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if(readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("The time server receive order : " + body);
					String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()) .toString() : "BAD ORDER";
					doWrite(sc, currentTime);
				}else if (readBytes < 0) {
					key.cancel();
					sc.close();
				}
			}
		}
	}

	/**
	 * @param sc
	 * @param currentTime
	 */
	private void doWrite(SocketChannel sc, String response) throws IOException {
		// TODO Auto-generated method stub
		if(response != null && response.trim().length() > 0) {
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}
}
