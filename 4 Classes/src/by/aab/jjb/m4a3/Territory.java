package by.aab.jjb.m4a3;

public abstract class Territory extends Unit {

	private Town capital;
	
	public Territory(String name, Town capital) {
		super(name);
		this.capital = capital;
	}

	public Town getCapital() {
		return capital;
	}
}
