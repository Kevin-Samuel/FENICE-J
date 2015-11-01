/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

public class OrderFactory extends AbstractOrderFactory {
	
	/*
	 * create an instance of limit order book
	 * @see fenice.engine.orderbook.AbstractOrderBookFactory#newLimitOrderBook(java.lang.String, java.lang.String, java.lang.String, int, double, java.lang.String)
	 */
	public Order newLimitOrder(String accountId, String securityId, String securityName, int quantity, double price, String direction) {
		String orderBookId = getOrderId(accountId, direction);
		TradeDirection tradeDirection = getTradeDirection(direction);
		return new Order(accountId, orderBookId, securityId, securityName, quantity, price, tradeDirection);
	}
	
	/*
	 * create an instance of market order book
	 * @see fenice.engine.orderbook.AbstractOrderBookFactory#newMarketOrderBook(java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	public Order newMarketOrder(String accountId, String securityId, String securityName, int quantity, String direction) {
		String orderBookId = getOrderId(accountId, direction);
		TradeDirection tradeDirection = getTradeDirection(direction);
		return new Order(accountId, orderBookId, securityId, securityName, quantity, tradeDirection);
	}
	
	public static void main(String[] args) {
		OrderFactory factory = new OrderFactory();
		Order order = factory.newLimitOrder("fenice", "000002", "WKA", 100, 7.8, OrderFactory.ASK);
		order.printOrder();
		Order order1 = factory.newLimitOrder("hasayake", "600000", "PFYH", 100, 9.9, OrderFactory.ASK);
		order1.printOrder();

	}
}
