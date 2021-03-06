/**
 * 
 */
package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author zyd
 * @date 2019年1月16日 下午2:21:40
 * @ClassName: TimeClient
 */
public class TimeClient {
	public static void main(String[] args) throws IOException {
		int port = 8000;
		if (args != null || args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				port = 8000;
			}
		}
		
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket("127.0.0.1", port);
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("QUERY TIME ORDER");
			System.out.println("Send order 2 server succeed.");
			String reString = in.readLine();
			System.out.println("Now is : " + reString);
		}finally {
			if(in != null) {
				try {
					in.close();
				}catch (IOException e1) {
					// TODO: handle exception
					e1.printStackTrace();
				}
				if (out != null) {
					out.close();
					out = null;
				}
				if(socket != null) {
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					socket = null;
				}
			}
		}
	}
}
