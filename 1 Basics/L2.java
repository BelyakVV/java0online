import java.util.Scanner;

public class L2 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("a=");
    double a = in.nextFloat();
    System.out.print("b=");
    double b = in.nextFloat();
    System.out.print("c=");
    double c = in.nextFloat();

    double Result = (b + Math.sqrt(b*b + 4*a*c)) / (2*a)
      - a*a*a*c + Math.sqrt(b);

    System.out.println("Result=" + Result);
  }
}