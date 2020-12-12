/*
 * Подпрограммы. №14.
 *
 *   Натуральное число, в записи которого n цифр, называется числом Армстронга,
 * если сумма его цифр, возведенная в степень n, равна самому числу. Найти все
 * числа Армстронга от 1 до k. Для решения задачи использовать декомпозицию.
 **
 *   В условии ошибка: должно быть не "сумма цифр, возведенная в степень n", а
 * "сумма цифр, возведенных в степень n". Здесь производится поиск чисел
 * Армстронга. Поиск чисел из условия в решении R14a.
 *   Чисел Армстронга в диапазоне значений типа long всего 50, и все они
 * известны. Следовательно, наиболее оптимальное решение - поиск в таблице
 * (см. решение R14b).
 *   Здесь поиск оптимизированным перебором. Простой перебор в решении R14d.
 */

import java.util.Scanner;


public class R14c {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long k = in.nextLong();

        int maxN = (int)Math.log10(k) + 1; //Максимальное число разрядов
        int[] digits = new int[maxN]; //Представление числа в виде десятичных разрядов
        long[] weights = new long[maxN]; //Веса десятичных разрядов
        for (int i = 0; i < maxN; i++) weights[i] = pow(10, i);
        long[] bigPart = new long[maxN]; //Число из значащих разрядов старше текущего
        long[] bigSums = new long[maxN]; //Суммы степеней разрядов старше текущего
        long[] powers = {0, 1, 0, 0, 0, 0, 0, 0, 0, 0}; //Цифры от 0 до 9 в текущей степени

        for (int n = 1; n <= maxN; n++) { //Число разрядов
            fillPowers(powers, n); //Пересчитываем степени цифр для текущего n
            int i = n - 1; //Текущий разряд. Начинаем со старшего
            while (i < n) {
                long newBigPart;
                long newBigSums;
                if (digits[i] < 9) { //Пробуем увеличить текущий разряд на 1
                    newBigPart = bigPart[i] + (digits[i] + 1) * weights[i];
                    if (newBigPart > k) { //Достигли максимума
                        break;
                    } else {
                        newBigSums = bigSums[i] + powers[digits[i] + 1];
                    }
                } else { //Признак, что увеличить текущий разряд нельзя
                    newBigPart = 0;
                    newBigSums = 1;
                }
                if (newBigSums > newBigPart) { //Если нельзя увеличить
                    i++; //Переходим к старшему разряду
                } else { //Можно увеличить текущий разряд
                    digits[i]++; //Увеличиваем его
                    while (i > 0) { //И обнуляем все младшие разряды
                        i--;
                        digits[i] = 0;
                        bigPart[i] = newBigPart;
                        bigSums[i] = newBigSums;
                    }
                    if (newBigPart == newBigSums) { //Возможно, нашли очередное число Армстронга
                        System.out.print(newBigPart + " ");
                    }
                }
            }
        }
        System.out.println();
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
     * Цифры от 0 до 9 в степени n
     */
    static void fillPowers(long[] powers, int n) {
        for (int i = 2; i <= 9; i++) {
            powers[i] = pow(i, n);
        }
    }
}