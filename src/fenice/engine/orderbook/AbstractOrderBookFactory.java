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
 * Description : abstract factory of order book
 * function    : create instance of order book
 */

public abstract class AbstractOrderBookFactory {
	
	public final static String BID = "BID";
	public final static String ASK = "ASK";
	
	public static long AutoIncrementId = 1000;
	
	public static String getOrderBookId(String uid) {
		// rule : time + uid + autoincrementid
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = format.format(new Date());
		String orderBookId = time + uid + Long.toString(AutoIncrementId);
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
	
}
