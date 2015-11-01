package fenice.common;

import java.io.IOException;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


import fenice.engine.orderbook.OrderFactory;
import fenice.fix.business.OrderSingleRequest;


public class ClientOrderSingle {
	private static String ORDER_RECEIVE_QUEUE = "ORDER_RECEIVE_QUEUE";
	public static void main(String[] args) throws IOException, InterruptedException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(ORDER_RECEIVE_QUEUE, true, false, false, null);
		/*
		int i = 0;
		while (true) {
				
			String message = "Order: " + i++;
			channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, message.getBytes());
			System.out.println(" [x] send : '" + message);
			Thread.sleep(1000);
		} 
 			*/
		
		String orderStr1 = OrderSingleRequest.convertOrderToMsg("fenice", "000002", "WKA", 100, 7.1, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr1.getBytes());
		Thread.sleep(1000);
		
		String orderStr2 = OrderSingleRequest.convertOrderToMsg("kevin", "000002", "WKA", 100, 7.0, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr2.getBytes());
		Thread.sleep(1000);
		
		String orderStr3 = OrderSingleRequest.convertOrderToMsg("samuel", "000002", "WKA", 100, 6.9, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr3.getBytes());
		Thread.sleep(1000);
		
		String orderStr4 = OrderSingleRequest.convertOrderToMsg("kobe", "000002", "WKA", 100, 6.6, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr4.getBytes());
		Thread.sleep(1000);
		
		String orderStr5 = OrderSingleRequest.convertOrderToMsg("james", "000002", "WKA", 100, 6.8, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr5.getBytes());
		Thread.sleep(1000);
		
		String orderStr6 = OrderSingleRequest.convertOrderToMsg("pual", "000002", "WKA", 100, 6.7, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr6.getBytes());
		Thread.sleep(1000);
			
		
		String orderStr7 = OrderSingleRequest.convertOrderToMsg("jordan", "000002", "WKA", 100, 7.6, OrderFactory.ASK);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr7.getBytes());
		Thread.sleep(1000);
		
		String orderStr8 = OrderSingleRequest.convertOrderToMsg("carter", "000002", "WKA", 100, 7.3, OrderFactory.ASK);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr8.getBytes());
		Thread.sleep(1000);
		
		String orderStr9 = OrderSingleRequest.convertOrderToMsg("garnett", "000002", "WKA", 100, 7.2, OrderFactory.ASK);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr9.getBytes());
		Thread.sleep(1000);
		
		String orderStr10 = OrderSingleRequest.convertOrderToMsg("onill", "000002", "WKA", 100, 7.5, OrderFactory.ASK);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr10.getBytes());
		Thread.sleep(1000);
		
		String orderStr11 = OrderSingleRequest.convertOrderToMsg("ducan", "000002", "WKA", 100, 7.4, OrderFactory.ASK);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr11.getBytes());
		Thread.sleep(1000);
		
		//----------------------------------------------------------------------------
		String orderStr12 = OrderSingleRequest.convertOrderToMsg("owen", "000002", "WKA", 150, 7.3, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr12.getBytes());
		Thread.sleep(1000);
		
		String orderStr13 = OrderSingleRequest.convertOrderToMsg("yanwei", "000002", "WKA", 150, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr13.getBytes());
		Thread.sleep(1000);
		
		String orderStr14 = OrderSingleRequest.convertOrderToMsg("lisp", "000002", "WKA", 100, 6.5, OrderFactory.BID);
		channel.basicPublish("", ORDER_RECEIVE_QUEUE, null, orderStr14.getBytes());
		Thread.sleep(1000);
	}
}
