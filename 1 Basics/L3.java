import java.util.Scanner;

public class L3 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("x=");
    double x = in.nextDouble();
    System.out.print("y=");
    double y = in.nextDouble();

    double Result = (Math.sin(x) + Math.cos(y)) / (Math.cos(x) - Math.sin(y))
      * Math.tan(x*y);

    System.out.println("Result=" + Result);
  }
}