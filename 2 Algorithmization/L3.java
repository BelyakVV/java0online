/*******************************************************************************
*                                                                              *
*                           Одномерные массивы. №3.                            *
*    Дан массив действительных чисел, размерность которого N. Подсчитать,      *
*  сколько в нем отрицательных, положительных и нулевых элементов.             *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L3 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int neg=0; int zero=0; int pos=0;
    int n = in.nextInt();
    double[] a = new double[n];
    for(int i=0; i<n; i++) {
      double ai = in.nextDouble(); a[i] = ai;
      if(ai<0) {neg++;} else
        if(ai==0) {zero++;} else pos++;
    }

    System.out.println(neg + " " + pos + " " + zero);
  }
}