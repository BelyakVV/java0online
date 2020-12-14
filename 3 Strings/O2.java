/*
 * Строки - объекты. №2.
 *
 *   В строке вставить после каждого символа 'a' символ 'b'.
 **
 *   Вход: исходная строка
 *   Выход: обработанная строка
 */

import java.util.Scanner;


public class O2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String s = in.next().replace("a", "ab");
        System.out.println(s);
    }
}