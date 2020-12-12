/*
 * Подпрограммы. №13.
 *
 *   Два простых числа называются «близнецами», если они отличаются друг от
 * друга на 2 (например, 41 и 43). Найти и напечатать все пары «близнецов» из
 * отрезка [n,2n], где n - заданное натуральное число больше 2. Для решения
 * задачи использовать декомпозицию.
 */

import java.util.Scanner;


public class R13 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int a = n;
        int b = n * 2;
        if ((a & 1) == 0) a++;
        int prev = 0;
        boolean prevIsPrime = false;
        for (int i = a; i <= b; i += 2) {
            boolean thisIsPrime = isPrime(i);
            if (thisIsPrime && prevIsPrime) {
                System.out.println(prev + " " + i);
            }
            prev = i;
            prevIsPrime = thisIsPrime;
        }
    }


    /*
     * Определение простого числа
     */
    static boolean isPrime(int n) {
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if ((n % i) == 0) return false;
        }
        return true;
    }
}