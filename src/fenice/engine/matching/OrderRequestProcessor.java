package fenice.engine.matching;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class OrderRequestProcessor implements Runnable{
	
	private static String ORDER_RECEIVE_QUEUE = "ORDER_RECEIVE_QUEUE";
	private Socket clientSocket;
	private String ipAddr;
	
	public static String getORDER_RECEIVE_QUEUE() {
		return ORDER_RECEIVE_QUEUE;
	}

	public static void setORDER_RECEIVE_QUEUE(String oRDER_RECEIVE_QUEUE) {
		ORDER_RECEIVE_QUEUE = oRDER_RECEIVE_QUEUE;
	}
	
	/*
	 * Constructor
	 * 
	 */
	public OrderRequestProcessor(Socket socket) {
		setClientSocket(socket);
		setIpAddr("localhost");
	}
	
	public void setClientSocket(Socket socket) {
		clientSocket = socket;
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}
	
	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/*
	 * send request message to rabbit 
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		try {
			DataInputStream orderMsgFromClient = new DataInputStream(clientSocket.getInputStream());
			String orderMsg = orderMsgFromClient.readUTF();
			//System.out.println(orderMsg);
			ConnectionFactory factory = new ConnectionFactory();
			
			factory.setHost(this.getIpAddr());
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(ORDER_RECEIVE_QUEUE, true, false, false, null);
			
			channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderMsg.getBytes());
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
