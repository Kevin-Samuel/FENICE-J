package fenice.fix.message;

import java.util.ArrayList;
import java.util.List;

public class Body {
	private List<Tag> bodyTagList;
	
	public Body() {
		bodyTagList = new ArrayList<Tag>();
	}
	
	public Body(List<Tag> bodytaglist) {
		setBodyTagList(bodytaglist);
	}
	
	public List<Tag> getBodyTagList() {
		return bodyTagList;
	}
	
	public void setBodyTagList(List<Tag> bodyTagList) {
		this.bodyTagList = bodyTagList;
	}
	
	public void addTag(Tag tag) {
		bodyTagList.add(tag);
	}
	
	public String getValue(String key) {
		String value = null;
		for(int i = 0; i < bodyTagList.size(); i++) {
			if (bodyTagList.get(i).getTagId().equals(key)) {
				value = bodyTagList.get(i).getTagValue();
				break;
			}
		}
		return value;
	}
	
	public static String encode(Body body) {
		String message = new String();
		for (int i = 0; i < body.bodyTagList.size(); ++i) {
			message += Tag.encode(body.bodyTagList.get(i));
			if (i != body.bodyTagList.size() - 1) {
				message += "^";
			}
		}
		return message;
	}
	
	public static Body decode(String bodyString) {
		List<Tag> bodytaglist = new ArrayList<Tag>();
		String[] taglistArr = bodyString.split("\\^");
	
		for (int i = 0; i < taglistArr.length; ++i) {
			Tag tag = Tag.decode(taglistArr[i]);
			bodytaglist.add(tag);
		}
		return new Body(bodytaglist);
	}
	
	public static void main(String[] args) {
		Body body = new Body();
		body.addTag(new Tag("price", "10.5"));
		body.addTag(new Tag("direction", "BID"));
		body.addTag(new Tag("uid", "320925"));
		String str = Body.encode(body);
		System.out.println(str);
		Body b = Body.decode(str);
		System.out.println(b.getValue("uid"));
	
	}
}
