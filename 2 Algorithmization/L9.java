/*******************************************************************************
*                                                                              *
*                            Одномерные массивы. №9.                           *
*    В массиве целых чисел с количеством элементов n найти наиболее часто      *
*  встречающееся число. Если таких чисел несколько, то определить наименьшее   *
*  из них.                                                                     *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class L9 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    long[] a = new long[n];
    long[] numbers = new long[n]; // Перечисление чисел, встретившихся в a[]
    int[] freq = new int[n]; // Соответствующая частота обнаружения
    int q=0; // Размер numbers[] = количество разных чисел
    for(int i=0; i<n; i++) {
      a[i]=in.nextLong();
      int j=0;
      for(; j<q; j++) // Поиск в ранее найденных числах
        if(a[i]==numbers[j]) {
          freq[j]++;
          break;
        }
      if(j==q) { // Если это новое число
        q++;
        numbers[j]=a[i];
        freq[j]=1;
      }
    }
    int min=0; // Индекс наименьшего из наиболее часто встречающихся чисел
    for(int j=0; j<q; j++) {
      if(freq[j]>freq[min]) {
        min=j;
        } else
        if(freq[j]==freq[min])
          if(numbers[j]<numbers[min]) min=j;
    }
    System.out.println(numbers[min]);
  }
}