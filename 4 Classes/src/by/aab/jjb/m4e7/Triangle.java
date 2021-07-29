package by.aab.jjb.m4e7;

/**
 * Описать класс, представляющий треугольник. Предусмотреть методы для создания
 * объектов, вычисления площади, периметра и точки пересечения медиан.
 * 
 * @author aabyodj
 */
public class Triangle {
	private Point a;
	private Point b;
	private Point c;	
	
	public Triangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public static Triangle create(Point a, Point b, Point c) {
		return new Triangle(a, b, c);
	}
	
	//http://www.treugolniki.ru/ploshhad-treugolnika-po-koordinatam-vershin/	
	public double area() {
		return Math.abs((b.getX() - a.getX()) * (c.getY() - a.getY())
				- (c.getX() - a.getX()) * (b.getY() - a.getY())) 
				/ 2;
	}
	
	public double perimeter() {
		return a.distanceTo(b) + b.distanceTo(c) + c.distanceTo(a);
	}
	
	//http://www.treugolniki.ru/tochka-peresecheniya-median/
	public Point medianIntersection() {
//		double medianX = (a.getX() + b.getX()) / 2;
//		double resultX = (medianX * 2 + c.getX()) / 3;
		double resultX = (a.getX() + b.getX() + c.getX()) / 3;
		
//		double medianY = (a.getY() + b.getY()) / 2;
//		double resultY = (medianY * 2 + c.getY()) / 3;
		double resultY = (a.getY() + b.getY() + c.getY()) / 3;
		
		return new Point(resultX, resultY);		
	}
}
