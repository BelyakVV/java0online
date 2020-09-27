/*******************************************************************************
*                                                                              *
*                          Многомерные массивы. №12.                           *
*    Отсортировать строки матрицы по возрастанию и убыванию значений           *
*  элементов.                                                                  *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M12 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int m = in.nextInt(); int n = in.nextInt();
    long[][] a = new long[m][n];
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        a[i][j] = in.nextLong();

    for (int i = 0; i < m; i++)
      for (int j = 0; j < n - 1; j++)
        for (int k = j; k < n; k++)
          if (a[i][k] < a[i][j]) {
            long t = a[i][j]; a[i][j] = a[i][k]; a[i][k] = t;
          }
    printMatrix(a);
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n - 1; j++)
        for (int k = j; k < n; k++)
          if (a[i][k] > a[i][j]) {
            long t = a[i][j]; a[i][j] = a[i][k]; a[i][k] = t;
          }
    printMatrix(a);
  }


  static void printMatrix(long[][] a) {
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[i].length; j++)
        System.out.print(a[i][j] + " ");
      System.out.println();
    }
  }
}