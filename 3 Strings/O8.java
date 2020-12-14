/*
 * Строки - объекты. №8.
 *
 *   Вводится строка слов, разделенных пробелами. Найти самое длинное слово и
 * вывести его на экран. Случай, когда самых длинных слов может быть несколько,
 * не обрабатывать.
 **
 *   Вход: строка
 *   Выход: самое длинное слово
 */

import java.util.Scanner;


public class O8 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String s = in.next();
        int wStart = 0; //Начало самого длинного слова
        int wLength = 0; //Его длина
        int start = 0; //Начало текущего слова
        int length = 0; //Длина текущего слова
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                if (0 == length) start = i;
                length++;
                if (length > wLength) {
                    wStart = start;
                    wLength = length;
                }
            } else {
                length = 0;
            }
        }
        System.out.println(s.substring(wStart, wStart + wLength));
    }
}