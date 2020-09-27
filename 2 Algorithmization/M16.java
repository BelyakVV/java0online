/*******************************************************************************
*                                                                              *
*                          Многомерные массивы. №16.                           *
*    Магическим квадратом порядка n называется квадратная матрица размера      *
*  n x n, составленная из чисел 1, 2, 3, ..., n 2 так, что суммы по каждому    *
*  столбцу, каждой строке и каждой из двух больших диагоналей равны между      *
*  собой. Построить такой квадрат.                                             *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class M16 {
  static int n;
  static long[][] a;

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    n = in.nextInt();
    if ((n < 1) || (n == 2)) {
      System.out.println("Такой квадрат невозможно построить.");
      System.exit(1);
    }
    a = new long[n][n];
    if ((n & 1) == 1) {
      odd();
    } else {
      if ((n & 2) == 2) {
        evenOdd();
      } else evenEven();
    }

    int c = 1; // Считаем количество десятичных разрядов в наибольшем числе
    long t = n * n;
    while (t > 0) {
      c++;
      t = t / 10;
    }
    String format = "%" + c + "d";
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++)
        System.out.printf(format, a[i][j]);
      System.out.println();
    }
  }


////////////////////////////    Сиамский метод   ///////////////////////////////
  static void odd() {
    long c = 1; long next = n; long last = next * next;
    int i = 0; int j = (int)(next >> 1);
    while (c <= last) {
      a[i][j] = c++;
      if (c > next) {
        next += n;
        i++;
      } else {
        if (--i < 0) i += n;
        if (++j >= n) j -= n;
      }
    }
  }


/////////////////////////    Квадрат размерностью 4*k   ////////////////////////
  static void evenEven() {
    linear();
    for (int i = 0; i < n >> 1; i++) // i < n/2
      for (int j = (i + 1) & 1; j < n >> 1; j += 2) { // j < n/2
        reflect(i, j);
        reflect(i, n - j - 1);
      }
  }


  static void linear() {
    long c = 1;
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        a[i][j] = c++;
  }


  static void reflect(int i, int j) {
    long t = a[i][j];
    a[i][j] = a[n-i-1][n-j-1];
    a[n-i-1][n-j-1] = t;
  }


///////////////////////     Метод окаймлённых квадратов    /////////////////////
  static void evenOdd() {
    n -= 2; // Сначала построить квадрат размерностью n-2
    evenEven();
    long delta = (n + 1) << 1; // 2*(n + 1)
    for (int i = n; i > 0; i--)
      for (int j = n; j > 0; j--)
        a[i][j] = a[i - 1][j - 1] + delta;

    n += 2;
    final int LAST = n - 1; // Правый столбец / нижняя строка
    long d = n * n + 1;
    int m = n >> 1; // n/2

               // Угловые ячейки
    a[0][0] = (m << 1) + m - 1; // 3*m - 1
    a[0][LAST] = 1;
    a[LAST][0] = d - 1;
    a[LAST][LAST] = d + 1 - m - (m << 1); // d - 3*m + 1

    int c = 1; // Горизонтальные части рамки
    for (int i = 1; i < m - 1; i++) {
      long t = (i << 1) + 1; // 2*i + 1
      a[0][c] = t;
      a[LAST][c++] = d - t;
    }
    for (int j = 1; j <= m; j++) {
      long t = j << 1; // 2*j
      a[LAST][c] = t;
      a[0][c++] = d - t;
    }

    c = 2; // Вертикальные части рамки
    a[1][0] = (m << 1) - 1; // 2*m - 1
    a[1][LAST] = d - (m << 1) + 1;
    for (int i = 1; i <= m >> 1; i++) {
      long t = m + (m << 1) - i - 1; // 3*m - i - 1
      a[c][0] = t;
      a[c++][LAST] = d - t;
    }
    for (int j = 1; j <= (m >> 1) + 1; j++) {
      long t = (m << 2) - j - 1; // 4*m - j - 1
      a[c][LAST] = t;
      a[c++][0] = d - t;
    }
    for (int q = 1; q < m >> 1; q++) {
      long t = (m << 1) + m + q - 1; // 3*m - 1 + q
      a[c][0] = t;
      a[c++][LAST] = d - t;
      t = (m << 1) + q; // 2*m + q
      a[c][LAST] = t;
      a[c++][0] = d - t;
    }
  }
}