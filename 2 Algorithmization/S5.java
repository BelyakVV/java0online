/*******************************************************************************
*                                                                              *
*                               Сортировки. №5.                                *
*    Сортировка вставками. Дана последовательность чисел                       *
*  a 1 , a 2 , ... , a n . Требуется переставить числа в порядке возрастания.  *
*  Делается это следующим образом. Пусть a 1 , a 2 , ... , a i - упорядоченная *
*  последовательность, т. е. a 1 <= a 2 <= ... <= a n . Берется следующее      *
*  число a i + 1 и вставляется в последовательность так, чтобы новая           *
*  последовательность была тоже возрастающей. Процесс производится до тех пор, *
*  пока все элементы от i +1 до n не будут перебраны. Примечание. Место        *
*  помещения очередного элемента в отсортированную часть производить с помощью *
*  двоичного поиска. Двоичный поиск оформить в виде отдельной функции.         *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class S5 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    long[] a = new long[n];
    for (int i = 0; i < n; i++)
      a[i] = in.nextLong();

    for (int i = 1; i < n; i++) {
      int pos = getPos(a, i);
      long ai = a[i];
      for (int j = i; j > pos; j--)
        a[j] = a[j - 1];
      a[pos] = ai;
    }

    for (int i = 0; i < n; i++)
      System.out.print(a[i] + " ");
    System.out.println();
  }


  static int getPos(long[] a, int n) { // n - размер отсортированной области
    long key = a[n];
    int lostBit = n & 1;
    int delta = n >> 1;
    int Result = delta;
    while (delta > 1) { // Корректирующие циклы быстрее, чем основной
      delta = delta + lostBit;
      lostBit = delta & 1;
      delta = delta >> 1;
      if (key < a[Result]) {
        Result -= delta;
      } else Result += delta;
    }
    while ((key < a[Result]) && (Result > 0)) Result--;
    while ((key >= a[Result]) && (Result < n)) Result++;
    return Result;
  }
}