/******************************************************************************
*                       Ветвления. №2.                                        *
*                       Найти max{min(a, b), min(c, d)}                       *
*                                                                             *
******************************************************************************/

import java.util.Scanner;

public class D2 {
  static int max(int a, int b) {
    if (a>b) {
      return a;
    } else {
      return b;
    }
  }

  static int min(int a, int b) {
    if (a<b) {
      return a;
    } else {
      return b;
    }
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("a=");
    int a = in.nextInt();

    System.out.print("b=");
    int b = in.nextInt();

    System.out.print("c=");
    int c = in.nextInt();

    System.out.print("d=");
    int d = in.nextInt();

    System.out.println("max(min(" + a + ", " + b +
      "), min(" + c + ", " + d + ") = " +
      max(min(a, b), min(c,d)));
  }
}