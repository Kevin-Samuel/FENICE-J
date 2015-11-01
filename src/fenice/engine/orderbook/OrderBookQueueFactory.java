/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */
package fenice.engine.orderbook;

import java.util.Comparator;
import java.util.Random;

public class OrderBookQueueFactory<E extends Order> {
	
	/*
	 * Description: create a queue
	 * @param dirction ("BID" or "ASK")
	 */
	
	public OrderBookQueue<E> createQueue(String direction) {
		OrderBookQueue<E> orderBookQueue = null;
		Comparator<E> orderBookRule = null;
		switch(direction) {
			case "BID":
				orderBookRule = new BidOrderBookRule<E>();
				break;
			case "ASK":
				orderBookRule = new AskOrderBookRule<E>();
				break;
			default:
				orderBookRule = new OrderBookRule<E>();
				break;
		}
		orderBookQueue = new OrderBookQueue<E>(orderBookRule);
		return orderBookQueue;
	}
	
	public static void main(String[] args) {
		OrderBookQueueFactory<Order> factory = new OrderBookQueueFactory<Order>();
		OrderBookQueue<Order> queue = factory.createQueue("ASK");
		
		Random r = new Random(47);
		OrderFactory orderfactory = new OrderFactory();
		Order[] order = new Order[10];
		
		order[0] = orderfactory.newLimitOrder("fenice", "000002", "WKA", 100, 7.8, OrderFactory.ASK);
		queue.offer(order[0]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[1] = orderfactory.newLimitOrder("hasayake", "000002", "WKA", 100, 8.8, OrderFactory.ASK);
		queue.offer(order[1]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[2] = orderfactory.newLimitOrder("kobe", "000002", "WKA", 100, 6.4, OrderFactory.ASK);
		queue.offer(order[2]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[3] = orderfactory.newLimitOrder("james", "000002", "WKA", 100, 9.2, OrderFactory.ASK);
		queue.offer(order[3]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[4] = orderfactory.newLimitOrder("jordan", "000002", "WKA", 100, 8.8, OrderFactory.ASK);
		queue.offer(order[4]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		
		while (queue.size() > 0) {
			Order book = queue.poll();
			book.printOrder();
		}
		
	}
}
