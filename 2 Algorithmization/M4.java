/*******************************************************************************
*                                                                              *
*                           Многомерные массивы. №4.                           *
*    Сформировать квадратную матрицу порядка n по заданному образцу            *
*  (n - четное):                                                               *
*                            1   2   3  ...  n                                 *
*                            n  n-1 n-2 ...  1                                 *
*                            1   2   3  ...  n                                 *
*                            n  n-1 n-2 ...  1                                 *
*                           ... ... ... ... ...                                *
*                            n  n-1 n-2 ...  1                                 *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M4 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    int[][] a = new int[n][n];
    for (int j = 0; j < n;) {
      a[0][j] = ++j;
      a[1][n-j] = j;
    }
    for (int i = 2; i < n; i++)
      a[i] = a[i-2];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++)
        System.out.print(a[i][j] + " ");
      System.out.println();
    }
  }
}