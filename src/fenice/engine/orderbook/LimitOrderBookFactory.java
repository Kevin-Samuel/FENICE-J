/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

public class LimitOrderBookFactory extends AbstractOrderBookFactory {
	
	public LimitOrderBook newLimitOrderBook(String uid, String securityId, String securityName, int quantity, String direction, double price) {
		String orderBookId = getOrderBookId(uid);
		TradeDirection tradeDirection = getTradeDirection(direction);
		return new LimitOrderBook(orderBookId, securityId, securityName, quantity, tradeDirection, price);
	}
}
