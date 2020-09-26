/*******************************************************************************
*                                                                              *
*                           Многомерные массивы. №6.                           *
*    Сформировать квадратную матрицу порядка n по заданному образцу            *
*  (n - четное):                                                               *
*                          1   1   1  ...  1   1   1                           *
*                          0   1   1  ...  1   1   0                           *
*                          0   0   1  ...  1   0   0                           *
*                         ... ... ... ... ... ... ...                          *
*                          0   1   1  ...  1   1   0                           *
*                          1   1   1  ...  1   1   1                           *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M6 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    int[][] a = new int[n][n];
    for (int i = 0; i <= n / 2; i++)
      for (int j = i; j < n - i; j++)
        a[i][j] = 1;
    for (int i = n / 2; i < n; i++)
      a[i] = a[n-i-1];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++)
        System.out.print(a[i][j] + " ");
      System.out.println();
    }
  }
}