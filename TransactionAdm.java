package main;

public class TransactionAdm {
	private String transactionId;
	private String paymentType;
	private String username;

	public TransactionAdm(String transactionId, String paymentType, String username) {
		this.transactionId = transactionId;
		this.paymentType = paymentType;
		this.username = username;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

}
