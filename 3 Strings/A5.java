/*
 * Строки - массивы. №5.
 *
 *   Удалить в строке все лишние пробелы, то есть серии подряд идущих пробелов
 * заменить на одиночные пробелы. Крайние пробелы в строке удалить.
 **
 *   Вход: исходная строка
 *   Выход: обработанная строка
 */

import java.util.Scanner;


public class A5 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        System.out.println(compress(in.next().toCharArray()));
    }


    /*
     * Собственно здесь основная обработка
     */
    static String compress(char[] s) {
        int i; //Индекс исходного элемента
        int j = 0; //Индекс обработанного элемента                      
        i = skipWhitespaces(s, 0); //Пропуск пробелов в начале
        while (i < s.length) {
            while (!Character.isWhitespace(s[i])) { //Очередное слово
                s[j++] = s[i++];
                if (i == s.length) break;
            }
            i = skipWhitespaces(s, i); //Пропуск пробелов после слова
            if (i < s.length) s[j++] = ' '; //Ещё что-то есть - пробел нужен
        }
        char[] result = new char[j];
        System.arraycopy(s, 0, result, 0, j);
        return new String(result);
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