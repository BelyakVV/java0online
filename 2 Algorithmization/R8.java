/*
 * Подпрограммы. №8.
 *
 *   Задан массив D. Определить следующие суммы: D[l] + D[2] + D[3];
 * D[3] + D[4] + D[5]; D[4] + D[5] + D[6].
 * Пояснение. Составить метод(методы) для вычисления суммы трех последовательно
 * расположенных элементов массива с номерами от k до m.
 */

import java.util.Scanner;


public class R8 {
    static Scanner in;


    public static void main(String[] args) {
        in = new Scanner(System.in);
        int n = in.nextInt();
        double[] d = new double[n];
        for (int i = 0; i < n; i++) d[i] = in.nextDouble();
        System.out.println(sum(d, 1, 3) + " " + sum(d, 3, 5) + " " + sum(d, 4, 6));
    }


    /*
     * Сумма элементов от k до m
     **
     * Отсчёт k и m от 1, а не от 0
     */
    static double sum(double[] a, int k, int m) {
        double result = 0;
        if (--k >= a.length) k = a.length - 1; 
        if (--m >= a.length) m = a.length - 1;
        for (int i = k; i <= m; i++) {
            result += a[i];
        }
        return result;
    }
}