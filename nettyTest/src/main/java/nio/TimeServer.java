/**
 * 
 */
package nio;

/**
 * @author zyd
 * @date 2019年1月17日 上午10:27:59 
 * @ClassName: TimeServer 
 */
public class TimeServer {
	public static void main(String[] args) {
		int port = 7000;
		if (args != null || args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				port = 7000;
			}
		}
		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
		
	}
}
