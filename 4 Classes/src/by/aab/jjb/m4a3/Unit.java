package by.aab.jjb.m4a3;

public abstract class Unit {
	
	private String name;

	public Unit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract double getArea();
	
	public abstract int getPopulaion();
}
