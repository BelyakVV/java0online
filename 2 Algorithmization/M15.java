/*******************************************************************************
*                                                                              *
*                          Многомерные массивы. №15.                           *
*    Найдите наибольший элемент матрицы и замените все нечетные элементы на    *
*  него.                                                                       *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M15 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int m = in.nextInt(); int n = in.nextInt();
    long[][] a = new long[m][n];
    long max = Long.MIN_VALUE;
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++) {
        long aij = in.nextLong();
        a[i][j] = aij;
        if (aij > max) max = aij;
      }

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        long aij = a[i][j];
        if ((aij & 1) == 1) {
          aij = max;
          a[i][j] = aij;
        }
        System.out.print(aij + " ");
      }
      System.out.println();
    }
  }
}