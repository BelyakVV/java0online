package by.aab.jjb.m4a4.bean;

import java.io.Serializable;

public class Customer implements Serializable {
	
	private long id;
	
	private String name;
	
	public Customer() {
		name = "";
	}
	
	public Customer(long id, String name) {
		this.id = id;
		setName(name);
	}
	
	public Customer(Customer source) {
		this.id = source.id;
		this.name = source.name;
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

	public void updateFrom(Customer that) {
		this.id = that.id;
		this.name = that.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Customer)) return false;
		Customer that = (Customer) obj;
		return (this.id == that.id) && (this.name.equals(that.name));
	}
	
	private static final long serialVersionUID = 3192912292353002683L;
}
