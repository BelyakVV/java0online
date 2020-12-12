/*
 * Подпрограммы. №5.
 *
 *   Составить программу, которая в массиве A[N] находит второе по величине
 * число (вывести на печать число, которое меньше максимального элемента
 * массива, но больше всех других элементов).
 */

import java.util.Scanner;


public class R5 {
    static Scanner in;


    public static void main(String[] args) {
        in = new Scanner(System.in);
        int n = in.nextInt();
        double[] a = new double[n];
        double max = Double.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            double ai = in.nextDouble();
            a[i] = ai;
            if (ai > max) max = ai;
        }
        System.out.println(subMax(a, max));
    }


    /*
     * Нахождение максимального числа, меньшего, чем max
     */
    static double subMax(double[] a, double max) {
        double result = Double.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            double ai = a[i];
            if (ai > result && ai < max) result = ai;
        }
        if (result == Double.MIN_VALUE) {
            System.out.println("Не найдено.");
            System.exit(1);
        }
        return result;
    }
}