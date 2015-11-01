package fenice.fix.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Header {
	private String BeginString;
	private String MsgType;
	private Date TradeTime;
	
	public Header(String beginString, String msgType, Date tradeTime) {
		setBeginString(beginString);
		setMsgType(msgType);
		setTradeTime(tradeTime);
	}
	
	public String getBeginString() {
		return BeginString;
	}
	public void setBeginString(String beginString) {
		BeginString = beginString;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public Date getTradeTime() {
		return TradeTime;
	}
	public void setTradeTime(Date tradeTime) {
		this.TradeTime = tradeTime;
	}
	
	public static String encode(Header head) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return "BeginString=" + head.BeginString + "#MsgType=" + head.MsgType + "#tradeTime=" + format.format(head.TradeTime); 
	}
	
	public static Header decode(String headString) throws ParseException {
		String[] headArr = headString.split("#");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String beginString = Tag.decode(headArr[0]).getTagValue();
		String msgtype = Tag.decode(headArr[1]).getTagValue();
		Date date = format.parse(Tag.decode(headArr[2]).getTagValue());
		return new Header(beginString, msgtype, date);
	}
	
	public static void main(String[] args) throws ParseException {
		Header head = new Header("Fix4", "entrust", new Date());
		String str = Header.encode(head);
		System.out.println(str);
		Header h = Header.decode(str);
		System.out.println(h.toString());
	}
}
