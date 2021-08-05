package by.aab.jjb.m4a4.bean;

import java.io.Serializable;

public class Account implements Serializable {
	
	private String iban;
	private long ownerId;
	private boolean blocked;
	private long balance;	
	
	public Account() {
		iban = "";
	}
	
	public Account(Account source) {
		this.iban = source.iban;
		this.ownerId = source.ownerId;
		this.blocked = source.blocked;
		this.balance = source.balance;
	}
	
	public Account(String iban, long ownerId, boolean blocked, long balance) {
		setIban(iban);
		this.ownerId = ownerId;
		this.blocked = blocked;
		this.balance = balance;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban.strip().toUpperCase();
	}

	public long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	public long getBalance() {
		return balance;
	}
	
	public void setBalance(long balance) {
		this.balance = balance;
	}

	public void updateFrom(Account that) {
		this.iban = that.iban;
		this.ownerId = that.ownerId;
		this.blocked = that.blocked;
		this.balance = that.balance;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Account)) return false;
		Account that = (Account) obj;
		return this.iban.equals(that.iban)
				&& this.ownerId == that.ownerId
				&& this.blocked == that.blocked
				&& this.balance == that.balance;
	}
	
	private static final long serialVersionUID = -4926982468208250253L;
}
