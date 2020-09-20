import java.util.Scanner;

public class L6 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    byte[] width = {-1, 4, 4, 4, 4, 2, 2, 2, 2, -1, -1, -1};

    System.out.print("x=");
    byte x = in.nextByte();
    System.out.print("y=");
    byte y = in.nextByte();

    boolean Result = Math.abs(x) <= width[y + 4];

    System.out.println(Result);
  }
}