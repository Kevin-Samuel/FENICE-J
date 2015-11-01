/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

import java.util.Date;
import java.text.SimpleDateFormat;

/*
 * Description : abstract factory of order
 * function    : create instance of order
 */

public abstract class AbstractOrderFactory {
	
	public final static String BID = "BID";
	public final static String ASK = "ASK";
	
	public final static String MARKET = "MARKET";
	public final static String LIMIT = "LIMIT";
	
	public static long AutoIncrementId = 1000;
	
	public static String getOrderId(String accountId, String direction) {
		// rule : time + uid + autoincrementid
		String direct = String.valueOf(direction.charAt(0));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = format.format(new Date());
		String orderBookId = direct + time + accountId + Long.toString(AutoIncrementId);
		AutoIncrementId++;
		return orderBookId;
	}
	
	public static TradeDirection getTradeDirection(String direction) {
		TradeDirection tradeDirection = TradeDirection.BID;
		switch (direction) {
			case "BID": 
				tradeDirection = TradeDirection.BID;
				break;
			case "ASK":
				tradeDirection = TradeDirection.ASK;
				break;
		}
		return tradeDirection;
	}
	
	
	public abstract Order newLimitOrder(String uid, String securityId, String securityName, int quantity, double price, String direction);
	
	public abstract Order newMarketOrder(String uid, String securityId, String securityName, int quantity, String direction);
	
}
