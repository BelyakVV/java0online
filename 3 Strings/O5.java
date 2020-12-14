/*
 * Строки - объекты. №5.
 *
 *   Подсчитать, сколько раз среди символов заданной строки встречается буква
 * “а”.
 **
 *   Будем считать русскую "а" наравне с латинской. Также не будем делать
 * различий между заглавными и строчными буквами.
 *   Вход: строка
 *   Выход: количество вхождений буквы “а”
 */

import java.util.Scanner;


public class O5 {
    public static void main(String[] args) {
        final char A_RUS = 'А';
        final char A_LAT = 'A';
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String s = in.next();
        int c = 0;
        for (int i = 0; i < s.length(); i++) {
            char si = Character.toUpperCase(s.charAt(i));
            if ((si == A_RUS) || (si == A_LAT)) {
                c++;
            }
        }
        System.out.println(c);
    }
}