package a4;

import cli.CLI;
import cli.CLI.Option;
import java.io.FileNotFoundException;

/**
 *   Счета. Клиент может иметь несколько счетов в банке. Учитывать возможность 
 * блокировки/разблокировки счета. Реализовать поиск и сортировку счетов. 
 * Вычисление общей суммы по счетам. Вычисление суммы по всем счетам, имеющим 
 * положительный и отрицательный балансы отдельно.
 * @author aabyodj
 */
public class A4 {
    /** Счета */
    static Accounts accounts;
    
    /** Интерфейс командной строки */
    static CLI cli = new CLI(new Option[]{
        new Option("Вывести все счета", A4::printAll),
        new Option("Вывести счета заданного клиента", A4::printCustomer),
        new Option("Вывести счета с отрицательным балансом", A4::printNegative),
        new Option("Вывести заблокированные счета", A4::printBlocked),
        new Option("Заблокировать счёт", A4::blockAccount),
        new Option("Разблокировать счёт", A4::unBlockAccount),
        new Option("Выход", ()-> System.exit(0))
    });

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        accounts = new Accounts("accounts.txt", "customers.txt");
        cli.run();
    }
    
    /** Вывести все счета */
    static void printAll() {
        //Отсортировать по номеру счёта и вывести
        System.out.println(accounts.sortById());
    }
    
    /** Вывести счета с отрицательным балансом */
    static void printNegative() {
        //Взять отрицательные, отсортироать по размеру остатка и вывести
        System.out.println(accounts.onlyNegative().sortByBalance());
    }
    
    /** Вывести счета заданного клиента */
    static void printCustomer() {
        //Меню выбора клиента
        var menu = CLI.buildMenu(accounts.getCustomersNames());
        //id выбранного клиента
        int customer = accounts.getCustomerId(cli.getChoice(menu));
        //Найти принадлежащие указанному клиенту, отсортировать по номеру счёта и вывести
        System.out.println(accounts.onlyCustomer(customer).sortById());
    }
    
    /** Вывести заблокированные счета */
    static void printBlocked() {
        //Найти заблокированные, отсортировать по имени владельца и вывести
        System.out.println(accounts.onlyBlocked().sortByOwnerThenId());
    }
    
    /** Заблокировать счёт */
    static void blockAccount() {
        int account = cli.getInt("Введите номер счёта");
        try {
            accounts.setBlocked(account, true);
            System.out.println("Счёт № " + account + " заблокирован.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /** Разблокировать счёт */
    static void unBlockAccount() {
        int account = cli.getInt("Введите номер счёта");
        try {
            accounts.setBlocked(account, false);
            System.out.println("Счёт № " + account + " разблокирован.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
