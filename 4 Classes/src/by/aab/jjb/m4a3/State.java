package by.aab.jjb.m4a3;

/**
 * Создать объект класса Государство, используя классы Область, Район, Город.
 * Методы: вывести на консоль столицу, количество областей, площадь, областные
 * центры.
 * 
 * @author aabyodj
 */
public class State extends Territory {

	private Region[] regions;
	
	public State(String name, Town capital, Region[] regions) {
		super(name, capital);
		this.regions = regions;
	}
	
	public void printCapital() {
		System.out.println("Столица: " + getCapital().getName());
	}
	
	public void printRegionsCount() {
		System.out.println("Количество областей: " + regions.length);
	}
	
	public void printArea() {
		System.out.println("Площадь: " + getArea());
	}
	
	public void printRegionCapitals() {
		System.out.println("Областные центры:");
		for (var region: regions) {
			System.out.println(region.getCapital().getName());
		}
	}
	
	@Override
	public double getArea() {
		double result = getCapital().getArea();
		for (var region: regions) {
			result += region.getArea();
		}
		return result;
	}
	
	@Override
	public int getPopulaion() {
		int result = getCapital().getPopulaion();
		for (var region: regions) {
			result += region.getPopulaion();
		}
		return result;
	}
}
