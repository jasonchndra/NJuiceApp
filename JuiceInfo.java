package main;

public class JuiceInfo {
	private String juiceId;
	private String juiceName;
	private int Price;
	private String juiceDescription;

	public JuiceInfo(String juiceId, String juiceName, int Price, String juiceDescription) {
		this.juiceId = juiceId;
		this.juiceName = juiceName;
		this.Price = Price;
		this.juiceDescription = juiceDescription;
	}

	public String getJuiceId() {
		return juiceId;
	}

	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	public String getJuiceName() {
		return juiceName;
	}

	public void setJuiceName(String juiceName) {
		this.juiceName = juiceName;
	}

	public int getPrice() {
		return Price;
	}

	public void setPrice(int price) {
		Price = price;
	}

	public String getJuiceDescription() {
		return juiceDescription;
	}

	public void setJuiceDescription(String juiceDescription) {
		this.juiceDescription = juiceDescription;
	}

}
