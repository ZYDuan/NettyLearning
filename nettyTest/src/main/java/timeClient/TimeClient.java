/**
 * 
 */
package timeClient;


/**
 * @author zyd
 * @date 2019年1月17日 下午12:48:08 
 * @ClassName: TimeClient 
 */
public class TimeClient {
	public static void main(String[] args) {
		int port = 8000;
		if (args != null || args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				port = 8000;
			}
		}
		new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "Time-AsyncTimeClientHandler-001").start();;
		
	}
}
