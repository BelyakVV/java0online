/*
 * Подпрограммы. №2.
 *
 *   Написать метод(методы) для нахождения наибольшего общего делителя четырех
 * натуральных чисел.
 */

import java.util.Scanner;


public class R2 {
    static Scanner in;


    public static void main(String[] args) {
        in = new Scanner(System.in);
        long a = in.nextLong();
        long b = in.nextLong();       
        long c = in.nextLong();
        long d = in.nextLong();
        System.out.println(gcd(a, b, c, d));
    }


    /*
     * НОД четырёх чисел
     */
    static long gcd(long a, long b, long c, long d) {
        return binGCD(a, binGCD(b, binGCD(c, d)));
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