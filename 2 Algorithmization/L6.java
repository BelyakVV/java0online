/*******************************************************************************
*                                                                              *
*                            Одномерные массивы. №6.                           *
*    Задана последовательность N вещественных чисел. Вычислить сумму чисел,    *
*  порядковые номера которых являются простыми числами.                        *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L6 {

  static boolean isPrime(int n) {
    boolean Result=(n>1?true:false);
    if(Result) {
      for(int i=2; i<=n/2; i++)
        if(n%i==0) {
          Result=false;
          break;
        }
    }
    return Result;
  }


  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    double s=0; // Аккумулятор суммы
    int n = in.nextInt();
    double[] a = new double[n];
    for(int i=0; i<n;) {
      double ai=in.nextDouble(); a[i]=ai;
      if(isPrime(++i)) s+=ai;
    }
    System.out.println(s);
  }
}