/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */
package fenice.engine.orderbook;

import java.util.Comparator;

/*
 * Description:  the queue rule of ask order book 
 * Comparator
 */
public class AskOrderBookRule<E extends Order> implements Comparator<E> {
	
	/*
	 * price precedence & time precedence
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(E orderbook1, E orderbook2) {
		
		if (orderbook1.getEntrustType() == EntrustType.MARKET && orderbook2.getEntrustType() == EntrustType.LIMIT) {
			return -1;
		}
		if (orderbook1.getEntrustType() == EntrustType.LIMIT && orderbook2.getEntrustType() == EntrustType.MARKET) {
			return 1;
		}
		if (orderbook1.getEntrustType() == EntrustType.LIMIT && orderbook2.getEntrustType() == EntrustType.LIMIT) {
			double priceCmp = orderbook1.getPrice() - orderbook2.getPrice();
			int timeCmp = orderbook1.getOrderTime().compareTo(orderbook2.getOrderTime());
			if (priceCmp < 0.0 || (priceCmp == 0.0 && timeCmp < 0)) {
				return -1;
			} else if (priceCmp == 0.0 && timeCmp == 0) {
				return 0;
			} else {
				return 1;
			}
		}
		if (orderbook1.getEntrustType() == EntrustType.MARKET && orderbook2.getEntrustType() == EntrustType.MARKET) {
			int cmp = orderbook1.getOrderTime().compareTo(orderbook2.getOrderTime());
			if (cmp < 0) {
				return -1;
			} else if (cmp > 0) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}
}
