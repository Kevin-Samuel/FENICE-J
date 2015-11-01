/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-4-20
 * @Version : 1.0.1
 */

package fenice.engine.delivery;

import fenice.models.AccountOperation;
import fenice.models.DeliveryOperation;
import fenice.models.OrderOperation;
import fenice.models.SecurityOperation;




/*
 * clear and delivery thread operation class 
 * 
 * 
 */
public class DeliveryThread implements Runnable {
	
	private String bidOrderId;
	private String askOrderId;
	private String securityId;
	private double price;
	private int quantity;
	
	public static AccountOperation account = new AccountOperation();
	public static OrderOperation order = new OrderOperation();
	public static SecurityOperation security = new SecurityOperation();
	public static DeliveryOperation delivery = new DeliveryOperation();
	
	public DeliveryThread(String bidOrderId, String askOrderId, String securityId, double price, int quantity) {
		setBidOrderId(bidOrderId);
		setAskOrderId(askOrderId);
		setPrice(price);
		setQuantity(quantity);
		setSecurityId(securityId);
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getSecurityId() {
		return securityId;
	}
	
	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}
	

	/*
	 *  clear and delivery thread
	 *  
	 */
	@Override
	public void run() {
		
		AccountOperation account = new AccountOperation();
		OrderOperation order = new OrderOperation();
		SecurityOperation security = new SecurityOperation();
		DeliveryOperation delivery = new DeliveryOperation();
		
		String bidAccountId = order.getAccountId(bidOrderId);
		String askAccountId = order.getAccountId(askOrderId);
		
		account.increaseSecurityHolding(bidAccountId, securityId, quantity);
		account.decreaseSecurityHolding(askAccountId, securityId, quantity);
		
		order.agreeOrder(bidOrderId);
		order.agreeOrder(askOrderId);
		
		double totalTurnOver = price * quantity;
		account.updateBalance(bidAccountId, -1 * totalTurnOver);
		account.updateBalance(askAccountId, totalTurnOver);
		
		security.updateSecurityPrice(securityId, price);
		
		delivery.addTradeRecord(bidAccountId, askAccountId, "agreed", new java.util.Date(), price, quantity);
		delivery.addDealRecord(bidAccountId, "BID", securityId, price, quantity, new java.util.Date(), "agreed");
		delivery.addDealRecord(askAccountId, "ASK", securityId, price, quantity, new java.util.Date(), "agreed");
		
	}
}
