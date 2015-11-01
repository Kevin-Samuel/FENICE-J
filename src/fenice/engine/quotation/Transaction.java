/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */
package fenice.engine.quotation;

import java.util.Date;

/*
 * The deal info
 * 
 */
public class Transaction {
	
	private String transactionId;
	private String securityId;
	private String securityName;
	
	private String bidOrderId;
	private	String askOrderId;
	
	private double dealPrice;
	private int dealQuantity;
	
	private Date dealTime;
	
	/*
	 * Constructor
	 * 
	 */
	public Transaction(String transactionId, String securityId, String securityName, String bidOrderId, 
			String askOrderId, double dealPrice, int dealQuantity, Date dealTime) {
		setTransactionId(transactionId);
		setSecurityId(securityId);
		setSecurityName(securityName);
		setBidOrderId(bidOrderId);
		setAskOrderId(askOrderId);
		setDealPrice(dealPrice);
		setDealQuantity(dealQuantity);
		setDealTime(dealTime);
	}
	
	public Transaction(String transactionId, String securityId, String securityName, String bidOrderId, 
			String askOrderId, double dealPrice, int dealQuantity) {
		this(transactionId, securityId, securityName, bidOrderId, askOrderId, dealPrice, dealQuantity, new Date());
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

	public String getBidOrderId() {
		return bidOrderId;
	}

	public void setBidOrderId(String bidOrderId) {
		this.bidOrderId = bidOrderId;
	}

	public String getAskOrderId() {
		return askOrderId;
	}

	public void setAskOrderId(String askOrderId) {
		this.askOrderId = askOrderId;
	}

	public double getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(double dealPrice) {
		this.dealPrice = dealPrice;
	}

	public int getDealQuantity() {
		return dealQuantity;
	}

	public void setDealQuantity(int dealQuantity) {
		this.dealQuantity = dealQuantity;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	
	public void printTransaction() {
		System.out.println("|-----------------------------------" + dealTime.toString() + "-----------------------------|");
		System.out.println("| Transaction ID : " + String.format("%-73s", getTransactionId()) + " |");
		System.out.println("| bid order book ID: " + String.format("%s", getBidOrderId()) + "  | ask order book ID: " + getAskOrderId() + "  |");
		System.out.println("| security ID: " + String.format("%-30s", getSecurityId()) + "| security Name: " + String.format("%-30s", getSecurityName()) + " |");
		System.out.println("| deal price : " + String.format("%-77s", String.valueOf(getDealPrice())) + " |");
		System.out.println("| deal quantity : " + String.format("%-74s", String.valueOf(getDealQuantity())) + " |");
		System.out.println("|--------------------------------------------------------------------------------------------|");
	}
	
	public static void main(String[] args) {
		Transaction deal = new Transaction("000001", "000002", "WKA", "320925", "19930827", 19.88, 200);
		deal.printTransaction();
	}
}
