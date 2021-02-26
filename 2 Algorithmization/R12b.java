import java.util.Scanner;

/**
 *   Даны натуральные числа К и N. Написать метод(методы) формирования массива
 * А, элементами которого являются числа, сумма цифр которых равна К и которые
 * не большее N.
 **
 *   Решение простым перебором. Для расчёта размера массива используется метод
 * из решения R12a.
 */
public class R12b {

    /** Максимально возможное k для n типа int (1 999 999 999) = 82 */
    static final byte MAX_K = 82;

    /**
     * @System.in  Числа K и N
     * @System.out Элементы массива A (если они есть)
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        byte k = in.nextByte();
        if (k > MAX_K) System.exit(0);      //Всё равно ничего не найдётся
        int n = in.nextInt();
        int[] a = fillArray(k, n);
        for (int ai: a) {
            System.out.print(ai + " ");
        }
        System.out.println();
    }

    /**
     * Формирование массива
     * @param k
     * @param n
     * @return Массив А
     */
    static int[] fillArray(byte k, int n) {
        int[] result = new int[calcSize(k, n)];
        int i = 0;
        int minX = (int) Math.pow(9, (double) k / 9);
        for (int x = minX; x <= n; x++) {
            if (digitSum(x) == k) result[i++] = x;
        }
        return result;
    }

    static byte digitSum(int n) {
        byte result = 0;
        while (n > 0) {
            result += (byte)(n % 10);
            n = n / 10;
        }
        return result;
    }

    /**
     * Индусский код: быстро перебираем все подходящие варианты и считаем их
     * количество.
     * @param k
     * @param n
     * @return Размер массива А
     */
    static int calcSize(int k, long n) {
        long result = 0;
        Digits digits = new Digits(k, n);     //Быстрый поиск элементов массива
        while ((0 != digits.nextLong()) && result <= Integer.MAX_VALUE) {
            result++;
        }
        if (result > Integer.MAX_VALUE) {
            System.err.println("Превышен лимит размера массива.");
            System.exit(1);
        }
        return (int) result;
    }
}


/**
 * Конструктор элементов для массива A
 */
class Digits {

    /** Количество десятичных разрядов в типе long */
    static final int LONG_DIGITS = 19; 

    /** Искомое число */
    private long myLong = 0;

    /** Искомое число в виде десятичных разрядов */
    private int[] myDigits = new int[LONG_DIGITS];

    /** Допустимый максимум для искомого числа */
    private long myMax;

    /** Позиция старшего значащего разряда */
    private int maxPos;

    /**
     * Инициализировать объект минимально возможным числом, не большим n,
     * у которого сумма цифр = k
     * @param k
     * @param n
     */
    public Digits(int k, long n) {

        //Вес очередного разряда
        long weight = 1;

        //Перебираем все разряды, начиная с младшего
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
        if ((myLong > myMax) || (myLong < 1)) {
            myLong = 0;      //Индикатор неверного результата
        }
    }


    /**
     * Получить следующее число (минимальное из оставшихся). Если вариантов
     * больше нет, то 0.
     */
    public long nextLong() {

        //Отдаём предыдущее найденное значение
        long result = myLong;

        //Перебираем все разряды, начиная с младшего
        int i = 0;

        //Пропускаем младшие незначащие разряды и ещё один
        while (i < LONG_DIGITS) {
            if (0 != myDigits[i++]) break;
        }

        //Среди оставшихся разрядов ищем наимладший меньше 9
        while (i < LONG_DIGITS) {
            if (myDigits[i] < 9) {
                myDigits[i]++;   //Забираем в него единицу из младшего
                myDigits[i - 1]--;

                //Обновляем позицию старшего значащего разряда
                if (i > maxPos) {
                    maxPos = i;
                }
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
        for (int j = 0; j < i; j++) {  //Быстрее, чем while
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

        //Переводим найденное число в long
        myLong = 0;
        long weight = 1;
        for (i = 0; i <= maxPos; i++) {               //Быстрее, чем foreach
            myLong += weight * myDigits[i];           //С проверкой (myDigits[i] != 0) выходит медленнее
            weight = weight * 10;
        }

        //Возможно, подходящих чисел больше нет
        if ((myLong < 1) || (myLong > myMax)) {
            myLong = 0;
        }
        return result;
    }
}
