package by.aab.jjb.m4a4.bean;

import static by.aab.jjb.m4a4.util.StringUtil.isEqual;

import java.io.Serializable;

public class CustomerFilter implements Serializable {

	private Long id;
	
	private String namePart;
	private String nameFloor;
	private String nameCeiling;
	
	public CustomerFilter() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNamePart() {
		return namePart;
	}

	public void setNamePart(String namePart) {
		this.namePart = namePart;
	}

	public String getNameFloor() {
		return nameFloor;
	}

	public void setNameFloor(String nameFloor) {
		this.nameFloor = nameFloor;
	}

	public String getNameCeiling() {
		return nameCeiling;
	}

	public void setNameCeiling(String nameCeiling) {
		this.nameCeiling = nameCeiling;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof CustomerFilter)) return false;
		CustomerFilter that = (CustomerFilter) obj;
		return isEqual(this.id, that.id) 
				&& isEqual(this.namePart, that.namePart)
				&& isEqual(this.nameFloor, that.nameFloor)
				&& isEqual(this.nameCeiling, that.nameCeiling);
	}
	
	private static final long serialVersionUID = 5553134041400096156L;
}
