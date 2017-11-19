package top.zuishare.util;

public enum ResourceType {
	METHOD("METHOD"),URL("URL");
	private String type;
	
	private ResourceType(String type){
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
