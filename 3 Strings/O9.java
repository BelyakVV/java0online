/*
 * Строки - объекты. №9.
 *
 *   Посчитать количество строчных (маленьких) и прописных (больших) букв в
 * введенной строке. Учитывать только английские буквы.
 **
 *   Вход: строка
 *   Выход: через пробел количество строчных и прописных английских букв
 */

import java.util.Scanner;


public class O9 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        String s = in.next();
        int uppers = 0;
        int lowers = 0;
        for (int i = 0; i < s.length(); i++) {
            char si = s.charAt(i);
            if (si >= 'A' && si <= 'Z') {
                uppers++;
            } else if (si >= 'a' && si <= 'z') {
                lowers++;
            }
        }
        System.out.println(lowers + " " + uppers);
    }
}