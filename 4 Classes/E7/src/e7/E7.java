package e7;

/**
 *   Описать класс, представляющий треугольник. Предусмотреть методы для 
 * создания объектов, вычисления площади, периметра и точки пересечения медиан.
 * Будем считать, что треугольник лежит в I четверти, сторона a лежит на оси 
 * абсцисс, сторона c выходит из начала координат.
 * @author aabyodj
 */
public class E7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double a = 3;
        double b = 4;
        double c = 5;
        System.out.println("Дан треугольник со сторонами " 
                + a + ", " + b + ", " + c);
        Triangle triangle = new Triangle(a, b, c);
        Triangle.Point centroid = triangle.centroid();
        System.out.println("Его площадь = " + triangle.area()
                + ", периметр = " + triangle.perimeter()
                + ", точка пересечения медиан C(" 
                + centroid.x + ", " + centroid.y + ")");
    }
    
}
