package fenice.fix.message;

public class Tag {
	private String tagId;
	private String tagValue;
	
	public Tag(String tagid, String tagvalue) {
		setTagId(tagid);
		setTagValue(tagvalue);
	}
	
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	public String getTagId() {
		return this.tagId;
	}
	
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	
	public String getTagValue() {
		return this.tagValue;
	}
	
	public static String encode(Tag tag) {
		return tag.getTagId() + "=" + tag.getTagValue();
	}
	
	public static Tag decode(String tagString) {
		String[] tagArr = tagString.split("=");
		return new Tag(tagArr[0], tagArr[1]);
	}
	
	public static void main(String[] args) {
		Tag tag = new Tag("price", "12.25");
		String str = Tag.encode(tag);
		System.out.println(str);
		Tag t = Tag.decode(str);
		System.out.println(t.toString());
	}

}
