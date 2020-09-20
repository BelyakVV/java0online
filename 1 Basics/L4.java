import java.util.Scanner;

public class L4 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("Введите действительное число меньше 1000: ");
    float R = in.nextFloat();
    if (R>=1000) {
      System.out.println("Ошибка: слишком большое число.");
      System.exit(1);
    }

    int i = (int) R;
    int f = (int) (R * 1000 % 1000);
    System.out.println(f + "." + i);
  }
}