package by.aab.jjb.m4a3;

public class District extends Territory {
	
	private double area;
	private int population;

	public District(String name, Town capital, double area, int population) {
		super(name, capital);
		this.area = area;
		this.population = population;
	}

	@Override
	public double getArea() {
		return area + getCapital().getArea();
	}

	@Override
	public int getPopulaion() {
		return population + getCapital().getPopulaion();
	}

}
