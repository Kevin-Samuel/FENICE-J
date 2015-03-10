/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

/*
 * Description: class of Market OrderBook
 * 
 */

public class MarketOrderBook extends OrderBook implements Comparable<MarketOrderBook>{
	public final String EntrustType = "MARKET";
	
	public MarketOrderBook(String orderBookId, String securityId, String securityName, int quantity, TradeDirection tradeDirection) {
		super(orderBookId, securityId, securityName, quantity, tradeDirection);
	}
	
	/*
	 * Description: implements the comparable interface
	 * rule: price precedence & time precedence
	 * 
	 */
	@Override
	public int compareTo(MarketOrderBook book) {
		if ((this.getTradeDirection() != book.getTradeDirection()) || (this.getSecurityId() != book.getSecurityId() )) {
			return -1;
		}
		if (this.getOrderTime().before(book.getOrderTime())) {
			return 1;
		} else {
			return -1;
		}
	}
}
