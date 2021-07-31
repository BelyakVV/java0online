package by.aab.jjb.m4a2;

/**
 * Создать объект класса Автомобиль, используя классы Колесо, Двигатель. Методы:
 * ехать, заправляться, менять колесо, вывести на консоль марку автомобиля.
 * 
 * @author aabyodj
 */
public class Car extends Device {

	private Wheel[] wheels;
	private Engine engine;
	
	public Car(String model, Wheel[] wheels, Engine engine) {
		super(model);
		this.wheels = wheels;
		this.engine = engine;
	}

	public Car(String model) {
		super(model);
		
		String wheelModel = model + " wheel";
		wheels = new Wheel[4];
		for (int i = 0; i < wheels.length; i++) {
			wheels[i] = new Wheel(wheelModel);
		}
		
		engine = new Engine(model + " engine");
	}
	
	@Override
	public void start() {
		engine.start();
		for (var wheel: wheels) {
			wheel.start();
		}
	}
	
	public void refuel() {
		//Some job here
	}
	
	public void setWheel (Wheel wheel, int position) {
		wheels[position] = wheel;
	}
	
	public void printModel() {
		System.out.println(getModel());
	}
}
