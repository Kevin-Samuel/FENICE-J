package fenice.engine.matching;

import fenice.engine.orderbook.EntrustType;
import fenice.engine.orderbook.Order;
import fenice.engine.orderbook.TradeDirection;
import fenice.models.AccountOperation;
import fenice.models.SecurityOperation;

public class OrderVerifier {
	
	private String securityId;
	
	public OrderVerifier(String securityId) {
		this.securityId = securityId;
	}
	
	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public boolean checkOrder(Order order) {
		
		if (order.getSecurityId().equals(getSecurityId()) == false) {
			return false;
		}
		
		TradeDirection tradeDirection; 
		tradeDirection = order.getTradeDirection();
		switch(tradeDirection) {
			case BID:
				return checkBidOrder(order);
			case ASK:
				return checkAskOrder(order);
		}
		return true;
	}
	
	
	public static boolean checkBidOrder(Order bidOrder) {
		
		AccountOperation account = new AccountOperation();
		SecurityOperation security = new SecurityOperation();
		if (account.hasThisAccount(bidOrder.getAccountId()) == false) {
			return false;
		}
		
		double balance = account.queryBalance(bidOrder.getAccountId());
		String securityId = bidOrder.getSecurityId();
		
		
		int shares = security.getSecurityShares(securityId);
		if (shares == -1) {
			return false;
		}
		
		double orderPrice = bidOrder.getPrice();
		int orderQuantity = bidOrder.getQuantity();
		if (bidOrder.getEntrustType() == EntrustType.LIMIT && orderPrice * orderQuantity > balance) {
			return false;
		}
		
		if (bidOrder.getQuantity() > shares) {
			return false;
		}
		
		return true;
	}
	
	public static boolean checkAskOrder(Order askOrder) {
		
		AccountOperation account = new AccountOperation();
		SecurityOperation security = new SecurityOperation();
		
		if (account.hasThisAccount(askOrder.getAccountId()) == false) {
			return false;
		}
		String securityId = askOrder.getSecurityId();
		
		if (security.hasThisSecurity(securityId) == false) {
			return false;
		}
		
		int holdings = account.querySecurityHoldingNum(askOrder.getAccountId(), securityId);
		if (askOrder.getQuantity() > holdings) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		Order lorder1 = new Order("000001", "1001", "FENICE", "FENICE", 100001, 7.8, TradeDirection.ASK);
		OrderVerifier ov = new OrderVerifier("FENICE");
		System.out.println(ov.checkOrder(lorder1));
	}
}
