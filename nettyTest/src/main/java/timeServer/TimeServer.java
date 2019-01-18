/**
 * 
 */
package timeServer;


/**
 * @author zyd
 * @date 2019年1月17日 下午12:44:04 
 * @ClassName: TimeServer 
 */
public class TimeServer {
	public static void main(String[] args) {
		int port = 8000;
		if (args != null || args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				port = 8000;
			}
		}
		AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
		new Thread(timeServer, "AIOTimeServer-001").start();
		
	}
}
