package fenice.fix.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fenice.engine.quotation.Transaction;
import fenice.engine.quotation.TransactionFactory;
import fenice.fix.message.Body;
import fenice.fix.message.Header;
import fenice.fix.message.Msg;
import fenice.fix.message.Tag;
import fenice.fix.message.Tail;

public class SingleTransactionGenerator {

	public static String convertTransactionToMsg(Transaction deal) {
		Header head = new Header("Fix", "Transaction", new Date());
		Body body = new Body();
		body.addTag(new Tag("transactionId", deal.getTransactionId()));
		body.addTag(new Tag("securityId", deal.getSecurityId()));
		body.addTag(new Tag("securityName", deal.getSecurityName()));
		body.addTag(new Tag("bidOrderId", deal.getBidOrderId()));
		body.addTag(new Tag("askOrderId", deal.getAskOrderId()));
		body.addTag(new Tag("dealPrice", String.valueOf(deal.getDealPrice())));
		body.addTag(new Tag("dealQuantity", String.valueOf(deal.getDealQuantity())));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = format.format(deal.getDealTime());
		body.addTag(new Tag("dealTime", time));
		Msg order = new Msg(head, body, new Tail());
		String orderStr = Msg.encode(order);
		return orderStr;
	}
	
	public static Transaction convertMsgToTransaction(String message) throws ParseException {
		TransactionFactory factory = new TransactionFactory();
		Transaction deal = null;
		Msg msgObj = Msg.decode(message);

		String transactionId = msgObj.getBody().getValue("transactionId");
		String securityId = msgObj.getBody().getValue("securityId");
		String securityName = msgObj.getBody().getValue("securityName");
		String bidOrderId = msgObj.getBody().getValue("bidOrderId");
		String askOrderId = msgObj.getBody().getValue("askOrderId");
		double dealPrice = Double.valueOf(msgObj.getBody().getValue("dealPrice"));
		int dealQuantity = Integer.valueOf(msgObj.getBody().getValue("dealQuantity"));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dealTime = format.parse(msgObj.getBody().getValue("dealTime"));
		deal = factory.newTransaction(transactionId, securityId, securityName, bidOrderId, askOrderId, dealPrice, dealQuantity, dealTime);
		return deal;
	}
	
	public static void main(String[] args) throws ParseException {
		TransactionFactory factory = new TransactionFactory();
		Transaction deal = factory.newTransaction("00000201", "000002", "WKA", "320925", "199308", 12.88, 500, new Date());
		deal.printTransaction();
		String msg = convertTransactionToMsg(deal);
		System.out.println(msg);
		Transaction deal1 = convertMsgToTransaction(msg);
		System.out.println(deal1.getTransactionId());
	}
}
