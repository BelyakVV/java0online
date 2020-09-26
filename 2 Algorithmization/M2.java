/*******************************************************************************
*                                                                              *
*                             Многомерные массивы. №2.                         *
*    Дана квадратная матрица. Вывести на экран элементы, стоящие на диагонали. *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M2 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    long[][] a = new long[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        a[i][j] = in.nextLong();

    for (int i = 0; i < n; i++)
      System.out.print(a[i][i] + " ");
    System.out.println();
  }
}