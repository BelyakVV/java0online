/*******************************************************************************
*                                                                              *
*                                  Циклы. №1.                                  *
*    Пользователь вводит любое целое положительное число, а программа          *
*  суммирует все числа от 1 до введенного пользователем числа.                 *
*                                                                              *
*******************************************************************************/

import java.util.Scanner;


public class C1 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("Введите целое положительное число: ");
    long n = in.nextLong();
    if (n<1) {
      System.out.println("Неверное число");
      System.exit(1);
    }

    long S=0; // (n+1)*n/2;
    while (n>0) S+=n--;

    System.out.println("Сумма: " + S);
  }
}