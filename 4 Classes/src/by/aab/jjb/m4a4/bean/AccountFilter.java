package by.aab.jjb.m4a4.bean;

import static by.aab.jjb.m4a4.util.StringUtil.isEqual;

import java.io.Serializable;

public class AccountFilter implements Serializable {
	
	String ibanPart;
	String ibanFloor;
	String ibanCeiling;
	
	Long ownerId;
	
	Boolean blocked;
	
	Long balanceFloor;
	Long balanceCeiling;
	
	public AccountFilter() {
	}

	public String getIbanPart() {
		return ibanPart;
	}

	public void setIbanPart(String ibanPart) {
		this.ibanPart = ibanPart;
	}

	public String getIbanFloor() {
		return ibanFloor;
	}

	public void setIbanFloor(String ibanFloor) {
		this.ibanFloor = ibanFloor;
	}

	public String getIbanCeiling() {
		return ibanCeiling;
	}

	public void setIbanCeiling(String ibanCeiling) {
		this.ibanCeiling = ibanCeiling;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwner(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	public Long getBalanceFloor() {
		return balanceFloor;
	}

	public void setBalanceFloor(Long balanceFloor) {
		this.balanceFloor = balanceFloor;
	}

	public Long getBalanceCeiling() {
		return balanceCeiling;
	}

	public void setBalanceCeiling(Long balanceCeiling) {
		this.balanceCeiling = balanceCeiling;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof AccountFilter)) return false;
		AccountFilter that = (AccountFilter) obj;
		return isEqual(this.ibanPart, that.ibanPart) 
				&& isEqual(this.ibanFloor, that.ibanFloor)
				&& isEqual(this.ibanCeiling, that.ibanCeiling) 
				&& isEqual(this.ownerId, that.ownerId)
				&& isEqual(this.blocked, that.blocked) 
				&& isEqual(this.balanceFloor, that.balanceFloor)
				&& isEqual(this.balanceCeiling, that.balanceCeiling);
	}
	
	private static final long serialVersionUID = 5040002612235188590L;
}
