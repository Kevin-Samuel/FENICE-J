/*
 * CopyRight (c) FENICE 
 * @Author  : fenicesun@tencent.com
 * @Date     : 2015-3-8
 * @Version : 1.0.1
 */
package fenice.engine.quotation;

import java.util.Date;

public class TransactionFactory {
	
	public static long AutoIncrementId = 1;
	
	public static String getDealId(String securityId) {
		String ID = securityId + String.valueOf(AutoIncrementId);
		AutoIncrementId++;
		return ID;
	}
	
	public Transaction newTransaction(String securityId, String securityName, String bidOrderId, 
			String askOrderId, double dealPrice, int dealQuantity) {
		String transactionId = getDealId(securityId);
		return new Transaction(transactionId, securityId, securityName, bidOrderId, askOrderId, dealPrice, dealQuantity);
	}
	
	public Transaction newTransaction(String securityId, String securityName, String bidOrderId, 
			String askOrderId, double dealPrice, int dealQuantity, Date dealTime) {
		String transactionId = getDealId(securityId);
		return new Transaction(transactionId, securityId, securityName, bidOrderId, askOrderId, dealPrice, dealQuantity, dealTime);
	}
	
	public Transaction newTransaction(String transactionId, String securityId, String securityName, String bidOrderId, 
			String askOrderId, double dealPrice, int dealQuantity, Date dealTime) {
		return new Transaction(transactionId, securityId, securityName, bidOrderId, askOrderId, dealPrice, dealQuantity, dealTime);
	}
	
	public static void main() {
		TransactionFactory factory = new TransactionFactory();
		Transaction deal = factory.newTransaction("000002", "WKA", "320925", "19930827", 12.98, 100);
		deal.printTransaction();
	}
}
