/******************************************************************************
*                                                                             *
*                               Ветвления. №3.                                *
*   Даны три точки А(х1,у1), В(х2,у2) и С(х3,у3).                             *
*   Определить, будут ли они расположены на одной прямой.                     *
*                                                                             *
******************************************************************************/

import java.util.Scanner;

class Point {
  double x, y;
}

public class D3 {
  Point A, B, C;

  static boolean oneLine() {
    if ((x1==x2)&&(x1==x3) || (y1==y2)&&(y1==y3)) {
      return true;
    } else {
      double dx1 = x[2] - x[1];
      double dx2 = x[3] - x[1];
      double dy1 = y[2] - y[1];
      double dy2 = y[3] - y[1];
      if (dx1*dy2 == dx2*dy1) {
        return true;
      } else {
        return false;
      }
    }
  }


  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("Точка A(x, y): ");
    x[1] = in.nextDouble();
    y[1] = in.nextDouble();

    System.out.print("Точка B(x, y): ");
    x[2] = in.nextDouble();
    y[2] = in.nextDouble();

    System.out.print("Точка C(x, y): ");
    x[2] = in.nextDouble();
    y[2] = in.nextDouble();

    System.out.print("Точки ");
    if (!oneLine()) { System.out.print("не "); }
    System.out.print("принадлежат одной прямой.");
  }
}