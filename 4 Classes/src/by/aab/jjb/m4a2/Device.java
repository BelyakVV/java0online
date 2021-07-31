package by.aab.jjb.m4a2;

public abstract class Device {
	
	private final String model;	

	public Device(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}	
	
	public abstract void start();
}
