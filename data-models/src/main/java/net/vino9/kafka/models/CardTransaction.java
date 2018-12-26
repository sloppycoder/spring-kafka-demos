package net.vino9.kafka.models;

@SuppressWarnings("ALL")
public class CardTransaction {
	String id;
	String cardNumber;
	String currency;
	double amount;
	String calloutStatus;
	Merchant merchant;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCalloutStatus() {
		return calloutStatus;
	}

	public void setCalloutStatus(String calloutStatus) {
		this.calloutStatus = calloutStatus;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
}
