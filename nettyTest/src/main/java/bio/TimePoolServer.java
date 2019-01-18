/**
 * 
 */
package bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zyd
 * @date 2019年1月16日 下午2:48:15
 * @ClassName: TimePoolServer
 */
public class TimePoolServer {
	public static void main(String[] args) throws IOException {
		int port = 8000;
		if (args != null || args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				port = 8000;
			}
		}

		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The time server is start in port : " + port);
			Socket socket = null;
			TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
			while (true) {
				socket = server.accept();
				singleExecutor.execute(new TimeServerHandler(socket));
			}
		} finally {
			if (server != null) {
				System.out.println("The time server close");
				server.close();
				server = null;
			}
		}
	}
}
