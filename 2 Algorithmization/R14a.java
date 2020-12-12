/*
 * Подпрограммы. №14.
 *
 *   Натуральное число, в записи которого n цифр, называется числом Армстронга,
 * если сумма его цифр, возведенная в степень n, равна самому числу. Найти все
 * числа Армстронга от 1 до k. Для решения задачи использовать декомпозицию.
 **
 *   В условии ошибка: должно быть не "сумма цифр, возведенная в степень n", а
 * "сумма цифр, возведенных в степень n".
 *   Здесь производится поиск чисел, указанных в условии. Поиск чисел Армстронга
 * в решениях R14b, R14c и R14d.
 */

import java.util.Scanner;


public class R14a {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long k = in.nextLong();
        long minX = 1; //Минимальное n-значное число
        for (int n = 1; n <= Math.log10(k) + 1; n++) {
            long maxX = Math.min(k, minX * 10 - 1); //Максимальное n-значное число не более k
            if (maxX < 0) maxX = k;
            double n1 = 1 / (double)n;
            int minSum = (int)Math.pow(minX, n1); //Нижний предел на сумму цифр
            int maxSum = (int)Math.pow(maxX, n1) + 1; //Верхний предел на сумму цифр
            for (int sum = minSum; sum <= maxSum; sum++) {
                long x = pow(sum, n);
                if (calcSum(x) == sum) System.out.print(x + " ");
            }
            minX = maxX + 1;
        }
        System.out.println();
    }


    /*
     * Целочисленное возведение в степень
     */  
    static long pow(int x, int n) {
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= x;
        }
        return result;
    }


    /*
     * Подсчёт суммы цифр
     */
    static int calcSum(long n) {
        int result = 0;
        while (n > 0) {
            result += (int)(n % 10);
            n = n / 10;
        }
        return result;
    }
}