/*******************************************************************************
*                                                                              *
*                          Одномерные массивы. №8.                             *
*    Дана последовательность целых чисел a 1 , a 2 ,  , a n . Образовать      *
*  новую последовательность, выбросив из исходной те члены, которые равны      *
*  min( a 1 , a 2 ,  , a n ) .                                                *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L8 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int min=0; // Индекс минимального элемента
    int c=0;   // Количество выбрасываемых элементов
    int n = in.nextInt();
    long[] a_in = new long[n];
    for(int i=0; i<n; i++) {
       a_in[i]=in.nextLong();
       if(a_in[i]==a_in[min]) {c++;} else
         if(a_in[i]<a_in[min]) {
           c=1;
           min=i;
         }
    }
    long[] a_out = new long[n-c];
    int j=0;
    for(int i=0; i<n; i++) {
      if(a_in[i]==a_in[min]) continue;
      a_out[j]=a_in[i];
      System.out.print(a_out[j++] + " ");
    }
    System.out.println();
  }
}