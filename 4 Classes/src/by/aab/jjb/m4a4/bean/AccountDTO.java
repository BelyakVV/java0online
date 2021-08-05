package by.aab.jjb.m4a4.bean;

import java.io.Serializable;

import by.aab.jjb.m4a4.service.CustomersCache;
import by.aab.jjb.m4a4.service.OrphanAccountException;

public class AccountDTO implements Serializable {
	private String iban;
	private CustomerDTO owner;
	private boolean blocked;
	private long balance;	

	public AccountDTO() {
		iban = "";
		owner = new CustomerDTO();
	}

	public AccountDTO(Account source, CustomerDTO owner) {
		assert owner.getId() == source.getOwnerId();
		this.iban = source.getIban();
		this.owner = owner;
		this.blocked = source.isBlocked();
		this.balance = source.getBalance();
	}
	
	public AccountDTO(Account source, CustomersCache cache) throws OrphanAccountException {
		CustomerDTO owner = cache.get(source.getOwnerId());
		if (null == owner) {
			throw new OrphanAccountException("Счёт " + source.getIban() + " не имеет владельца");
		}
		this.iban = source.getIban();
		this.owner = owner;
		this.blocked = source.isBlocked();
		this.balance = source.getBalance();
	}
	
	public AccountDTO(String iban, CustomerDTO owner, boolean blocked, long balance) {
		setIban(iban);
		setOwner(owner);
		this.blocked = blocked;
		this.balance = balance;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban.strip().toUpperCase();
	}

	public CustomerDTO getOwner() {
		return owner;
	}

	public void setOwner(CustomerDTO owner) {
		assert owner != null;
		this.owner = owner;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof AccountDTO)) return false;
		AccountDTO that = (AccountDTO) obj;
		return this.iban.equals(that.iban) 
				&& this.owner.equals(that.owner) 
				&& this.blocked == that.blocked
				&& this.balance == that.balance;
	}

	private static final long serialVersionUID = 9074606216454368380L;
}
