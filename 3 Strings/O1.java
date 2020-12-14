/*
 * Строки - объекты. №1.
 *
 *   Дан текст (строка). Найдите наибольшее количество подряд идущих пробелов в
 * нем.
 **
 *   Вход: исходная строка
 *   Выход: наибольшее количество подряд идущих пробелов
 */

import java.util.Scanner;


public class O1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String s = in.next();
        int spaces = 0;
        int maxSpaces = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.codePointAt(i) == ' ') {
                spaces++;                                   
                if (spaces > maxSpaces) maxSpaces = spaces;
            } else {
                spaces = 0;
            }
        }
        System.out.println(maxSpaces);
    }
}