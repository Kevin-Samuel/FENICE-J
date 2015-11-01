package fenice.fix.business;

import java.text.ParseException;
import java.util.Date;

import fenice.engine.orderbook.*;
import fenice.fix.message.*;

public class OrderSingleRequest {
	
	public static String convertOrderToMsg(String uid, String securityId, String securityName, long quantity, String direction) {
		Header head = new Header("Fix", "MarketEntrust", new Date());
		Body body = new Body();
		body.addTag(new Tag("uid", uid));
		body.addTag(new Tag("securityId", securityId));
		body.addTag(new Tag("securityName", securityName));
		body.addTag(new Tag("quantity", String.valueOf(quantity)));
		body.addTag(new Tag("direction", direction));
		Msg order = new Msg(head, body, new Tail());
		String orderStr = Msg.encode(order);
		return orderStr;
	}
	
	public static String convertOrderToMsg(String uid, String securityId, String securityName, long quantity, double price, String direction) {
		Header head = new Header("Fix", "LimitEntrust", new Date());
		Body body = new Body();
		body.addTag(new Tag("uid", uid));
		body.addTag(new Tag("securityId", securityId));
		body.addTag(new Tag("securityName", securityName));
		body.addTag(new Tag("quantity", String.valueOf(quantity)));
		body.addTag(new Tag("price", String.valueOf(price)));
		body.addTag(new Tag("direction", direction));
		Msg order = new Msg(head, body, new Tail());
		String orderStr = Msg.encode(order);
		return orderStr;
	}
	
	public static Order convertMsgToOrder(String message) throws ParseException {
		OrderFactory factory = new OrderFactory();
		Order order = null;
		Msg msgObj = Msg.decode(message);
		String msgType = msgObj.getHead().getMsgType();
		String uid = msgObj.getBody().getValue("uid");
		String securityId = msgObj.getBody().getValue("securityId");
		String securityName = msgObj.getBody().getValue("securityName");
		int quantity = Integer.valueOf(msgObj.getBody().getValue("quantity"));
		String direction = msgObj.getBody().getValue("direction");
		//System.out.println("msgType: " + msgType + " uid: " + uid + " securityId: " + securityId + " securityName: " + securityName + " quantity: " + quantity + " direction: " + direction);
		switch (msgType) {
			case "LimitEntrust":
				double price = Double.valueOf(msgObj.getBody().getValue("price"));
				order = factory.newLimitOrder(uid, securityId, securityName, quantity, price, direction);
				break;
			case "MarketEntrust":
				order = factory.newMarketOrder(uid, securityId, securityName, quantity, direction);
				break;
			case "cancel":
				
				break;
		}
		return order;
	}
	
	public static void main(String[] args) throws ParseException {
		String msg = convertOrderToMsg("320925", "000002", "WKA", 100, 25.88, "ASK");
		System.out.println(msg);
		Order order = convertMsgToOrder(msg);
		order.printOrder();
	}
	
}
