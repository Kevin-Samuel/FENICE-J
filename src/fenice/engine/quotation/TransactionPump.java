package fenice.engine.quotation;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import fenice.fix.business.SingleTransactionGenerator;


public class TransactionPump implements Runnable {
	
	private static String TRANSACTION_BROADCAST_QUEUE = "TRANSACTION_BROADCAST_QUEUE";
	
	public static ConnectionFactory factory = new ConnectionFactory();
	private static Connection connection;
	
	private Transaction deal;
	
	private static String ipAddr;
	
	public static String getTRANSACTION_BROADCAST_QUEUE() {
		return TRANSACTION_BROADCAST_QUEUE;
	}

	public static void setTRANSACTION_BROADCAST_QUEUE(
			String tRANSACTION_BROADCAST_QUEUE) {
		TRANSACTION_BROADCAST_QUEUE = tRANSACTION_BROADCAST_QUEUE;
	}

	public static Connection getConnectionInstance() throws IOException {
		if(connection != null) {
			return connection;
		} else {
			factory.setHost(getIpAddr());
			setConnection(factory.newConnection());
		}
		return connection;
	}
	
	public TransactionPump(Transaction deal) throws IOException {
		setDeal(deal);
		setIpAddr("localhost");
		connection = getConnectionInstance();
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		TransactionPump.connection = connection;
	}

	public Transaction getDeal() {
		return deal;
	}

	public void setDeal(Transaction deal) {
		this.deal = deal;
	}


	public static String getIpAddr() {
		return ipAddr;
	}

	public static void setIpAddr(String ipAddr) {
		TransactionPump.ipAddr = ipAddr;
	}

	@Override
	public void run() {
		
		try {

			Channel channel = connection.createChannel();
			channel.queueDeclare(TRANSACTION_BROADCAST_QUEUE, true, false, false, null);
			
			String transactionMsgStr = SingleTransactionGenerator.convertTransactionToMsg(getDeal());
			
			channel.basicPublish("", TRANSACTION_BROADCAST_QUEUE, null, transactionMsgStr.getBytes());
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
