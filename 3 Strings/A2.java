/*
 * Строки - массивы. №2.
 *
 *   Замените в строке все вхождения 'word' на 'letter'.
 **
 *   Вход: исходная строка
 *   Выход: преобразованная строка
 */

import java.util.Scanner;


public class A2 {
    public static void main(String[] args) {
        char[] CHR_WORD = "word".toCharArray();
        char[] CHR_LETTER = "letter".toCharArray();
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        char[] s = in.next().toCharArray();
        System.out.println(new String(replace(s, CHR_WORD, CHR_LETTER)));
    }


    /*
     * Замена в строке s подстроки sub1 на sub2
     */
    static char[] replace(char[] s, char[] sub1, char[] sub2) {
        if ((sub1.length > s.length) || (s.length == 0) || (sub1.length == 0)) {
            return s;
        }
        int shift = 0; //Сдвиг длины массива
        int i = 0; //Позиция в строке s
        int j = 0; //Позиция в sub1
        while (i < s.length) { //Сначала поиск и подсчёт количества вхождений
            while ((i < s.length) && (j < sub1.length)) {
                if (s[i++] != sub1[j++]) { //Несовпадение или неполное совпадение
                    i -= j - 1; //Откат к началу + 1 позция
                    break;
                }
                if (j == sub1.length) shift++; //Полное совпадение
            }
            j = 0;
        }
        if (shift == 0) return s; //Ничего не найдено
        shift *= sub2.length - sub1.length;
        char[] result = new char[s.length + shift];
        shift = 0;
        i = 0;
        j = 0;
        while (i < s.length) { //Теперь замена
            while ((i < s.length) && (j < sub1.length)) {
                if (s[i++] != sub1[j++]) { //Несовпадение
                    i -= j; //Откат к началу
                    result[i + shift] = s[i];
                    i++;
                    break;
                }
                if (j == sub1.length) { //Полное совпадение
                    System.arraycopy(sub2, 0, result, i - j + shift, sub2.length);
                    shift += sub2.length - sub1.length;
                };
            }
            j = 0;
        }
        return result;
    }
}