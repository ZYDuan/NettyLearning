/**
 * 
 */
package nio;

/**
 * @author zyd
 * @date 2019年1月17日 上午11:04:03 
 * @ClassName: TimeClient 
 */
public class TimeClient {
	public static void main(String[] args) {
		int port = 7000;
		if (args != null || args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				port = 7000;
			}
		}
		new Thread(new TimeClientHandler("127.0.0.1", port), "NIO-Time-Client-001").start();;
		
	}
}
