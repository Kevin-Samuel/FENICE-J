package fenice.common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSocketConnector {
	
	private DataOutputStream orderRequest;
	private Socket socket;
	 
	public ClientSocketConnector() {
		try {
			socket = new Socket("localhost", 8888);
			
			orderRequest = new DataOutputStream(socket.getOutputStream());
			orderRequest.writeUTF("BeginString=Fix#MsgType=LimitEntrust#tradeTime=20150323171048@uid=lisp^securityId=000002^securityName=WKA^quantity=100^price=6.5^direction=BID");
			orderRequest.flush();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ClientSocketConnector();
	}
}
