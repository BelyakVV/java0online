/*
 * Подпрограммы. №12.
 *
 *   Даны натуральные числа К и N. Написать метод (методы) формирования массива
 * А, элементами которого являются числа, сумма цифр которых равна К и которые
 * не больше N.
 *
 **
 * Максимально возможная сумма цифр числа типа long (8 999 999 999 999 999 999) = 170
 * Размер массива А по порядку величины не более 10 ^ (log10(N) - K/9) (а вот и нет!)
 */

import java.util.Scanner;


public class R12a {
    static final int MAX_K = 170; //Максимально возможная сумма цифр числа типа long
    static final int MAX_SIZE = 100000000; //Максимальный размер массива



    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int k = in.nextInt();
        if (k > MAX_K) System.exit(0); //Это не ошибка, но всё равно ничего не найдётся
        long n = in.nextLong();
        long[] a = fillArray(k, n);
        for (long ai: a) System.out.print(ai + " ");
        System.out.println();
    }

    /*
     * Собственно формирование массива
     */
    static long[] fillArray(int k, long n) {
        long[] result = new long[calcSize(k, n)];
        Digits digits = new Digits(k, n); //Представление в виде десятичных разрядов
        for (int i = 0; i < result.length; i++) result[i] = digits.nextLong();
        return result;
    }


    /*
     * Индусский код: перебираем все подходящие варианты и считаем их количество
     */
    static int calcSize(int k, long n) {
        long result = 0;
        Digits digits = new Digits(k, n);
        while ((0 != digits.nextLong()) && result <= MAX_SIZE) result++;
        if (result > MAX_SIZE) {
            System.out.println("Превышен лимит размера массива.");
            System.exit(1);
        }
        return (int)result;
    }
}


/*
 * Конструктор элементов для массива
 */
class Digits {  
    static final int LONG_DIGITS = 19; //Количество десятичных разрядов в типе long

    private int[] myDigits = new int[LONG_DIGITS]; //Число в виде десятичных разрядов
    private long myLong = 0; //Это же число
    private long myMax; //Допустимый максимум для него

    private int maxPos; //Старший значащий разряд


    public Digits(int k, long n) {
        //Минимально возможное число, сумма цифр = k
        long weight = 1; //Вес очередного разряда
        for (int i = 0; i < LONG_DIGITS; i++) {
            if (k > 9) {
                myDigits[i] = 9;
                k -= 9;
                myLong += weight * 9;
                weight = weight * 10;
            } else if (k > 0) {
                myDigits[i] = k;
                myLong += weight * k;
                maxPos = i;
                k = 0;
            } else {
                myDigits[i] = 0;
            }
        }
        myMax = n;
        if ((myLong > myMax) || (myLong < 1)) myLong = 0; //Индикатор неверного результата
    }


    /*
     * Следующее число (минимальное из оставшихся). Если вариантов больше нет, то 0
     */
    public long nextLong() {
        long result = myLong; //Отдаём предыдущее найденное значение
        int i = 0;
        while (i < LONG_DIGITS) //Пропускаем младшие незначащие разряды и ещё один
            if (0 != myDigits[i++]) break;
        while (i < LONG_DIGITS) { //Среди оставшихся ищем наимладший меньше 9
            if (myDigits[i] < 9) {
                myDigits[i]++; //Забираем в него единицу из младшего
                myDigits[i - 1]--;
                if (i > maxPos) maxPos = i; //Старший значащий разряд
                break;
            }
            i++;
        }
        /*
         * Теперь между разрядами, младшими только что увеличенного,
         * перегруппируем единицы так, чтобы десятичное представление было
         * наименьшим.
         */
        i--;
        for (int j = 0; j < i; j++) { //Быстрее, чем while
            int k = j + 1;
            while ((myDigits[j] < 9) && (k <= i)) {
                if (myDigits[k] > 0) {
                    int delta = Math.min(myDigits[k], 9 - myDigits[j]); //Это быстрее, чем if
                    myDigits[j] += delta;
                    myDigits[k] -= delta;
                }
                k++;
            }
        }
        myLong = 0; //Заново считаем
        long weight = 1;
        for (i = 0; i <= maxPos; i++) { //Быстрее, чем foreach
            myLong += weight * myDigits[i]; //С проверкой (myDigits[i] != 0) выходит медленнее
            weight = weight * 10;
        }
        if ((myLong < 1) || (myLong > myMax)) myLong = 0;
        return result;
    }
}