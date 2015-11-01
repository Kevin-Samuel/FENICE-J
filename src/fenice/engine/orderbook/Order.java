/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */

package fenice.engine.orderbook;

import java.util.Date;




/*
 *  Description: basic order book class
 */

public class Order implements Cloneable{
	private String accountId;
	
	private String orderId;
	private String securityId;
	private String securityName;
	
	private double price;
	private int quantity;
	private TradeDirection tradeDirection;
	private EntrustType entrustType;

	private Date orderTime;
	
	/*
	 * Constructor
	 * @param orderId securityId quantity orderTime TradeDirection
	 */
	public Order(String accountId, String orderId, String securityId, String securityName, int quantity, double price, TradeDirection tradeDirection, EntrustType entrustType, Date orderTime) {
		this.setAccountId(accountId);
		this.setOrderId(orderId);
		this.setSecurityId(securityId);
		this.setSecurityName(securityName);
		this.setQuantity(quantity);
		this.setPrice(price);
		this.setTradeDirection(tradeDirection);
		this.setEntrustType(entrustType);
		this.setOrderTime(orderTime);
	}
	
	//create an instance of limit order book
	public Order(String accountId, String orderId, String securityId, String securityName, int quantity, double price, TradeDirection tradeDirection) {
		this(accountId, orderId, securityId, securityName, quantity, price, tradeDirection, EntrustType.LIMIT, new Date());
	}
	
	//create an instance of market order book
	public Order(String accountId, String orderId, String securityId, String securityName, int quantity, TradeDirection tradeDirection) {
		this(accountId, orderId, securityId, securityName, quantity, 0.0, tradeDirection, EntrustType.MARKET, new Date());
	}
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/*
	 * getter & setter methods
	 * 
	 */
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public EntrustType getEntrustType() {
		return entrustType;
	}

	public void setEntrustType(EntrustType entrustType) {
		this.entrustType = entrustType;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Order order = (Order)this.clone();
		order.setOrderTime((Date)order.getOrderTime().clone());
		return order;
	}
	
	public void printOrder() {
		System.out.println("|--------------------------------------------------|");
		System.out.println("| account Id    | " + String.format("%-30s", this.getAccountId() + " | "));
		System.out.println("| order Id        | " + String.format("%-30s", this.getOrderId()) + " | ");
		System.out.println("| security Id     | " + String.format("%-30s", this.getSecurityId()) + " | ");
		System.out.println("| security Name   | " + String.format("%-30s", this.getSecurityName()) + " | ");
		System.out.println("| entrust type    | " + String.format("%-30s", this.getEntrustType()) + " | ");
		if (this.getEntrustType() == EntrustType.LIMIT)
			System.out.println("| price           | " + String.format("%-30s", this.getPrice()) + " | ");
		System.out.println("| quantity        | " + String.format("%-30s", this.getQuantity()) + " | ");
		System.out.println("| trade direction | " + String.format("%-30s", this.getTradeDirection()) + " | ");
		System.out.println("|--------------------------------------------------|");
	}
	
	public static void main(String[] args) {
		Order lorder1 = new Order("000001", "1001", "000002", "WKA", 100, 7.8, TradeDirection.ASK);
		lorder1.printOrder();
	}
	
}
