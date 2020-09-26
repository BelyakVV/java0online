/*******************************************************************************
*                                                                              *
*                          Одномерные массивы. №1.                             *
*    В массив A [N] занесены натуральные числа. Найти сумму тех элементов,     *
*  которые кратны данному К.                                                   *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L1 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    long[] a = new long[n];
    for(int i=0; i<n; i++)
      a[i] = in.nextLong();
    long k = in.nextLong();

    long s=0;
    for(int i=0; i<n; i++)
      if(a[i]%k == 0) s+=a[i];

    System.out.println(s);
  }
}