package ch.dietrich.entity;

public class Club {

	private int id;
	private String name = "";
	private String address = "";
	private String city = "";
	private String zip = "";
	private String country = "";
	private boolean temproary = false;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean isTemproary() {
		return temproary;
	}
	public void setTemproary(boolean temproary) {
		this.temproary = temproary;
	}
	
	
}
