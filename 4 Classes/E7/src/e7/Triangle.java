package e7;

/**
 *   Описать класс, представляющий треугольник. Предусмотреть методы для 
 * создания объектов, вычисления площади, периметра и точки пересечения медиан.
 * Будем считать, что треугольник лежит в I четверти, сторона a лежит на оси 
 * абсцисс, сторона c выходит из начала координат.
 * @author aabyodj
 */
public class Triangle {

    private final double a;
    private final double b;
    private final double c;

    Triangle(double a, double b, double c) {        
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    /**
     * Вычисление площади треугольника по формуле Герона
     * @return Площадь треугольника
     */
    public double area() {        
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));    
    }
    
    /** 
     * Вычисление декартовых координат точки пересечения медиан для случая, 
     * когда треугольник лежит в I четверти, сторона a лежит на оси абсцисс, 
     * сторона c выходит из начала координат.
     * @return Точка пересечения медиан
     */
    public Point centroid() {
//        //Координата x вершины, противолежащей стороне a
//        double bcX = a * c / (b + c);
//        double resultX = a / 2 - (a / 2 - bcX) / 3;
        double resultX = a * (1 + c / (b + c)) / 3;
//        //Высота, опущенная на сторону a
//        double aH = 2 * area() / a;
//        double resultY = aH / 3;
        double resultY = 2 * area() / (a * 3);
        return new Point(resultX, resultY);
    }
   
    /**
     * Вычисление периметра треугольника
     * @return Периметр треугольника
     */
    public double perimeter() {
        return a + b + c;
    }

    /**
     * Точка в декартовой системе координат
     */
    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
