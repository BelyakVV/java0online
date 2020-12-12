/*
 * Подпрограммы. №16.
 *
 *   Написать программу, определяющую сумму n - значных чисел, содержащих только
 * нечетные цифры. Определить также, сколько четных цифр в найденной сумме. Для
 * решения задачи использовать декомпозицию.
 **
 *   Ввод: число n
 *   Вывод: через пробел найденная сумма и количество чётных цифр в ней.
 */

import java.util.Scanner;


public class R16 {
    static final int DIG_SUM = 1 + 3 + 5 + 7 + 9; //Нечётные цифры
    static final int DIG_COUNT = 5; //Количество слагаемых DIG_SUM


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (Math.pow(DIG_COUNT, n - 1) > Long.MAX_VALUE / DIG_SUM) {
            System.out.println("Слишком большое n");
            System.exit(1);
        }                                                                                        
        long onePos = DIG_SUM * pow(DIG_COUNT, n - 1); //Первоначальное значение каждого разряда
        long[] digits = new long[n + (int)Math.log10(onePos)]; //Искомая сумма в виде десятичных разрядов
        for (int i = 0; i < n; i++) { //Первоначальное заполнение
            digits[i] = onePos;
        }
        int evens = 0; //Количество чётных цифр в найденной сумме
        for (int i = 0; i < digits.length - 1; i++) { //Нормализация результата
            digits[i + 1] += digits[i] / 10;
            digits[i] = digits[i] % 10;
            if ((digits[i] & 1) == 0) evens++; //Подсчёт чётных цифр
        }
        if ((digits[digits.length - 1] & 1) == 0) evens++; //Учёт старшего разряда
        printNumber(digits);
        System.out.println(" " + evens);
    }


    /*
     * Целочисленное возведение в степень
     */
    static long pow(int a, int n) {
        long result = 1;
        for (int i = 1; i <= n; i++) result *= a;
        return result;
    }


    /*
     * Печать числа, заданного в виде массива цифр
     */
    static void printNumber(long[] a) {
        for (int i = a.length - 1; i >= 0; i--) {
            System.out.print(a[i]);
        }
    }
}