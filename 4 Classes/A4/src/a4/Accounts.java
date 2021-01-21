package a4;

import cli.Table;
import static cli.Table.Align.RIGHT;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *   Счета. Клиент может иметь несколько счетов в банке. Учитывать возможность 
 * блокировки/разблокировки счета. Реализовать поиск и сортировку счетов. 
 * Вычисление общей суммы по счетам. Вычисление суммы по всем счетам, имеющим 
 * положительный и отрицательный балансы отдельно.
 * @author aabyodj
 */
public class Accounts {
    /** Счета */
    private Account[] accounts;
    /** Клиенты */
    private Customers customers;
    
    /** 
     * Загрузить счета и базу клиентов из файлов
     * @param accFileName
     * @param custFileName
     * @throws java.io.FileNotFoundException 
     */
    public Accounts(String accFileName, String custFileName) throws FileNotFoundException {
        accounts = loadAccounts(accFileName);
        customers = new Customers(custFileName);
    }

    private Accounts(Account[] accounts, Customers customers) {
        this.accounts = accounts;
        this.customers = customers;
    }
    
    /**
     * Получить id клиента по индексу в массиве клиентов
     * @param i
     * @return 
     */
    public int getCustomerId(int i) {
        return customers.customers[i].id;
    }
    
    /**
     * Получить массив имён клиентов
     * @return 
     */
    public String[] getCustomersNames() {
        return customers.getNames();
    }
    
    /**
     * Посчитать сумму по счетам с отрицательным балансом
     * @return 
     */
    public long getNegSum() {
        long result = 0;
        for (var account: accounts) {
            if (account.balance < 0) result += account.balance;
        }
        return result;
    }
    
    /**
     * Посчитать сумму по счетам с положительным балансом
     * @return 
     */
    public long getPosSum() {
        long result = 0;
        for (var account: accounts) {
            if (account.balance > 0) result += account.balance;
        }
        return result;
    }
    
    /**
     * Посчитать сумму балансов всех счетов
     * @return 
     */
    public long getSum() {
        long result = 0;
        for (var account: accounts) {
            result += account.balance;
        }
        return result;
    }
    
    /**
     * Получить статус блокировки счёта
     * @param id
     * @return true в случае блокировки
     */
    public boolean isBlocked(int id) {
        for (var account : accounts) {
            if (account.id == id) return account.blocked;
        }
        throw new NoSuchElementException();
    }
    
    /**
     * Установить статус блокировки счёта
     * @param id
     * @param blocked true для блокировки
     */
    public void setBlocked(int id, boolean blocked) {
        for (Account account : accounts) {
            if (account.id == id) {
                account.blocked = blocked;
                return;
            }
        }
        throw new NoSuchElementException("Счёт с таким номером отсутствует");
    }
    
    /**
     * Получить количество счетов в этом экземпляре агрегатора
     * @return 
     */
    public int size() {
        return accounts.length;
    }
    
    /** Шапка таблицы для вывода счетов */
    static final String[] HEAD = {"Счёт", "Клиент", "Баланс", "Блок"};
    
    /**
     * Вывод массива счетов в виде таблицы 
     * @return 
     */
    @Override
    public String toString() {  
        if (accounts.length == 0) return "Ничего не найдено.";
        //В таблице только шапка
        Table result = new Table(HEAD);
        //Номера счетов выровнять вправо
        result.getCol(0).setAlign(RIGHT);
        //Баланс выровнять вправо
        result.getCol(2).setAlign(RIGHT);
        //Сумма балансов всех счетов
        long sum = 0;
        //Сумма отрицательных балансов
        long negSum = 0;
        //Сумма положительных балансов
        long posSum = 0;
        for (var account : accounts) {
            //Очередная строка таблицы
            String[] row = new String[HEAD.length];
            //Номер счёта
            row[0] = Integer.toString(account.id);
            //Имя клиента
            row[1] = customers.getName(account.owner);
            //Баланс
            row[2] = formatBalance(account.balance);
            //Признак блокировки счёта
            row[3] = account.blocked ? "да" : "нет";
            result.addRow(row);
            //Сумма балансов всех счетов
            sum += account.balance;
            if (account.balance > 0) {
                //Положительный баланс
                posSum += account.balance;
            } else {
                //Отрицательный или нулевой
                negSum += account.balance;
            }
        }
        //Горизонтальный разделитель перед строкой "Итого"
        result.addHSeparator();
        //Строка "Итого"
        result.addRow(new String[]{"", "Итого", formatBalance(sum), ""});
        //Ячейку со словом "Итого" выровнять вправо
        result.getLastRow().getCell(1).setAlign(RIGHT);
        //Если суммы и положительных, и отрицательных балансов
        if ((posSum != 0) && (negSum != 0)) {
            result.addRow(new String[]{"", "Из них:", "", ""});
            result.addRow(new String[]{"", "отрицательных", formatBalance(negSum), ""});
            //Ячейку со словом "отрицательных" выровнять вправо
            result.getLastRow().getCell(1).setAlign(RIGHT);
            result.addRow(new String[]{"", "положительных", formatBalance(posSum), ""});
            //Ячейку со словом "положительных" выровнять вправо
            result.getLastRow().getCell(1).setAlign(RIGHT);
        }
        return result.toString();
    }
        
    /**
     * Отформатировать баланс счёта в виде 12 345 678,90
     * @param balance
     * @return 
     */
    private String formatBalance(long balance) {
        return String.format("%,d,%02d", balance / 100, Math.abs(balance % 100));
    }
    
    /**
     * Найти заблокированные счета
     * @return Новый экземпляр агрегатора с результатом
     */
    public Accounts onlyBlocked() {
        //До создания массива накапливаем результат в связном списке
        LinkedList<Account> result = new LinkedList<>();
        for (var account: accounts) {
            if (account.blocked)
                result.add(account);
        }
        //Вернуть результат внутри нового экземпляра агрегатора
        return new Accounts(result.toArray(new Account[0]), customers);
    }
    
    /**
     * Найти счета заданного клиента по его id
     * @param id
     * @return Новый экземпляр агрегатора с результатом
     */
    public Accounts onlyCustomer(int id) {
        LinkedList<Account> result = new LinkedList<>();
        for (var account: accounts) {
            if (account.owner == id)
                result.add(account);
        }
        //Вернуть внутри нового экземпляра агрегатора результат, а 
        return new Accounts(result.toArray(new Account[0]), 
                customers.onlyCustomer(id));
    }
    
    /**
     * Найти счета с отрицательным балансом и вернуть внутри нового экземпляра 
     * агрегатора
     * @return Новый экземпляр агрегатора с результатом
     */
    public Accounts onlyNegative() {
        LinkedList<Account> result = new LinkedList<>();
        for (var account: accounts) {
            if (account.balance < 0)
                result.add(account);
        }
        return new Accounts(result.toArray(new Account[0]), customers);
    }
    
    /**
     * Отсортировать счета по остаточному балансу внутри текущего экземпляра 
     * агрегатора и вернуть его
     * @return Текущий экземпляр агрегатора
     */
    public Accounts sortByBalance() {
        Arrays.sort(accounts, (a1, a2) -> Long.compare(a1.balance, a2.balance));
        return this;
    }
    
    /**
     * Отсортировать счета по номерам (id) внутри текущего экземпляра агрегатора 
     * и вернуть его
     * @return Текущий экземпляр агрегатора
     */
    public Accounts sortById() {
        Arrays.sort(accounts, (a1, a2) -> Integer.compare(a1.id, a2.id));
        return this;
    }
    
    /**
     * Отсортировать счета по именам владельцев по алфавиту, а в случае 
     * равенства - по номеру счёта (id). Сортировка производится внутри текущего 
     * экземпляра агрегатора.
     * @return Текущий экземпляр агрегатора
     */
    public Accounts sortByOwnerThenId() {
        if (accounts.length == 0) return this;
        //Заполнить вспомагательные индексы
        for (var account: accounts)
            //Номер в отсортированном по алфавиту массиве
            account.ownerIndex = customers.getIndex(account.owner);
        Arrays.sort(accounts, (a1, a2) -> {
            //Сначала сравниваем по алфавиту
            if (a1.ownerIndex != a2.ownerIndex) 
                return Integer.compare(a1.ownerIndex, a2.ownerIndex);
            //При равенстве - сравниваем по номеру счёта
            return Integer.compare(a1.id, a2.id);
        });
        return this;
    }
    
    /**
     * Загрузить массив счетов из файла
     * @param fileName Имя файла
     * @return
     * @throws FileNotFoundException 
     */
    private Account[] loadAccounts(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner in = new Scanner(file);
        //До создания массива результат накапливается в связном списке
        LinkedList<Account> result = new LinkedList<>();
        while (in.hasNextLine()) {
            try {
                result.add(new Account(in.nextLine()));
            } catch (NoSuchElementException e) {
                //Строки с ошибками пропускаем
            }
        }
        return result.toArray(new Account[0]);
    }
    
    /** Разделитель полей в файле */
    private static final Pattern DELIMITER = Pattern.compile("\\s*;\\s*");
    
    /** Банковский счёт */
    private class Account {
        /** Номер счёта */
        public final int id;
        /** id владельца счёта в списке клиентов */
        public final int owner;
        /** Индекс владельца счёта в массиве, упорядоченном по алфавиту */
        int ownerIndex = -1;
        /** Баланс */
        long balance = 0;
        /** Признак блокировки счёта */
        boolean blocked = false;

        /**
         * Создать экземпляр банковского счёта из строки 
         * @param string 
         * @throws NoSuchElementException 
         */
        Account(String string) throws NoSuchElementException {
            Scanner in = new Scanner(string).useDelimiter(DELIMITER);
            //Обязательные поля
            this.id = in.nextInt();
            this.owner = in.nextInt();
            //Опциональные поля
            if (in.hasNextLong()) balance = in.nextLong();
            if (in.hasNextBoolean()) blocked = in.nextBoolean();
        }
       
        /**
         * Получить текущий баланс счёта
         * @return 
         */
        public long getBalance() {
            return balance;
        }
        
        /**
         * Изменить баланс счёта на заданную сумму
         * @param delta 
         */
        public void add(long delta) {
            if (blocked) throw new IllegalStateException("Счёт заблокирован");
            balance += delta;
        }
        
        /**
         * Получить статус блокировки счёта
         * @return 
         */
        public boolean isBlocked() {
            return blocked;
        }
        
        /**
         * Установить статус блокировки счёта
         * @param block 
         */
        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }
    }

    /** Справочник клиентов */
    class Customers {
        /** Массив клиентов */
        Customer[] customers;
        
        /** 
         * Создать справочник клиентов из одного элемента
         * @param customer 
         */
        Customers(Customer customer) {
            this.customers = new Customer[]{customer};
        }
        
        /**
         * Создать справочник клиентов из заданного массива
         * @param customers 
         */
        Customers(Customer[] customers) {
            this.customers = customers;
        }
        
        /**
         * Загрузить справочник клиентов из файла
         * @param fileName Имя файла
         * @throws FileNotFoundException 
         */
        Customers(String fileName) throws FileNotFoundException {
            File file = new File(fileName);
            Scanner in = new Scanner(file);
            //До создания массива результат накапливается в связном списке
            LinkedList<Customer> result = new LinkedList<>();
            while (in.hasNextLine()) {
                try {
                    result.add(new Customer(in.nextLine()));
                } catch (NoSuchElementException e) {
                    //Строки с ошибками пропускаем
                }
            }
            //Отсортировать справочник по алфавиту
            result.sort((c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
            customers = result.toArray(new Customer[0]);
        }
        
        /**
         * Найти клиента по id
         * @param id
         * @return 
         */
        Customer getCustomer(int id) {
            for (var customer: customers) {
                if (customer.id == id) return customer;
            }
            throw new NoSuchElementException("Клиент с таким id не найден");            
        }
        
        /**
         * Получить индекс клиента в массиве по id
         * @param id
         * @return 
         */
        int getIndex(int id) {
            for (int i = 0; i < customers.length; i++) {
                if (customers[i].id == id) return i;
            }
            throw new NoSuchElementException("Клиент с таким id не найден");  
        }
        
        /**
         * Получить имя клиента по его id
         * @param id
         * @return 
         */
        public String getName(int id) {
            return getCustomer(id).name;
        }
        
        /**
         * Получить массив с именами клиентов
         * @return 
         */
        public String[] getNames() {
            String[] result = new String[customers.length];
            for (int i = 0; i < customers.length; i++) {
                result[i] = customers[i].name;
            }
            return result;
        }
        
        /**
         * Найти клиента по id и поместить его в новый пустой экземпляр 
         * справочника
         * @param id
         * @return 
         */
        public Customers onlyCustomer(int id) {
            return new Customers(getCustomer(id));
        }
    }

    /** Клиент - элемент справочника */
    class Customer {
        /** Идентификатор клиента */
        public final int id;
        /** Имя клиента */
        String name;

        /**
         * Создать элемент справочника из строки
         * @param string
         * @throws NoSuchElementException 
         */
        Customer(String string) throws NoSuchElementException {
            Scanner in = new Scanner(string).useDelimiter(DELIMITER);
            this.id = in.nextInt();
            this.name = in.next();
        }
        
        /**
         * Получить имя клиента
         * @return 
         */
        public String getName() {
            return name;
        }
    }    
}
