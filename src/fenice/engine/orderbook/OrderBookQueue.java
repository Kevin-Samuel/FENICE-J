/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

import java.util.concurrent.PriorityBlockingQueue;

/*
 * Description: bid or ask queue 
 * function   : store the order book that have not matched
 * 
 */

public class OrderBookQueue {
	
	private PriorityBlockingQueue<? extends OrderBook> queue;
}
