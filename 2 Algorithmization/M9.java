/*******************************************************************************
*                                                                              *
*                           Многомерные массивы. №9.                           *
*    Задана матрица неотрицательных чисел. Посчитать сумму элементов в каждом  *
*  столбце. Определить, какой столбец содержит максимальную сумму.             *                                                    *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M9 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int m = in.nextInt(); int n = in.nextInt();
    double[][] a = new double[m][n]; // Неотрицательные - не обязательно целые!
    double[] s = new double[n]; // Суммы столбцов
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++) {
        double aij = in.nextDouble();
        a[i][j] = aij;
        s[j] += aij;
      }

    int max = 0; // Столбец с максимальной суммой
    for (int j = 0; j < n; j++) {
      if (s[j] > s[max]) max = j;
      System.out.print(s[j] + " ");
    }
    System.out.println("\n" + (max + 1));
  }
}