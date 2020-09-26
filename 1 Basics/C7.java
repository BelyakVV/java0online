/*******************************************************************************
*                                                                              *
*                                   Циклы. №7.                                 *
*  Для каждого натурального числа в промежутке от m до n вывести все делители, *
*  кроме единицы и самого числа. m и n вводятся с клавиатуры.                  *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class C7 {

  static void notNatural() {
    System.out.println("Ошибка: это не натуральное число.");
    System.exit(1);
  }


  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("m=");
    long m = in.nextLong(); if(m<0) notNatural();
    System.out.print("n=");
    long n = in.nextLong(); if(n<0) notNatural();
    if(n<m) {
      long t=m; m=n; n=t;
    }

    for(long i=m; i<=n; i++) {
      System.out.print(i + ": ");
      for(long j=2; j<=i/2; j++)
        if((i%j)==0) System.out.print(j + "; ");
      System.out.println();
    }
  }
}