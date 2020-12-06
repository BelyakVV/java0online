/*
 * Подпрограммы. №9.
 *
 *   Даны числа X, Y, Z, Т — длины сторон четырехугольника. Написать метод
 * (методы) вычисления его площади, если угол между сторонами длиной X и Y —
 * прямой.
 */

import java.util.Scanner;


public class R9 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        double x = in.nextDouble();    
        double y = in.nextDouble();
        double z = in.nextDouble();
        double t = in.nextDouble();
        System.out.println(area(x, y, z, t));
    }


    /*
     * Площадь четырёхугольника со сторонами a, b, c, d.
     * Угол между a и b - прямой.
     */
    static double area(double a, double b, double c, double d) {
        double dia = Math.sqrt(a * a + b * b); //Диагональ напротив прямого угла
        return a * b / 2 + hero(c, d, dia);
    }


    /*
     * Площадь треугольника по формуле Герона
     */
    static double hero(double a, double b, double c) {
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }
}