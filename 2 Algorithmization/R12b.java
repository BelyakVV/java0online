/*
 *   Даны натуральные числа К и N. Написать метод(методы) формирования массива
 * А, элементами которого являются числа, сумма цифр которых равна К и которые
 * не большее N.
 **
 *   Решение простым перебором
 */



import java.util.Scanner;


public class R12b {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        //Максимально возможное k для n типа int (1 999 999 999) = 81
        byte k = in.nextByte();
        //для n = Integer.MAX_VALUE будет перебирать оооочень долго
        int n = in.nextInt();
        for (int i = (int)Math.pow(9, (double)k / 9); i < n; i++) {
            if (digitSum(i) == k) System.out.print(i + " ");
        }
        System.out.println();
    }


    static byte digitSum(int n) {
        byte result = 0;
        while (n > 0) {
            result += (byte)(n % 10);
            n = n / 10;
        }
        return result;
    }
}