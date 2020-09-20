/*******************************************************************************
*                                                                              *
*                                Ветвления. №5.                                *
*  Вычислить значение функции.                                                 *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class D5 {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);

    System.out.print("x=");
    double x = in.nextDouble();

    double F = (x>3 ? 1/(x*x*x + 6) : x*x - 3*x + 9);
    System.out.println("F(" + x + ")=" + F);
  }
}