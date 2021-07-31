package by.aab.jjb.m4a3;

public class Region extends Territory {

	private District[] districts;
	
	public Region(String name, Town capital, District[] districts) {
		super(name, capital);
		this.districts = districts;
	}

	@Override
	public double getArea() {
		double result = getCapital().getArea();
		for (var district: districts) {
			result += district.getArea();
		}
		return result;
	}
	
	@Override
	public int getPopulaion() {
		int result = getCapital().getPopulaion();
		for (var district: districts) {
			result += district.getPopulaion();
		}
		return result;
	}
}
