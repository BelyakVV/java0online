/******************************************************************************
*                                                                             *
*                               Ветвления. №3.                                *
*   Даны три точки А(х1,у1), В(х2,у2) и С(х3,у3).                             *
*   Определить, будут ли они расположены на одной прямой.                     *
*                                                                             *
******************************************************************************/

import java.util.Scanner;


public class D3 {

  static double x1, y1, x2, y2, x3, y3;

  static boolean oneLine() {
    if ((x1==x2)&&(x1==x3) || (y1==y2)&&(y1==y3)) {
      return true;
    } else {
      double dx1 = x2 - x1;
      double dx2 = x3 - x1;
      double dy1 = y2 - y1;
      double dy2 = y3 - y1;
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
    x1 = in.nextDouble();
    y1 = in.nextDouble();

    System.out.print("Точка B(x, y): ");
    x2 = in.nextDouble();
    y2 = in.nextDouble();

    System.out.print("Точка C(x, y): ");
    x3 = in.nextDouble();
    y3 = in.nextDouble();

    System.out.print("Точки ");
    if (!oneLine()) { System.out.print("не "); }
    System.out.println("принадлежат одной прямой.");
  }
}