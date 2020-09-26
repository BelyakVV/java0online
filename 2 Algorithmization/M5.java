/*******************************************************************************
*                                                                              *
*                           Многомерные массивы. №5.                           *
*    Сформировать квадратную матрицу порядка n по заданному образцу            *
*  (n - четное):                                                               *
*                          1   1   1  ...  1   1   1                           *
*                          2   2   2  ...  2   2   0                           *
*                          3   3   3  ...  3   0   0                           *
*                         ... ... ... ... ... ... ...                          *
*                         n-1 n-1  0  ...  0   0   0                           *
*                          n   0   0  ...  0   0   0                           *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M5 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    int[][] a = new int[n][n];
    for (int i = 0; i < n;) {
      int k = i + 1;
      for (int j = 0; j < n - i; j++)
        a[i][j] = k;
      i = k;
    }

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++)
        System.out.print(a[i][j] + " ");
      System.out.println();
    }
  }
}