/*
 * Строки - объекты. №7.
 *
 *   Вводится строка. Требуется удалить из нее повторяющиеся символы и все
 * пробелы. Например, если было введено "abc cde def", то должно быть выведено
 * "abcdef".
 **
 *   Вход: строка
 *   Выход: обработанная строка
 */

import java.util.Scanner;


public class O7 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String s = in.next().replace(" ", "");
        int i = 0;
        while (i < s.length()) {
            String si = s.substring(i, i + 1);
            String tail = s.substring(i + 1).replace(si, "");
            s = s.substring(0, i + 1).concat(tail);
            i++;
        }
        System.out.println(s);
    }
}