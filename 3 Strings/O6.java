/*
 * Строки - объекты. №6.
 *
 *   Из заданной строки получить новую, повторив каждый символ дважды.
 **
 *   Вход: строка
 *   Выход: обработанная строка
 */

import java.util.Scanner;


public class O6 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        StringBuilder s = new StringBuilder(in.next());
        int l1 = s.length(); //Исходная длина строки
        s.setLength(l1 << 1);
        for (int i = l1 - 1; i >= 0; i--) {
            char si = s.charAt(i);
            s.setCharAt(i << 1, si);
            s.setCharAt((i << 1) + 1, si);
        }
        System.out.println(s.toString());
    }
}