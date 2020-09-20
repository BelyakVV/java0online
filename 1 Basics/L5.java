import java.util.Scanner;

public class L5 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.print("Введите длительность в секундах: ");
    long T = in.nextLong();

    byte S = (byte) (T % 60);
    T = T / 60;

    byte M = (byte) (T % 60);
    T = T / 60;

    System.out.printf("%dч %02dм %02dс\n", T, M, S);
  }
}