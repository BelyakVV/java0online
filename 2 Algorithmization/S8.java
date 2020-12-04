/*
 * Сортировки. №8.
 *
 * Даны дроби P1/Q1, P2/Q2, ..., Pn/Qn ( Pi, Qi - натуральные). Составить
 * программу, которая приводит эти дроби к общему знаменателю и упорядочивает их
 * в порядке возрастания.
 */

import java.util.Scanner;


public class S8 {
    static Scanner in;


    public static void main(String[] args) {
        in = new Scanner(System.in);
        int n = in.nextInt();
        long[] p = new long[n];
        long[] q = new long[n];
        long cd = 1; //Общий знаменатель
        for (int i = 0; i < n; i++) { //Ввод
            p[i] = in.nextLong();
            long qi = in.nextLong();
            q[i] = qi;
            cd = lcm(qi, cd);
        }
        for (int i = 0; i < n; i++) { //Коррекция числителей для общего знаменателя
            p[i] = p[i] * (cd / q[i]);
        }
        shell(p); //Сортировка числителей методом Шелла
        for (int i = 0; i < n; i++) { //Вывод
            System.out.print(p[i] + "/" + cd + " ");
        }
        System.out.println();
    }


    /*
     * НОК
     */
    static long lcm(long a, long b) {
        return a * b / binGCD(a, b);
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


    /*
     * Сортировка Шелла
     */
    static void shell(long[] a) {
        int delta = (a.length / 3) | 1; //delta >= 1
        while (delta > 0) {
            for (int start = 0; start < delta; start++) {
                for (int i = start + delta; i < a.length; i += delta) {
                    long ai = a[i];
                    int j = i - delta;
                    while (j >= start) {
                        long aj = a[j];
                        if (aj <= ai) break;
                        a[j + delta] = aj;
                        j -= delta;
                    }
                    a[j + delta] = ai;
                }
            }
            delta = (delta >> 1);
        }
    }
}