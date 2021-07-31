package by.aab.jjb.m4a3;

public class Town extends Unit {

	private double area;
	private int population;
	
	public Town(String name, double area, int population) {
		super(name);
		this.area = area;
		this.population = population;
	}

	@Override
	public double getArea() {
		return area;
	}

	@Override
	public int getPopulaion() {
		return population;
	}
}
