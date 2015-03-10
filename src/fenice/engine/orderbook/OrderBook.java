/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

import java.util.Date;


/*
 * Description : Direction of securities trading
 * 
 */
enum TradeDirection {
	BID, ASK
}

/*
 *  Description: basic order book class
 */

public abstract class OrderBook {
	
	private String orderBookId;
	private String securityId;
	private String securityName;
	
	private int quantity;
	private TradeDirection tradeDirection;

	private Date orderTime;
	
	/*
	 * Constructor
	 * @param orderBookId securityId quantity orderTime TradeDirection
	 */
	public OrderBook(String orderBookId, String securityId, String securityName, int quantity, TradeDirection tradeDirection, Date orderTime) {
		this.orderBookId = orderBookId;
		this.securityId  = securityId;
		this.securityName = securityName;
		this.quantity  = quantity;
		this.tradeDirection = tradeDirection;
		this.orderTime = orderTime;
	}
	
	public OrderBook(String orderBookId, String securityId, String securityName, int quantity, TradeDirection tradeDirection) {
		this(orderBookId, securityId, securityName, quantity, tradeDirection, new Date());
	}
	
	/*
	 * getter & setter methods
	 * 
	 */
	public String getOrderBookId() {
		return orderBookId;
	}
	
	public void setOrderBookId(String orderBookId) {
		this.orderBookId = orderBookId;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}
	
	public String getSecurityName() {
		return securityName;
	}
	
	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public TradeDirection getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(TradeDirection tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	
	
}
