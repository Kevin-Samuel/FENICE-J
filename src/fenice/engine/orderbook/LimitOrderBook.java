/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */
package fenice.engine.orderbook;

/*
 * Description: class of Limit OrderBook
 * 
 */

public class LimitOrderBook extends OrderBook implements Comparable<LimitOrderBook>{
	public final String EntrustType = "LIMIT";
	
	//price of limit order book
	private double price;
	
	/*
	 * Constructor
	 * @param orderBookId securityId quantity orderTime TradeDirection price
	 */
	public LimitOrderBook(String orderBookId, String securityId, String securityName, int quantity, TradeDirection tradeDirection, double price) {
		super(orderBookId, securityId, securityName, quantity, tradeDirection);
		this.price = price;
	}

	/*
	 * setter & getter methods
	 * 
	 */
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	/*
	 * Description: implements the comparable interface
	 * rule: price precedence & time precedence
	 * 
	 */
	@Override
	public int compareTo(LimitOrderBook book) {
		if ((this.getTradeDirection() != book.getTradeDirection()) || (this.getSecurityId() != book.getSecurityId() )) {
			return -1;
		}
		if ( (this.getPrice() > book.getPrice()) || ((this.getPrice() == book.getPrice()) && (this.getOrderTime().before(book.getOrderTime()))) ) {
			return 1;
		} else if ((this.getOrderTime().getTime() == book.getOrderTime().getTime()) && (this.getPrice() == book.getPrice())) {
			return 0;
		} else {
			return -1;
		}
	}
	
	public static void main(String[] args) {
		LimitOrderBook order1 = new LimitOrderBook("0001", "JTYH", "交通银行", 1, TradeDirection.BID, 6.0);
		LimitOrderBook order2 = new LimitOrderBook("0001", "MSYH", "MSYH", 1, TradeDirection.ASK, 9.0);
		System.out.println(order1.compareTo(order2));
		System.out.println(order1.getTradeDirection() == order2.getTradeDirection());
	}
	
}
