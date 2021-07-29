package by.aab.jjb.m4e5;

/**
 * Опишите класс, реализующий десятичный счетчик, который может увеличивать или
 * уменьшать свое значение на единицу в заданном диапазоне. Предусмотрите
 * инициализацию счетчика значениями по умолчанию и произвольными значениями.
 * Счетчик имеет методы увеличения и уменьшения состояния, и метод позволяющее
 * получить его текущее состояние. Написать код, демонстрирующий все возможности
 * класса.
 * 
 * @author aabyodj
 *
 */
public class DecimalCounter {

	private Decimal value = new Decimal(0);
	private Decimal ceiling = new Decimal(Decimal.MAX_INT);
	private Decimal floor = new Decimal(Decimal.MIN_INT);
	
	public DecimalCounter() {		
	}
	
	public DecimalCounter(int value) {
		setValue(value);
	}
	
	public DecimalCounter(int value, int floor, int ceiling) {
		if (floor > ceiling) {
			int t = floor;
			floor = ceiling;
			ceiling = t;
		}
		if (value < floor) {
			value = floor;
		} else if (value > ceiling) {
			value = ceiling;
		}
		this.value.set(value);
		this.ceiling.set(ceiling);
		this.floor.set(floor);
	}

	public Decimal getCeiling() {
		return ceiling;
	}
	
	public void setCeiling(int ceiling) {
		this.ceiling.set(ceiling);
		if (this.ceiling.compareTo(floor) < 0) floor.set(this.ceiling);
		meetBoundaries();
	}
	
	public Decimal getFloor() {
		return floor;
	}
	
	public void setFloor(int floor) {
		this.floor.set(floor);
		if (this.floor.compareTo(ceiling) > 0) ceiling.set(this.ceiling);
		meetBoundaries();
	}
	
	public boolean inc() {
		if (value.compareTo(ceiling) >= 0) return false;
		value.inc();
		return true;
	}
	
	public boolean dec() {
		if (value.compareTo(floor) <= 0) return false;
		value.dec();
		return true;
	}
	
	public void setValue(int value) {
		this.value.set(value);
		adjustBoundaries();
	}
	
	public int toInt() {
		return value.toInt();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Текущее значение: ").append(value)
				.append(", нижняя граница: ").append(floor)
				.append(", верхняя граница: ").append(ceiling).toString();
	}

	private void adjustBoundaries() {
		if (value.compareTo(floor) < 0) floor.set(value);
		if (value.compareTo(ceiling) > 0) ceiling.set(value);
	}
	
	private void meetBoundaries() {
		if (value.compareTo(floor) < 0) value.set(floor);
		if (value.compareTo(ceiling) > 0) value.set(ceiling);
	}
}
