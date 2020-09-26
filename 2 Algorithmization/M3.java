/*******************************************************************************
*                                                                              *
*                           Многомерные массивы. №3.                           *
*    Дана матрица. Вывести k-ю строку и p-й столбец матрицы.                   *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M3 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int m = in.nextInt(); int n = in.nextInt();
    long[][] a = new long[m][n];
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        a[i][j] = in.nextLong();

    int k = in.nextInt();
    for (int j = 0; j < n; j++)
      System.out.print(a[k][j] + " ");
    System.out.println();

    int p = in.nextInt();
    for (int i = 0; i < m; i++)
      System.out.print(a[i][p] + " ");
    System.out.println();
  }
}