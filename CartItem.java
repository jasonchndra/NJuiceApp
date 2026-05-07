package main;

public class CartItem {
	private String username;
	private String juiceName;
	private int juicePrice;
	private int qty;
	private String juiceId;

	public CartItem(String juiceId, String username, String juiceName, int juicePrice, int qty) {
		super();
		this.juiceId = juiceId;
		this.username = username;
		this.juiceName = juiceName;
		this.juicePrice = juicePrice;
		this.qty = qty;
	}

	public String getJuiceId() {
		return juiceId;
	}

	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	public String toString() {
		return getInfo();
	}

	public String getInfo() {
		return String.format("%dx - %s - [Rp. %d]", qty, juiceName, juicePrice);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJuiceName() {
		return juiceName;
	}

	public void setJuiceName(String juiceName) {
		this.juiceName = juiceName;
	}

	public int getJuicePrice() {
		return juicePrice;
	}

	public void setJuicePrice(int juicePrice) {
		this.juicePrice = juicePrice;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

}