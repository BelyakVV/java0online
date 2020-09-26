/*******************************************************************************
*                                                                              *
*                            Одномерные массивы. №10.                          *
*    Дан целочисленный массив с количеством элементов n. Сжать массив,         *
*  выбросив из него каждый второй элемент (освободившиеся элементы заполнить   *
*  нулями). Дополнительный массив не использовать.                             *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L10 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    long[] a = new long[n];
    for(int i=0; i<n; i++)
      a[i]=in.nextLong();

    for(int i=1; i<n; i+=2)
      a[i]=0;

    for(int i=0; i<n; i++)
      System.out.print(a[i] + " ");
    System.out.println();
  }
}