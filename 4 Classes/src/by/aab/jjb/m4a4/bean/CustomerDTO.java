package by.aab.jjb.m4a4.bean;

import java.io.Serializable;

public class CustomerDTO implements Serializable {
	
	private long id;
	private String name;
	
	public CustomerDTO() {
		name = "";
	}
	
	public CustomerDTO(long id, String name) {
		this.id = id;
		setName(name);
	}

	public CustomerDTO(Customer source) {
		this.id = source.getId();
		this.name = source.getName();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		assert name != null;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof CustomerDTO)) return false;
		CustomerDTO that = (CustomerDTO) obj;
		return (this.id == that.id) && (this.name.equals(that.name));
	}
	
	private static final long serialVersionUID = 3722398665258748681L;
}
