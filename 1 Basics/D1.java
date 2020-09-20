import java.util.Scanner;

public class D1 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("Введите угол А: ");
    float A = Math.abs(in.nextFloat());

    System.out.print("Введите угол B: ");
    float B = Math.abs(in.nextFloat());

    if ((A + B) >= 180) {
      System.out.println("Такого треугольника не существует.");
    } else
      if ((A==90) || (B==90) || (A+B==90)) {
        System.out.println("Это прямоугольный треугольник.");
      } else {
        System.out.println("Треугольник существует.");
      }
  }
}