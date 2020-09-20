/*******************************************************************************
*                                                                              *
*                                Ветвления. №4.                                *
*    Заданы размеры А, В прямоугольного отверстия и размеры х, у, z кирпича.   *
*  Определить, пройдет ли кирпич через отверстие.                              *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class D4 {


  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    double A, B, x, y, z;

    System.out.print("Размеры отверстия: ");
    A = in.nextDouble(); B = in.nextDouble();

    System.out.print("Размеры кирпича: ");
    x = in.nextDouble(); y = in.nextDouble(); z = in.nextDouble();

    if ((x<=A)&&(y<=B)||(x<=B)&&(y<=A)||
        (x<=A)&&(z<=B)||(x<=B)&&(z<=A)||
        (z<=A)&&(y<=B)||(z<=B)&&(y<=A)) {
      System.out.println("Кирпич проходит");
    } else {
      System.out.println("Кирпич не проходит");
    }
  }
}