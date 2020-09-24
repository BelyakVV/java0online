/*******************************************************************************
*                                                                              *
*                                  Циклы. №2.                                  *
*  Вычислить значения функции на отрезке [а,b] c шагом h.                      *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class C2 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("a=");
    double a = in.nextDouble();
    System.out.print("b=");
    double b = in.nextDouble();
    System.out.print("h=");
    double h = in.nextDouble();

    while(a<=b) {
      System.out.println("y(" + a + ")=" + (a>2?a:-a));
      a+=h;
    }
  }
}