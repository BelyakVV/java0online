/*
 * Подпрограммы. №6.
 *
 *   Написать метод(методы), проверяющий, являются ли данные три числа взаимно
 * простыми.
 */

import java.util.Scanner;


public class R6 {
    static Scanner in;


    public static void main(String[] args) {
        in = new Scanner(System.in);
        long a = in.nextLong();
        long b = in.nextLong();
        long c = in.nextLong();
        System.out.println(coPrimes(a, b, c) ? "Да" : "Нет");
    }


    /*
     * Являются ли a, b, c взаимно простыми?
     */
    static boolean coPrimes(long a, long b, long c) {
        return (binGCD(a, b) == 1)
               && (binGCD(b, c) == 1)
               && (binGCD(a, c) == 1);
    }


    /*
     * Бинарный алгоритм вычисления НОД
     */
    static long binGCD(long a, long b) {
        if (a == b) {
            return a;
        }
        int rol = 0; //Требуемый сдвиг результата
        while ((a > 1) && (b > 1) && ((a & 1) == 0) && ((b & 1) == 0)) {
            a = a >> 1;
            b = b >> 1;
            rol++;
        }
        while ((a > 0) && (b > 0)) {
            if (a == b) {
                return a << rol;
            }
            while ((a > 1) && ((a & 1) == 0)) {
                a = a >> 1;
            } 
            while ((b > 1) && ((b & 1) == 0)) {
                b = b >> 1;
            }
            if (b > a) {
                long t = a;
                a = b;
                b = t;
            }
            a = (a - b) >> 1;
        }
        if (a == 0) {
            return b << rol;
        } else
        {
            return a << rol;
        }
    }
}