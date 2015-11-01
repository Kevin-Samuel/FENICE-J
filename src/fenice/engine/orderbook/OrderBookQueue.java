/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

/*
 * Description: bid or ask queue 
 * function   : store the order book that have not matched
 * 
 */

public class OrderBookQueue<E extends Order> {
	
	protected PriorityBlockingQueue<E> orderBookQueue;
	
	//rule interface
	protected Comparator<E> orderBookRule;
	
	public OrderBookQueue() {
		this(new OrderBookRule<E>());
	}
	
	public OrderBookQueue(Comparator<E> rule) {
		setOrderBookRule(rule);
		setOrderBookQueue(new PriorityBlockingQueue<E>(500, orderBookRule));
	}
	
	
	public PriorityBlockingQueue<E> getOrderBookQueue() {
		return orderBookQueue;
	}

	public void setOrderBookQueue(PriorityBlockingQueue<E> queue) {
		this.orderBookQueue = queue;
	}
	
	public void setOrderBookRule(Comparator<E> orderBookRule) {
		this.orderBookRule = orderBookRule;
	}
	
	/*
	 * add order book into queue
	 * functions: add, offer, put
	 * @param: E (extends OrderBook) e
	 * @return: boolean
	 */
	public boolean add(E orderbook) {
		return orderBookQueue.add(orderbook);
	}
	
	public boolean offer(E orderbook) {
		return orderBookQueue.offer(orderbook);
	}
	
	public void put(E orderbook) {
		orderBookQueue.put(orderbook);
	}
	
	/*
	 * get and remove the font element of queue
	 * functions: poll, take
	 * @return: E (extends OrderBook)
	 */
	public E poll() {
		return orderBookQueue.poll();
	}
	
	public E take() {
		E fontOrder = null;
		try {
			fontOrder = orderBookQueue.take();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fontOrder;
	}
	
	/*
	 * get the size of order book in queue
	 */
	public int size() {
		return orderBookQueue.size();
	}
	
	/*
	 * remove the object in queue 
	 */
	public boolean remove(Object orderBook) {
		return orderBookQueue.remove(orderBook);
	}
	
	/*
	 * remove all order book in queue
	 */
	public void clear() {
		orderBookQueue.clear();
	}
	
	/*
	 * check if this object in queue
	 */
	public boolean contains(Object orderBook) {
		return orderBookQueue.contains(orderBook);
	}
	
	/*
	 * get iterator
	 */
	public Iterator<E> iterator() {
		return orderBookQueue.iterator();
	}
	
	public static void main(String[] args) {
		Random r = new Random(47);
		OrderFactory factory = new OrderFactory();
		Order[] order = new Order[10];
		OrderBookQueue<Order> queue = new OrderBookQueue<Order>(new BidOrderBookRule<Order>());
		order[0] = factory.newMarketOrder("fenice", "000002", "WKA", 100, OrderFactory.ASK);
		queue.offer(order[0]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[1] = factory.newLimitOrder("hasayake", "000002", "WKA", 100, 8.8, OrderFactory.ASK);
		queue.offer(order[1]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[2] = factory.newLimitOrder("kobe", "000002", "WKA", 100, 6.4, OrderFactory.ASK);
		queue.offer(order[2]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[3] = factory.newLimitOrder("james", "000002", "WKA", 100, 9.2, OrderFactory.ASK);
		queue.offer(order[3]);
		try {  
            Thread.sleep(r.nextInt(1000));  
        } catch (InterruptedException e) {  
            e.printStackTrace();
        }
		order[4] = factory.newMarketOrder("jordan", "000002", "WKA", 100, OrderFactory.ASK);
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
		//OrderBook book = queue.poll();
		//book.printOrderBook();
	}
}
