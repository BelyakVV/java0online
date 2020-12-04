/*
 * Сортировки. №6.
 *
 * Сортировка Шелла. Дан массив n действительных чисел. Требуется упорядочить
 * его по возрастанию. Делается это следующим образом: сравниваются два
 * соседних элемента a i и a i + 1 . Если a i <= a i + 1 , то продвигаются
 * на один элемент вперед. Если a i > a i + 1 , то производится перестановка
 * и сдвигаются на один элемент назад. Составить алгоритм этой сортировки.
 *
 * Указанный в условии алгоритм - не сортировка Шелла, а гномья сортировка.
 * В решении реализованы обе сортировки.
 */

import java.util.Scanner;


public class S6 {
    public static void main(String[] args) {
      Scanner in = new Scanner(System.in);
      int n = in.nextInt();
      long[] a = new long[n];
      for (int i = 0; i < n; i++) {
          a[i] = in.nextLong();
      }

      long[] b = new long[a.length];
      System.arraycopy(a, 0, b, 0, a.length);

      gnome(a);
      arrayPrint(a);

      shell(b);
      arrayPrint(b);
    }


    /*
     * Гномья сортировка
     */
    static void gnome(long[] a) {
        int i = 0;
        while (i < a.length - 1) {
            if (a[i] <= a[i + 1]) {
                i++;
            } else {
                long t = a[i];
                a[i] = a[i + 1];
                a[i + 1] = t;
                if (i > 0) {
                    i--;
                }
            }
        }
    }


    /*
     * Сортировка Шелла
     */
    static void shell(long[] a) {
        int delta = (a.length / 3) | 1; //delta >= 1
        while (delta > 0) {
            for (int start = 0; start < delta; start++) {
                for (int i = start + delta; i < a.length; i += delta) {
                    long ai = a[i];
                    int j = i - delta;
                    while (j >= start) {
                        if (a[j] <= ai) break;
                        a[j + delta] = a[j];
                        j -= delta;
                    }
                    a[j + delta] = ai;
                }
            }
            delta = (delta >> 1);
        }
    }


    /*
     * Печать массива
     */
    static void arrayPrint(long[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }
}