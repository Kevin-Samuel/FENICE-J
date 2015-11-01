/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.matching;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/*
 * Description: Font Server Dispatcher
 * function   : accept request and create a thread to send it to mom server
 * 
 */

public class FrontServerDispatcher implements Runnable{
	
	ServerSocket frontServer;
	DataInputStream msgFromClient;
	
	public FrontServerDispatcher() {
		try {
			frontServer = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * listen request and new thread to handle it
	 * 
	 */
	public void onRequest() {
		try {
			
			while(true) {
				
				Socket clientSocket = frontServer.accept();
				
				OrderRequestProcessor processor = new OrderRequestProcessor(clientSocket);
				new Thread(processor).start();
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		onRequest();
	}
	
	
	public static void main(String[] args) {
		FrontServerDispatcher dispatcher = new FrontServerDispatcher();
		dispatcher.onRequest();
	}
}
