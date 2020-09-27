/*******************************************************************************
*                                                                              *
*                          Многомерные массивы. №13.                           *
*    Отсортировать столбцы матрицы по возрастанию и убыванию значений          *
*  элементов.                                                                  *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M13 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int m = in.nextInt(); int n = in.nextInt();
    long[][] a = new long[m][n];
    for (int i = 0; i < m; i++)
      for (int j = 0; j < n; j++)
        a[i][j] = in.nextLong();

    long[] col = new long[m]; // Для экономии памяти объявлено здесь
    for (int j = 0; j < n; j++) {
      getColumn(a, j, col);
      for (int k = 0; k < m - 1; k++)
        for (int l = k; l < m; l++)
          if (col[l] < col[k]) {
            long t = col[k]; col[k] = col[l]; col[l] = t;
          }
      putColumn(col, a, j);
    }
    printMatrix(a);
    for (int j = 0; j < n; j++) {
      getColumn(a, j, col);
      for (int k = 0; k < m - 1; k++)
        for (int l = k; l < m; l++)
          if (col[l] > col[k]) {
            long t = col[k]; col[k] = col[l]; col[l] = t;
          }
      putColumn(col, a, j);
    }
    printMatrix(a);
  }


  static void getColumn(long[][] a, int j, long[] col) {
    for (int i = 0; i < a.length; i++)
      col[i] = a[i][j];
  }


  static void putColumn(long[] col, long[][] a, int j) {
     for (int i = 0; i < a.length; i++)
       a[i][j] = col[i];
  }


  static void printMatrix(long[][] a) {
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[i].length; j++)
        System.out.print(a[i][j] + " ");
      System.out.println();
    }
  }
}