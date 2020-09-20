import java.util.Scanner;

public class L1 {
  public static void main(String[] args) {
    Scanner sin = new Scanner(System.in);

    System.out.print("a=");
    float a = sin.nextFloat();
    System.out.print("b=");
    float b = sin.nextFloat();
    System.out.print("c=");
    float c = sin.nextFloat();

    float z=((a-3)*b/2)+c;
    System.out.println("a=" + a + " b=" + b + " c=" + c + " z=" + z);
  }
}