/*
 * Строки - массивы. №4.
 *
 *   В строке найти количество чисел.
 **
 *   Под числом будем понимать некоторую последовательность подряд идущих
 * символов, состоящую из цифр и знаков "-" (может встречаться не более одного
 * раза и находиться строго в начале числа) и "." (может встречаться не более
 * одного раза и находиться строго между цифрами). От остальной части строки
 * число должно быть отделено как минимум одним пробелом или знаком табуляции
 * (Character.isWhiteSpace())
 *   Вход: исходная строка
 *   Выход: количество цифр
 */

import java.util.Scanner;


public class A4 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        char[] s = in.next().toCharArray();
        int numbers = 0; //Количество найденных чисел
        int i = 0;
        mainLoop: while (i < s.length) {
            if (s[i] == '-') { //Минус может быть только в начале
                if (++i == s.length) break; //Минусом не может заканчиваться
            }
            if (!Character.isDigit(s[i])) { //Теперь должны быть только цифры
                i = skipTillWhitespace(s, i);
                i = skipWhitespaces(s, i);
                continue;
            }
            while (Character.isDigit(s[i])) { //Пропускаем цифры
                if (++i == s.length) {
                    numbers++;
                    break mainLoop;
                }
            }
            if (s[i] == '.') { //Тут может быть точка
                if (++i == s.length) break; //Точкой не может заканчиваться
                if (!Character.isDigit(s[i])) { //После точки должны быть цифры
                    i = skipTillWhitespace(s, i);
                    i = skipWhitespaces(s, i);
                    continue;
                }
                while (Character.isDigit(s[i])) { //Пропускаем цифры
                    if (++i == s.length) {
                        numbers++;
                        break mainLoop;
                    }
                }
            }
            if (Character.isWhitespace(s[i++])) numbers++; //После числа должен быть пробел
        }
        System.out.println(numbers);
    }


    /*
     * Пропуск до пробела
     */
    static int skipTillWhitespace(char[] s, int i) {
        while (i < s.length) {
            if (Character.isWhitespace(s[i])) break;
            i++;
        }
        return i;
    }


    /*
     * Пропуск пробелов и приравненых к ним символов
     */
    static int skipWhitespaces (char[] s, int i) {
        while (i < s.length) {
            if (!Character.isWhitespace(s[i])) break;
            i++;
        }
        return i;
    }
}