/*******************************************************************************
*                                                                              *
*                           Многомерные массивы. №8.                           *
*    В числовой матрице поменять местами два столбца, т. е. все элементы       *
*  одного столбца поставить на соответствующие им позиции другого, а элементы  *
*  второго переместить в первый. Номера столбцов вводит пользователь с         *
*  клавиатуры.                                                                 *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M8 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int m = in.nextInt(); int n = in.nextInt();
    long[][] a = new long[m][n];
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        a[i][j] = in.nextLong();

    int p = in.nextInt() - 1; int q = in.nextInt() - 1;
    for (int i = 0; i < m; i++) {
      long t = a[i][p]; a[i][p] = a[i][q]; a[i][q] = t;
    }

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++)
        System.out.print(a[i][j] + " ");
      System.out.println();
    }
  }
}