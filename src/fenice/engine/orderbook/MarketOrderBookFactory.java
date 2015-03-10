package fenice.engine.orderbook;

public class MarketOrderBookFactory extends AbstractOrderBookFactory{
	
	public MarketOrderBook newLimitOrderBook(String uid, String securityId, String securityName, int quantity, String direction) {
		String orderBookId = getOrderBookId(uid);
		TradeDirection tradeDirection = getTradeDirection(direction);
		return new MarketOrderBook(orderBookId, securityId, securityName, quantity, tradeDirection);
	}
}
