/*******************************************************************************
*                                                                              *
*                                Циклы. №5.                                    *
*    Даны числовой ряд и некоторое число е. Найти сумму тех членов ряда,       *
*  модуль которых больше или равен заданному е.                                *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class C5 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    System.out.print("e=");
    double e = in.nextDouble();
    double S = 0;

    if(e>0) {
      long p2=2; long p3=3; double p6=6; double a=0;
      do {
        S += a;
        a = (p2 + p3) / p6;
        p2 = p2 * 2;
        p3 = p3 * 3;
        p6 = p6 * 6;
      } while(a>=e);
    } else {
      S = 1.5;
    }

    System.out.println("S=" + S);
  }
}