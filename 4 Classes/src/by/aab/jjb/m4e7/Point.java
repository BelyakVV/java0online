package by.aab.jjb.m4e7;

public class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double distanceTo(Point that) {
		double deltaX = this.x - that.x;
		double deltaY = this.y - that.y;
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
}
