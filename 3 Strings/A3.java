/*
 * Строки - массивы. №3.
 *
 *   В строке найти количество цифр.
 **
 *   Вход: исходная строка
 *   Выход: количество цифр
 */

import java.util.Scanner;


public class A3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        char[] s = in.next().toCharArray();
        int digits = 0; //Количество найденных цифр
        for (int i = 0; i < s.length; i++) {
            if (Character.isDigit(s[i])) digits++;
        }
        System.out.println(digits);
    }
}