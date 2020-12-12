/*
 * Подпрограммы. №11.
 *
 *   Написать метод(методы), определяющий, в каком из данных двух чисел больше
 * цифр.
 *
 **
 * Не указано, как обрабатывать ситуацию, когда количество цифр одинаково. В
 * таком случае вывести можно то число, модуль которого больше.
 */

import java.util.Scanner;


public class R11 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long a = in.nextLong();
        long b = in.nextLong();
        System.out.println(moreDigits(a, b));
    }


    /*
     * Нахождение большего по модулю числа
     */
    static long moreDigits(long a, long b) {
        return Math.abs(a) > Math.abs(b) ? a : b;
    }
}