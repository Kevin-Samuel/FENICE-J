package fenice.fix.message;

import java.text.ParseException;
import java.util.Date;

public class Msg {
	private Header head;
	private Body body;
	private Tail tail;
	
	public Msg(Header head, Body body, Tail tail) {
		setHead(head);
		setBody(body);
		setTail(tail);
	}

	public Header getHead() {
		return head;
	}

	public void setHead(Header head) {
		this.head = head;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}
	
	public static String encode(Msg message) {
		return Header.encode(message.getHead()) + "@" + Body.encode(message.getBody());
	}
	
	public static Msg decode(String msgString) throws ParseException {
		String[] msgArr = msgString.split("@");
		return new Msg(Header.decode(msgArr[0]), Body.decode(msgArr[1]), new Tail());
	}
	
	public static void main(String[] args) throws ParseException {
		Header head = new Header("Fix4.1", "entrust", new Date());
		Body body = new Body();
		body.addTag(new Tag("uid", "320925"));
		body.addTag(new Tag("securityId", "000002"));
		body.addTag(new Tag("securityName", "WKA"));
		body.addTag(new Tag("quantity", "1000"));
		body.addTag(new Tag("price", "12.27"));
		body.addTag(new Tag("direction", "bid"));
		Msg single = new Msg(head, body, new Tail());
		String str = Msg.encode(single);
		System.out.println(str);
		Msg obj = Msg.decode(str);
		System.out.println(obj.toString());
		
	}

}
