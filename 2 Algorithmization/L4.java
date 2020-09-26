/*******************************************************************************
*                                                                              *
*                           Одномерные массивы. №4.                            *
*    Даны действительные числа а 1 ,а 2 ,..., а n . Поменять местами           *
*  наибольший и наименьший элементы.                                           *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L4 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int least=0; int most=0; // Индексы наименьшего и наибольшего элементов
    int n = in.nextInt();
    double[] a = new double[n];
    for(int i=0; i<n; i++) {
      double ai = in.nextDouble(); a[i]=ai;
      if(ai<a[least]) {least=i;} else
        if(ai>a[most]) most=i;
    }

    if(least!=most) {
      double t=a[least]; a[least]=a[most]; a[most]=t;
    }

    for(int i=0; i<n; i++) System.out.print(a[i] + " ");
    System.out.println();
  }
}