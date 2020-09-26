/*******************************************************************************
*                                                                              *
*                            Многомерные массивы. №1.                          *
*    Дана матрица. Вывести на экран все нечетные столбцы, у которых первый     *
*  элемент больше последнего.                                                  *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M1 {
  public static void main(String[] args) {
    Scanner in = new Scanner (System.in);

    int m=in.nextInt(); int n=in.nextInt();
    long[][] a = new long[m][n];
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        a[i][j] = in.nextLong();

    for (int j = 0; j < n; j+=2) // Нумерация в матрице с 1 => j=0 - нечётный столбец
      if (a[0][j] > a[m-1][j]) {
        for (int i = 0; i < m; i++)
          System.out.print(a[i][j] + " ");
        System.out.println();
      }
  }
}