package main;

//Buat data tableView

public class Transaction {
	private String transactionID, paymentType, username, juiceID, juiceName;
	private int quantity, juicePrice;
	
	public Transaction(String transactionID, String paymentType, String username, String juiceID, String juiceName,
			int quantity, int juicePrice) {
		super();
		this.transactionID = transactionID;
		this.paymentType = paymentType;
		this.username = username;
		this.juiceID = juiceID;
		this.juiceName = juiceName;
		this.quantity = quantity;
		this.setJuicePrice(juicePrice);
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJuiceID() {
		return juiceID;
	}

	public void setJuiceID(String juiceID) {
		this.juiceID = juiceID;
	}

	public String getJuiceName() {
		return juiceName;
	}

	public void setJuiceName(String juiceName) {
		this.juiceName = juiceName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getJuicePrice() {
		return juicePrice;
	}

	public void setJuicePrice(int juicePrice) {
		this.juicePrice = juicePrice;
	}
}