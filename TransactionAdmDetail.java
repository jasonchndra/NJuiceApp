package main;

public class TransactionAdmDetail {
	private String transactionId;
	private String juiceId;
	private String juiceName;
	private int quantity;

	public TransactionAdmDetail(String transactionId, String juiceId, String juiceName, int quantity) {
		this.transactionId = transactionId;
		this.juiceId = juiceId;
		this.juiceName = juiceName;
		this.quantity = quantity;
	}

	public String getJuiceName() {
		return juiceName;
	}

	public void setJuiceName(String juiceName) {
		this.juiceName = juiceName;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getJuiceId() {
		return juiceId;
	}

	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
