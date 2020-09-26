/*******************************************************************************
*                                                                              *
*                          Многомерные массивы. №10.                           *
*    Найти положительные элементы главной диагонали квадратной матрицы.        *                                                    *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M10 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    double[][] a = new double[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++)
        a[i][j] = in.nextDouble();
      if (a[i][i] > 0)
        System.out.print(a[i][i] + " ");
    }
    System.out.println();
  }
}