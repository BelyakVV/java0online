/*
 * Подпрограммы. №17.
 *
 *   Из заданного числа вычли сумму его цифр. Из результата вновь вычли сумму
 * его цифр и т.д. Сколько таких действий надо произвести, чтобы получился нуль?
 * Для решения задачи использовать декомпозицию.
 **
 *
 *   Ввод: число a
 *   Вывод: искомое количество действий
 */

import java.util.Scanner;


public class R17 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long a = in.nextLong();
        int c = 0;
        while (a > 0) {
            a -= digSum(a);
            c++;
        }
        System.out.println(c);
    }


    /*
     * Сумма цифр
     */
    static long digSum(long a) {
        long result = 0;
        while (a > 0) {
            result += a % 10;
            a = a / 10;
        }
        return result;
    }
}