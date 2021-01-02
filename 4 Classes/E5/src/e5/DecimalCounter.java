package e5;

/**
 *
 * @author aabyodj
 */
public final class DecimalCounter {
    private static final String OVERFLOW_HI_MSG = "Значение счётчика больше верхней границы";
    private static final String OVERFLOW_LO_MSG = "Значение счётчика меньше нижней границы";

    /** 
     * Значение счётчика в виде массива десятичных разрядов.
     * Старший разряд содержит знак: < 0 для отрицательного, 0 для 0, > 0 для 
     * положительного
     */
    private int[] digits;
    
    /** Верхняя граница заданного диапазона */
    private final int[] hiBound;
    /** Нижняя граница заданного диапазона */
    private final int[] loBound;

    /** Инициализация счётчика и границ значениями по умолчанию */
    public DecimalCounter() {
        this(0);
    }

    /**
     * Инициализация счётчика произвольным значением, границ - значениями по 
     * умолчанию.
     * @param init Начальное значение счётчика
     */
    public DecimalCounter(long init) {
        this(init, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /** 
     * Инициализация произвольными значениеми счётчика и границ
     * @param init начальное значение счётчика
     * @param loBound Нижняя граница
     * @param hiBound Верхняя граница
     */
    public DecimalCounter(long init, long loBound, long hiBound) {
        this.loBound = toDigits(loBound);
        this.hiBound = toDigits(hiBound);
        setValue(init);
    } 

    /**
     * Возврат нижней границы допустимых значений счётчика, установленной при 
     * его инициализации.
     * @return Нижняя граница
     */
    public long getLoBound() {
        return toLong(loBound);
    }
    
    /**
     * Возврат верхней границы допустимых значений счётчика, установленной при 
     * его инициализации.
     * @return Верхняя граница
     */
    public long getHiBound() {
        return toLong(hiBound);
    }
    
    /**
     * Установка текущего значения счётчика
     * @param value новое значение 
     */
    public void setValue(long value) {
        int size = calcSize(value); //количество десятичных разрядов
        size = Integer.max(Integer.max(size, loBound.length - 1), hiBound.length - 1);
        int[] newDigits = toDigits(value, size);
        if (compare(newDigits, loBound) < 0) {
            throw new IllegalArgumentException(OVERFLOW_LO_MSG);            
        }
        if (compare(newDigits, hiBound) > 0) {
            throw new IllegalArgumentException(OVERFLOW_HI_MSG);
        }
        digits = newDigits;
    }
    
    /**
     * Возврат текущего значения счётчика
     * @return текущее значение
     */
    public long getValue() {
        return toLong(digits);
    }
    
    /** Уменьшение значения счётчика на 1 */
    public void dec() {
        if (compare(digits, loBound) < 1) { //Нельзя пересечь нижнюю границу
            throw new ArithmeticException(OVERFLOW_LO_MSG);
        }
        if (digits[digits.length - 1] < 1) { //Знак числа: отрицательное или 0
            absInc();
            digits[digits.length - 1] = -1;
        } else { //Положительное
            absDec();
        }
    }
    
    /** Увеличение значения счётчика на 1 */
    public void inc() {
        if (compare(digits, hiBound) > -1) { //Нельзя пересечь верхнюю границу
            throw new ArithmeticException(OVERFLOW_HI_MSG);
        }
        if (digits[digits.length - 1] < 0) { //Знак числа: отрицательное
            absDec();
        } else { //Положительное или 0
            absInc();
            digits[digits.length - 1] = 1;
        }
    }
    
    /** Уменьшение модуля значения счётчика на единицу */
    private void absDec() {
        boolean borrow = true;
        boolean zero = true;
        for (int i = 0; i < digits.length - 1; i++) {
            if (borrow) digits[i]--;
            borrow = digits[i] < 0;
            if (borrow) digits[i] = 9;
            zero = zero && digits[i] == 0;
        }
        if (zero) digits[digits.length - 1] = 0; //Знак числа
    }
    
    /** Увеличение модуля значения счётчика на единицу */
    private void absInc() {
        boolean overflow = true;
        for (int i = 0; i < digits.length - 1; i++) {
            if (overflow) digits[i]++;
            overflow = digits[i] > 9;
            if(overflow) digits[i] = 0;
        }
    }
   
    /** Сравнение двух чисел, заданных в виде массива цифр */
    private int compare(int[] a, int[] b) {
        int signA = a[a.length - 1];
        int signB = b[b.length - 1];
        int result = signA - signB; 
        if (result != 0) return result;
        if (signA == 0) return 0; //Оба числа - нули
        int i;
        if (a.length > b.length) {
            i = a.length - 2;
            while (i > b.length - 2)
                if (a[i--] != 0) return signA;            
        } else {
            i = b.length - 2; 
            while(i > a.length - 2)
                if (b[i--] != 0) return -signA;
        } 
        //Числа одинаковой длины, или старшие разряды более длинного числа - нули
        while (i >= 0) {
            result = a[i] - b[i];
            if (result != 0) 
                return result > 0 ? signA : -signA;
            i--;
        }
        return 0; //Числа одинаковы
    }
    
    /** 
     * Вычисление количества десятичных разрядов числа n
     * @param n
     * @return 
     */
    private static int calcSize(long n) {
        if (n == Long.MIN_VALUE) n++; //Для Long.MIN_VALUE Math.abs() не работает
        return Integer.max((int)Math.log10(Math.abs(n)), 0) + 1;
    }
    
    /**
     * Преобразование числа в формате long в массив десятичных разрядов
     * @param a Число
     * @return Представление заданного числа в виде десятичных разрядов
     */
    private static int[] toDigits(long a) {
        return toDigits(a, calcSize(a));
    }
    
    /**
     *   Преобразование числа в формате long в массив десятичных разрядов 
     * заданного размера.
     * @param a Число
     * @param size Желаемое количество разрядов
     * @return Представление заданного числа в виде десятичных разрядов
     */
    private static int[] toDigits(long a, int size) {
        if (a == Long.MIN_VALUE) { //Math.abs() для Long.MIN_VALUE не работает
            int[] result = toDigits(++a, size);
            result[0]++;
            return result;
        }
        int[] result = new int[size + 1]; // +1 разряд для знака
        result[size] = Long.compare(a, 0); //Знак числа
        a = Math.abs(a);
        for (int i = 0; i < size; i++) {
            result[i] = (int)(a % 10);
            a = a / 10;
        }  
        return result;
    }
    
    /**
     * Преобразование числа в виде десятичных разрядов в формат long
     * @param digits Число в виде десятичных разрядов. Старший разряд - знак числа
     * @return Заданное число в формате long
     */
    private static long toLong(int[] digits) {
        int sign = digits[digits.length - 1]; //Знак числа
        if (0 == sign) return 0;
        long result = 0;
        long weight = 1; //Вес десятичного разряда
        for (int i = 0; i < digits.length - 1; i++) {
            result += digits[i] * weight;
            weight *= 10;
        }
        return sign > 0 ? result : -result;        
    }
}
