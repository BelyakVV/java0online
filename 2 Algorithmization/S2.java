/*******************************************************************************
*                                                                              *
*                               Сортировки. №2.                                *
*    Даны две последовательности a 1 <= a 2 <= ... <= a n                      *
*  и b 1 <= b 2 <= ... <= b m . Образовать из них новую последовательность     *
*  чисел так, чтобы она тоже была неубывающей. Примечание. Дополнительный      *
*  массив не использовать.                                                     *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class S2 {
  static Scanner in;


  public static void main(String[] args) {
    in = new Scanner(System.in);
    long[] a = nextArray();
    long[] b = nextArray();

    int i = 0; int j = 0;
    long ai = a[i]; long bj = b[j];
    for (int c = a.length + b.length; c > 0; c--) {
      if (ai < bj) {
        System.out.print(ai + " ");
        if (++i < a.length) {
          ai = a[i];
        } else ai = Long.MAX_VALUE;
      } else {
        System.out.print(bj + " ");
        if (++j < b.length) {
          bj = b[j];
        } else bj = Long.MAX_VALUE;
      }
    }
    System.out.println();
  }


  static long[] nextArray() {
    int n = in.nextInt();
    long[] Result = new long[n];
    for (int i = 0; i < n; i++)
      Result[i] = in.nextLong();
    return Result;
  }
}