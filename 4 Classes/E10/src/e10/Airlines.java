package e10;

import cli.Table;
import static cli.Table.Align.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *   Класс, агрегирующий массив типа Airline.
 *   Найти и вывести:
 *   a) список рейсов для заданного пункта назначения;
 *   b) список рейсов для заданного дня недели;
 *   c) список рейсов для заданного дня недели, время вылета для которых больше 
 * заданного.
 * @author aabyodj
 */
public class Airlines {
    /** Массив авиарейсов */
    private Airline[] airlines;
    
    /** Справочник пунктов назначения */
    public final RefArray destinations;
    /** Справочник типов самолёта */
    public final RefArray planes;

    private Airlines(Airline[] airlines, RefArray destinations, RefArray planes) {
        this.airlines = airlines;
        this.destinations = destinations;
        this.planes = planes;
    }

    /**
     * Загрузка массива авиарейсов из файла
     * @param airlinesFileName Имя файла авиарейсов
     * @param destinationsFileName Имя файла справочника пунктов назначения
     * @param planesFileName Имя файла справочника типов самолётов
     * @throws FileNotFoundException 
     */
    public Airlines(String airlinesFileName, 
                    String destinationsFileName, 
                    String planesFileName) 
            throws FileNotFoundException {
        File file = new File(airlinesFileName);
        Scanner in = new Scanner(file);
        //До создания массива будем помещать элементы в список
        LinkedList<Airline> result = new LinkedList<>();
        while (in.hasNextLine()) {
            try {
                //Пробуем прочитать очередную строку
                result.add(new Airline(in.nextLine()));
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        airlines = result.toArray(new Airline[0]);
        //Отсортировать по времени вылета
        Arrays.sort(airlines, 
                (var i1, var i2) -> Integer.compare(i1.getDeparture(), i2.getDeparture()));
        //Справочник пунктов назначения
        destinations = new RefArray(destinationsFileName, "В закат");
        //Справочник типов самолётов
        planes = new RefArray(planesFileName, "Метла");
    }

    /** Шапка таблицы */
    static final String[] HEAD = {"Рейс", "Пункт назначения", "Вылет",
                                  "Дни недели", "Самолёт"};
    @Override
    public String toString() {
        if (airlines.length == 0) return "Ничего не найдено";
        //Создать пустую таблицу с шапкой
        Table result = new Table(HEAD);
        //Заполнить её
        for (var airline: airlines) {
            result.addRow(formatAirline(airline));
        }
        //Выровнять вправо столбец с номерами рейсов
        result.getCol(0).setAlign(RIGHT);
        return result.toString();
    }
    
    /**
     * Форматирование полей Airline для табличного вывода
     * @param airline
     * @return 
     */
    private String[] formatAirline(Airline airline) {
        return new String[]{
            //Номер рейса
            Integer.toString(airline.getFlight()),
            //Пункт назначения
            destinations.getById(airline.getDestination()),
            //Время вылета
            formatTime(airline.getDeparture()),
            //Дни недели
            formatDays(airline.getDaysOfWeek()),
            //Тип самолёта
            planes.getById(airline.getPlane())};
    }

    /**
     * Преобразование количества секунд после полуночи в часы и минуты
     * @param time
     * @return 
     */
    private String formatTime(int time) {
        time = time / 60; //Игнорируем секунды
        String result = String.format("%02d", time % 60);
        result = String.format("%02d", time / 60) + ":" + result;
        return result;
    }

    /**
     * Преобразование дней недели, заданных в виде битового поля, где 
     * 1 = понедельник, 64 = воскресенье, в перечисление кратких наименований
     * этих дней (например, пн чт вс)
     * @param days Битовое поле
     * @return 
     */
    private String formatDays(int days) {
        StringBuilder result = new StringBuilder();
        //Перечисление всех возможных дней
        for (var day: Day.values()) {
            if ((days & day.bitWeight) != 0) //Этот день есть в поле
                result.append(day.shortName).append(' ');
        }
        if (result.length() < 1) { //Ничего не найдено
            result.append("нет");
        } else { //Что-то нашлось
            //Обрезать последний пробел
            result.setLength(result.length() - 1); 
        }
        return result.toString();
    }
    
    /**
     * Найти рейсы, вылетающие после указанного времени, и вернуть их в новом 
     * экземпляре агрегатора
     * @param time Время вылета
     * @return 
     */
    public Airlines afterTime(int time) {
        LinkedList<Airline> result = new LinkedList<>();
        for (var airline: airlines) {
            if (airline.getDeparture() > time) result.add(airline);
        }
        return new Airlines(result.toArray(new Airline[0]), destinations, planes);
    }
    
    /**
     * Найти рейсы с заданным пунктом назначения и вернуть их в новом экземпляре
     * агрегатора
     * @param destination id пункта назначения
     * @return 
     */
    public Airlines onlyDestination(int destination) {
        if (!destinations.isValid(destination)) 
            return onlyInvalidDestinations();
        LinkedList<Airline> result = new LinkedList<>();
        for (var airline: airlines) {
            if (airline.getDestination() == destination) result.add(airline);
        }
        return new Airlines(result.toArray(new Airline[0]), destinations, planes);
    }
    
    /**
     * Найти рейсы, пункт назначения которых отсутствует в справочнике и вернуть
     * их в новом экземпляре агрегатора
     * @return 
     */
    public Airlines onlyInvalidDestinations() {
        LinkedList<Airline> result = new LinkedList<>();
        for (var airline: airlines) {
            if (!destinations.isValid(airline.getDestination()))
                result.add(airline);
        }
        return new Airlines(result.toArray(new Airline[0]), destinations, planes);
    }
    
    /**
     * Найти рейсы, курсирующие в любой из заданных дней недели, и вернуть их в
     * новом экземпляре агрегатора
     * @param daysOfWeek Дни недели в виде битового поля, где понедельник = 1,
     * воскресенье = 64
     * @return 
     */
    public Airlines onlyDaysOfWeek(int daysOfWeek) {
        LinkedList<Airline> result = new LinkedList<>();
        for (var airline: airlines) {
            if ((airline.getDaysOfWeek() & daysOfWeek) != 0)
                result.add(airline);
        }
        return new Airlines(result.toArray(new Airline[0]), destinations, planes);
    }
    
    /** 
     *    Агрегатор справочного массива с функциями загрузки из файла и поиска 
     * по id.
     */
    public static class RefArray {
        /** Массив элементов справочника */
        private final RefItem[] items;
        /** Значение по умолчанию для отсутствующих в справочнике элементов */
        private final String defaultValue;
        
        /**
         * Чтение справочника из файла
         * @param fileName Имя файла
         * @param defaultValue Значение по умолчанию для отсутствующих в 
         * справочнике элементов
         * @throws java.io.FileNotFoundException
         */
        public RefArray(String fileName, String defaultValue) 
                throws FileNotFoundException {
            File file = new File(fileName);
            Scanner in = new Scanner(file);
            //До создания массива помещаем элементы в список
            LinkedList<RefItem> result = new LinkedList<>();
            while (in.hasNextLine()) {
                try {
                    //Пробуем прочитать очередную строку
                    result.add(new RefItem(in.nextLine()));
                } catch (Exception e) {
                    //Строки с ошибками пропускаем
                }
            }
            items = result.toArray(new RefItem[0]);
            //Отсортировать по алфавиту
            Arrays.sort(items, 
                    (var i1, var i2) -> i1.value.compareToIgnoreCase(i2.value));
            this.defaultValue = defaultValue;
        }
        
        /**
         * Поиск в справочнике по id, в случае неудачи возврат defaultValue
         * @param id
         * @return 
         */
        public String getById(int id) {
            for (var item: items) {
                if (item.id == id) return item.value;
            }
            return defaultValue;
        } 
        
        /**
         * Возврат id по индексу в массиве или -1 в случае выхода за границы
         * массива
         * @param i Индекс в справочном массиве
         * @return 
         */
        public int getId(int i) {
            return (i < 0 || i >= items.length) ? -1 : items[i].id;
        }
        
        /**
         * Проверить, содержится ли в справочнике элемент с данным id
         * @param id
         * @return 
         */
        public boolean isValid(int id) {
            for (var item: items) {
                if (item.id == id) return true;
            }
            return false;
        }
        
        /**
         * Преобразование справочника в массив строк и добавление в него 
         * последним элементом значения по умолчанию
         * @return 
         */
        public String[] toStrings() {
            String[] result = new String[items.length + 1];
            for (int i = 0; i < items.length; i++) {
                result[i] = items[i].value;
            }
            result[items.length] = defaultValue;
            return result;
        }
    }

    /** Элемент справочника */
    private static class RefItem {
        /** Идентификатор */
        final int id;
        /** Значение элемента */
        final String value;

        /**
         * Создание элемента справочника из строки текста
         * @param line 
         */
        RefItem(String line) {
            Scanner in = new Scanner(line).useDelimiter("\\s*;\\s*");
            this.id = in.nextInt();
            this.value = in.next();
        }        
    }
    
    /**
     * Дни недели
     */
    public enum Day {
        MONDAY  ("Понедельник", "пн"), 
        TUESDAY ("Вторник",     "вт"), 
        WEDNSDAY("Среда",       "ср"), 
        THURSDAY("Четверг",     "чт"), 
        FRIDAY  ("Пятница",     "пт"), 
        SATURDAY("Суббота",     "сб"), 
        SUNDAY  ("Воскресенье", "вс");
        
        /** Битовое поле, где понедельник = 1, воскресенье = 64 */
        public final int bitWeight;
        /** Название дня недели */
        public final String name;
        /** Короткое наименование дня недели */
        public final String shortName;

        private Day(String name, String shortName) {
            this.bitWeight = 1 << this.ordinal();
            this.name = name;
            this.shortName = shortName;
        }

        @Override
        public String toString() {
            return name;
        }   
        
        /**
         * Возврат списка дней недели в виде массива строк 
         * @return 
         */
        public static String[] toStrings() {
            Day[] values = Day.values();
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].name;
            }
            return result;
        }
    }
}
