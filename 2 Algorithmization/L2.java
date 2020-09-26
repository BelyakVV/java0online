/*******************************************************************************
*                                                                              *
*                           Линейные массивы. №2.                              *
*    Дана последовательность действительных чисел а 1 ,а 2 ,..., а n .         *
*  Заменить все ее члены, большие данного Z, этим числом. Подсчитать           *
*  количество замен.                                                           *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L2 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    double[] a = new double[n];
    for(int i=0; i<n; i++)
      a[i] = in.nextDouble();
    double z = in.nextDouble();

    int c=0; // Счётчик замен
    for(int i=0; i<n; i++)
      if(a[i]>z) {
        a[i]=z;
        c++;
      }

    System.out.println(c);
    }
}