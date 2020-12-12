/*
 * Подпрограммы. №3.
 *
 *   Вычислить площадь правильного шестиугольника со стороной а, используя метод
 * вычисления площади треугольника.
 */

import java.util.Scanner;


public class R3 {
    static Scanner in;


    public static void main(String[] args) {
        in = new Scanner(System.in);
        double a = in.nextDouble();
        System.out.println(triArea(a) * 6);
    }


    /*
     * Площадь правильного треугольника
     */
    static double triArea(double a) {
        return a * a * Math.sin(Math.toRadians(60)) / 2;
    }
}