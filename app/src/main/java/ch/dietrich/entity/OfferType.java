package ch.dietrich.entity;

public class OfferType {

	private Integer id = null;
	private String name = "";
	private boolean hasPrice;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHasPrice() {
		return hasPrice;
	}
	public void setHasPrice(boolean hasPrice) {
		this.hasPrice = hasPrice;
	}
	
	
}
