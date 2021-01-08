package e8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *   Класс, агрегирующий массив типа Customer.
 *   Найти и вывести:
 *   a) список покупателей в алфавитном порядке;
 *   b) список покупателей, у которых номер кредитной карточки находится в 
 * заданном интервале
 * @author aabyodj
 */
public final class Customers {
    /** Массив покупателей */
    private final SCustomer[] customers;
    
    /** Наибольшая ширина поля id */
    private final int maxIdWidth;
    /** Наибольшая длина ФИО */
    private final int maxFullNameWidth;
    /** Наибольшая ширина поля адреса */
    private final int maxAddressWidth;
    
    /** 
     * @param customers Массив покупателей
     * @param maxIdWidth Наибольшая ширина поля id
     * @param maxFullNameWidth Наибольшая длина ФИО
     * @param maxAddressWidth Наибольшая ширина поля адреса
     */
    private Customers(SCustomer[] customers, 
                      int maxIdWidth, 
                      int maxFullNameWidth, 
                      int maxAddressWidth) {
        this.customers = customers;
        this.maxIdWidth = maxIdWidth;
        this.maxFullNameWidth = maxFullNameWidth;
        this.maxAddressWidth = maxAddressWidth;
    }
    
    /**
     * Прочитать массив покупателей из файла. Если ничего прочитать не удалось,
     * вернуть null.
     * @param fileName Имя файла
     * @return Экземпляр класса Customers, содержащий прочитанный массив
     * @throws FileNotFoundException
     */
    public static Customers read(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner in = new Scanner(file);
        var result = new LinkedList<SCustomer>();
        int maxIdWidth = 0; //Наибольшая ширина поля id
        int maxFNWidth = 0; //Наибольшая длина ФИО
        int maxAddrWidth = 0; //Наибольшая ширина поля адреса
        while (in.hasNextLine()) {
            String line = in.nextLine();
            SCustomer customer = SCustomer.parse(line);
            if (customer != null) {
                result.add(customer);
                //Ширина поля id
                int idWidth = (int)Math.log10(customer.getId()) + 1;
                if (idWidth > maxIdWidth) 
                    maxIdWidth = idWidth;
                //Длина ФИО
                if (customer.fullName.length() > maxFNWidth)
                    maxFNWidth = customer.fullName.length();
                //Ширина поля адреса
                if (customer.getAddress().length() > maxAddrWidth)
                    maxAddrWidth = customer.getAddress().length();
            }
        }
        if (result.isEmpty()) return null;
        return new Customers(result.toArray(new SCustomer[result.size()]), 
                maxIdWidth, maxFNWidth, maxAddrWidth);
    } 
    
    /**
     * Создать копию текущего класса, в которой массив покупателей отсортирован
     * по алфавиту
     * @return
     */
    public Customers alphabetical() {
        Customers result = new Customers(customers,
                maxIdWidth, maxFullNameWidth, maxAddressWidth);
        Arrays.sort(result.customers, 
                (var c1, var c2) -> c1.fullName.compareTo(c2.fullName));
        return result;
    } 
    
    /**
     * Найти покупателей, номера карт которых попадают в заданный диапазон, и
     * поместить их в новый экземпляр класса Customers. Если ничего не найдено,
     * вернуть null.
     * @param min Нижняя граница диапазона
     * @param max Верхняя граница диапазона
     * @return
     */
    public Customers cardRange(long min, long max) {
        var result = new LinkedList<SCustomer>();
        int maxIdWidth = 0; //Наибольшая ширина поля id. Поле maxIdWidth здесь гарантированно не понадобится
        int maxFNWidth = 0; //Наибольшая длина ФИО
        int maxAddrWidth = 0; //Наибольшая ширина поля адреса
        for (var customer: customers) {
            long card = customer.getCard();
            if (card >= min && card <= max) {
                result.add(customer);
                //Ширина поля id
                int idWidth = (int)Math.log10(customer.getId()) + 1;
                if (idWidth > maxIdWidth) 
                    maxIdWidth = idWidth;
                //Длина ФИО
                if (customer.fullName.length() > maxFNWidth)
                    maxFNWidth = customer.fullName.length();
                //Ширина поля адреса
                if (customer.getAddress().length() > maxAddrWidth)
                    maxAddrWidth = customer.getAddress().length();
            }
        }
        if (result.isEmpty()) return null;
        return new Customers(result.toArray(new SCustomer[result.size()]), 
                maxIdWidth, maxFNWidth, maxAddrWidth);
    }

    private static final char CR_SEPARATOR = '+';
    private static final char H_SEPARATOR = '-';
    private static final char V_SEPARATOR = '|';
    private static final char SPACE = ' ';
    
    @Override
    public String toString() {
        //<editor-fold defaultstate="collapsed" desc="Вычисление ширин полей">
        final int CARD_WIDTH = 16; //16 цифр
        final int IBAN_WIDTH = 28; //В Беларуси
        final int MSW = 100; //Пороговая ширина экрана, меньше которой номера счетов выводятся без пробелов
        final int screenWidth = CLI.getScreenWidth(); //Текущая ширина экрана
        //Ширина поля id
        final int idWidth = Math.max(maxIdWidth, 2) + 1; //+1 для сепаратора
        //Варианты ширины поля банковской карты: +3 на пробелы, +1 на сепаратор
        final int cardWidth = (screenWidth >= MSW ? CARD_WIDTH + 3 : CARD_WIDTH) + 1;
        //Варианты ширины поля счёта: +6 на пробелы. Сепаратор не нужен.
        final int ibanWidth = screenWidth >= MSW ? IBAN_WIDTH + 6 : IBAN_WIDTH;
        //Поля неизменной ширины
        final int constWidth = idWidth + cardWidth + ibanWidth; 
        //Максимальная ширина резиновой части таблицы
        final int maxVarWidth = maxFullNameWidth + maxAddressWidth + 2; //+2 для сепараторов
        //Суммарная ширина таблицы
        final int totalWidth = Math.min(constWidth + maxVarWidth, screenWidth);
        //Ширина резиновой части таблицы
        final int varWidth = totalWidth - constWidth;
        //Фактическая ширина ФИО
        final int fullNameWidth = varWidth * (maxFullNameWidth + 1) / maxVarWidth;
        //Фактическая ширина адреса
        final int addressWidth = varWidth - fullNameWidth;
        //</editor-fold>
        String result = repeatChar(H_SEPARATOR, totalWidth) + "\n";
        //<editor-fold defaultstate="collapsed" desc="Шапка таблицы">
        result += alignCenter("id", idWidth - 1) + V_SEPARATOR
                + alignCenter("ФИО", fullNameWidth - 1) + V_SEPARATOR
                + alignCenter("Адрес", addressWidth - 1) + V_SEPARATOR
                + alignCenter("Номер карты", cardWidth - 1) + V_SEPARATOR
                + alignCenter("Номер счёта", ibanWidth) + "\n";
        result += repeatChar(H_SEPARATOR, idWidth - 1) + CR_SEPARATOR
                + repeatChar(H_SEPARATOR, fullNameWidth - 1) + CR_SEPARATOR
                + repeatChar(H_SEPARATOR, addressWidth - 1) + CR_SEPARATOR
                + repeatChar(H_SEPARATOR, cardWidth - 1) + CR_SEPARATOR
                + repeatChar(H_SEPARATOR, ibanWidth) + "\n";
        //</editor-fold>
        //Шаблон форматирования поля id
        final String idFormat = "%" + (idWidth - 1) + "d";
        //Шаблон форматирования поля кредитной карты
        final String cardFormat = "%016d";
        for (var customer: customers) {
            //Поле id
            result += String.format(idFormat, customer.getId()) + V_SEPARATOR;
            //ФИО
            result += alignLeft(customer.fullName, fullNameWidth - 1) + V_SEPARATOR;
            //Адрес
            result += alignLeft(customer.getAddress(), addressWidth - 1) + V_SEPARATOR;
            //Кредитная карта
            result += screenWidth >= MSW ?
                    formatBy4(String.format(cardFormat, customer.getCard())) + V_SEPARATOR :
                    String.format(cardFormat, customer.getCard()) + V_SEPARATOR;
            //Банковский счёт
            result += screenWidth >= MSW ?
                    formatBy4(customer.getIban()) + "\n" :
                    customer.getIban() + "\n";
        }
        result += repeatChar(H_SEPARATOR, totalWidth) + "\n";
        return result.strip();
    }
    
    /**
     * Ограничить длину строки и дописать в конце многоточие. Если строка уже 
     * укладывается в заданный размер - вернуть её без изменений.
     * @param string Исходная строка
     * @param length Ограничение на длину
     * @return Строка не длинее length
     */
    private String trimString(String string, int length) {
        if (string.length() <= length) return string;
        StringBuilder result = new StringBuilder(string);
        result.setLength(length);
        if (length > 3) result.replace(length - 3, length, "...");
        return result.toString();
    }
    
    /**
     * Выравнивание строки влево при заданной ширине. 
     * Если строка короче, чем надо - дополнить справа пробелами; если длинее - 
     * обрезать лишнее и добавить в конце многоточие.
     * @param string Исходная строка
     * @param width Заданная ширина
     * @return
     */
    private String alignLeft(String string, int width) {
        if (string.length() > width) return trimString(string, width);
        StringBuilder result = new StringBuilder(string);
        while (result.length() < width) result.append(SPACE);
        return result.toString();
    }
    
    private String alignCenter(String string, int width) {
        if (string.length() > width) return trimString(string, width);
        int startPos = (width - string.length()) / 2;
        return repeatChar(SPACE, startPos) + string 
                + repeatChar(SPACE, width - startPos - string.length());
    }
    
    /**
     * Добавить пробел после каждого четвёртого символа 
     * @param string
     * @return 
     */
    private String formatBy4(String string) {
        return string.replaceAll(".{4}", "$0 ").strip();
    }
    
    /**
     * Сгенерировать из заданного символа строку заданной длины 
     * @param ch Символ
     * @param length Длина строки
     * @return 
     */
    private String repeatChar(char ch, int length) {
        char[] result = new char[length];
        Arrays.fill(result, ch);
        return new String(result);
    }

    /** 
     * Вспомогательный класс, чтобы при сортировке по алфавиту не вычислять 
     * при каждом сравнении surname + name + patronymic
     */
    private static class SCustomer extends Customer {
        /** Фамилия + имя + отчество */
        public String fullName;
        
        /**
         * Создание объекта и заполнение его информацией из заданной строки
         * @param line 
         */
        public SCustomer(String line) {
            super(line);
            calcFullName();
        }
        
        /**
         * Создать новый объект и заполненить его информацией из заданной 
         * строки, при неудаче вернуть null.
         * @param line
         * @return Объект или null
         */
        public static SCustomer parse(String line) {
            try {
                return new SCustomer(line);                
            } catch (java.util.NoSuchElementException e) {
                return null;
            }
        }
        
        /** Объединение фамилии, имени и отчества в одну строку */
        private void calcFullName() {
            fullName =  (getSurname() + " " + getName() + " " + getPatronymic());
        }

        @Override
        public void setSurname(String surname) {
            super.setSurname(surname);
            calcFullName();
        }

        @Override
        public void setName(String name) {
            super.setName(name);
            calcFullName();
        }

        @Override
        public void setPatronymic(String patronymic) {
            super.setPatronymic(patronymic);
            calcFullName();
        }
    }
}
