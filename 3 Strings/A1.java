/*
 * Строки - массивы. №1.
 *
 *   Дан массив названий переменных в camelCase. Преобразовать названия в
 * snake_case.
 **
 *   Вход: количество строк n, затем n строк, каждая с новой строки
 *   Выход: n преобразованных строк, каждая с новой строки
 */

import java.util.Scanner;


public class A1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        int n = in.nextInt();
        String[] s = new String[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.next();
            s[i] = transform(s[i]);
            System.out.println(s[i]);
        }
    }


    /*
     * Преобразование camelCase в snake_case
     */
    static String transform(String s) {
        int shift = 0; //Сдвиг длины массива
        for (int i = 1; i < s.length(); i++) { //кроме первой позиции
            if (Character.isUpperCase(s.charAt(i))) shift++;
        }
        char[] result = new char[s.length() + shift];
        shift = 0;
        if (s.length() > 0) result[0] = Character.toLowerCase(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                result[i + shift++] = '_';
                result[i + shift] = Character.toLowerCase(s.charAt(i));
            } else {
                result[i + shift] = s.charAt(i);
            }
        }
        return new String(result);
    }
}