/*
 * Строки - объекты. №3.
 *
 *   Проверить, является ли заданное слово палиндромом.
 **
 *   Вход: исходная строка
 *   Выход: ответ да или нет
 */

import java.util.Scanner;


public class O3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String s = in.next();
        String reverse = (new StringBuilder(s)).reverse().toString();
        System.out.println(s.equalsIgnoreCase(reverse) ? "Да" : "Нет");
    }
}