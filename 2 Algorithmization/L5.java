/*******************************************************************************
*                                                                              *
*                             Одномерные массивы. №5.                          *
*    Даны целые числа а 1 ,а 2 ,..., а n . Вывести на печать только те числа,  *
*  для которых а i > i.                                                        *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L5 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    long[] a = new long[n];
    for(int i=0; i<n;) {
      long ai=in.nextLong(); a[i]=ai;
      if(ai>++i) System.out.print(ai + " ");
    }
    System.out.println();
  }
}