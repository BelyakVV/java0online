/*
 * Подпрограммы. №15.
 *
 *   Найти все натуральные n-значные числа, цифры в которых образуют строго
 * возрастающую последовательность (например, 1234, 5789). Для решения задачи
 * использовать декомпозицию.
 */

import java.util.Scanner;


public class R15{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (n > 9) System.exit(0); //Не существует таких чисел
        int[] digits = new int[n]; //Число в виде десятичных разрядов
        int i = n - 1; //Начинаем со старшего значащего разряда
        while (i < n) {
            if (digits[i] < (9 - i)) { //Пробуем увеличить текущий разряд
                digits[i]++;
                while (i > 0) { //Если получилось - заполняем младшие разряды
                    digits[i - 1] = digits[i] + 1; //По возрастанию
                    i--;
                }
                printNumber(digits); //Вывод очередного числа
            } else { //Текущий разряд нельзя увеличить
                i++; //Переходим к старшему разряду
            }
        }
        System.out.println();
    }


    /*
     * Печать числа, заданного в виде массива цифр
     */
    static void printNumber(int[] a) {
        for (int i = a.length - 1; i >= 0; i--) {
            System.out.print(a[i]);
        }
        System.out.print(" ");
    }
}