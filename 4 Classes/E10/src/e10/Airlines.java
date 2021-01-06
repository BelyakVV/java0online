package e10;

import cli.CLI;
import cli.Table;
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
    final RefArray destinations;
    /** Справочник типов самолёта */
    private RefArray planes;

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

    static final String HEAD = "Рейс" + CLI.V_SEPARATOR 
                             + "Пункт назначения" + CLI.V_SEPARATOR
                             + "Вылет" + CLI.V_SEPARATOR
                             + "Дни недели" + CLI.V_SEPARATOR
                             + "Самолёт";
    @Override
    public String toString() {
        if (airlines.length == 0) return "Ничего не найдено";
//        String[] rows = new String[airlines.length + 1];
//        rows[0] = HEAD;
//        for (int i = 0; i < airlines.length; i++) {
//            rows[i + 1] = formatAirline(airlines[i].toString());
//        }
//        CLI.formatTable(rows);
//        var result = new StringBuilder();
//        for (var row: rows) {
//            result.append(row).append('\n');
//        }
//        return result.toString();
        Table result = new Table(HEAD);
        for (var airline: airlines) {
            result.addRow(formatAirline(airline.toString()));
            //System.out.println(airline.toString());
        }
        result.getCol(0).setAlign(CLI.Align.RIGHT);
        return result.toString();
    }
    
//    private String formatAirline(String str) {
//        String[] fields = str.split(CLI.T_R_SPLIT_REGEX);
//        StringBuilder result = new StringBuilder(fields[0]);
//        result.append(CLI.V_SEPARATOR).append(
//                destinations.getById(Integer.decode(fields[1])));
//        result.append(CLI.V_SEPARATOR).append(
//                formatTime(Integer.decode(fields[2])));
//        result.append(CLI.V_SEPARATOR).append(
//                formatDays(Integer.decode(fields[3])));
//        result.append(CLI.V_SEPARATOR).append(
//                planes.getById(Integer.decode(fields[4])));
//        return result.toString();
//    }
    
    private String[] formatAirline(String str) {
        String[] result = str.split(CLI.T_R_SPLIT_REGEX);
        result[1] = destinations.getById(Integer.decode(result[1]));
        result[2] = formatTime(Integer.decode(result[2]));
        result[3] = formatDays(Integer.decode(result[3]));
        result[4] = planes.getById(Integer.decode(result[4]));
        return result;
    }

    private String formatTime(int time) {
        time = time / 60; //Игнорируем секунды
        String result = String.format("%02d", time % 60);
        result = String.format("%02d", time / 60) + ":" + result;
        return result;
    }

    private String formatDays(int days) {
        StringBuilder result = new StringBuilder();
        for (var day: Day.values()) {
            if ((days & day.bitWeight) != 0) 
                result.append(day.shortName).append(' ');
        }
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        } else {
            result.append("нет");
        }
        return result.toString();
    }
    
    public Airlines afterTime(int time) {
        LinkedList<Airline> result = new LinkedList<>();
        for (var airline: airlines) {
            if (airline.getDeparture() > time) result.add(airline);
        }
        return new Airlines(result.toArray(new Airline[0]), destinations, planes);
    }
    
    public Airlines onlyDestination(int destination) {
        LinkedList<Airline> result = new LinkedList<>();
        for (var airline: airlines) {
            if (airline.getDestination() == destination) result.add(airline);
        }
        return new Airlines(result.toArray(new Airline[0]), destinations, planes);
    }
    
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
    static class RefArray {
        /** Массив элементов справочника */
        private final RefItem[] refArray;
        /** Значение по умолчанию для отсутствующих в справочнике элементов */
        private final String defaultValue;
        
        /**
         * Чтение справочника из файла
         * @param fileName Имя файла
         * @param defaultValue Значение по умолчанию для отсутствующих в 
         * справочнике элементов
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
            refArray = result.toArray(new RefItem[0]);
            //Отсортировать по алфавиту
            Arrays.sort(refArray, 
                    (var i1, var i2) -> i1.value.compareToIgnoreCase(i2.value));
            this.defaultValue = defaultValue;
        }
        
        /**
         * Поиск в справочнике по id, в случае неудачи возврат defaultValue
         * @param id
         * @return 
         */
        String getById(int id) {
            for (var item: refArray) {
                if (item.id == id) return item.value;
            }
            return defaultValue;
        } 
        
        int getId(int i) {
            return (i < 0 || i >= refArray.length) ? -1 : refArray[i].id;
        }
        
        String[] toStrings() {
            String[] result = new String[refArray.length + 1];
            for (int i = 0; i < refArray.length; i++) {
                result[i] = refArray[i].value;
            }
            result[refArray.length] = defaultValue;
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
        public RefItem(String line) {
            Scanner in = new Scanner(line).useDelimiter("\\s*;\\s*");
            this.id = in.nextInt();
            this.value = in.next();
        }        
    }
    
    enum Day {
        MONDAY  ("Понедельник", "пн"), 
        TUESDAY ("Вторник",     "вт"), 
        WEDNSDAY("Среда",       "ср"), 
        THURSDAY("Четверг",     "чт"), 
        FRIDAY  ("Пятница",     "пт"), 
        SATURDAY("Суббота",     "сб"), 
        SUNDAY  ("Воскресенье", "вс");
        
        /** битовое поле, где понедельник = 1, воскресенье = 64 */
        final int bitWeight;
        /** Название дня недели */
        final String name;
        final String shortName;

        private Day(String name, String shortName) {
            this.bitWeight = 1 << this.ordinal();
            this.name = name;
            this.shortName = shortName;
        }

        @Override
        public String toString() {
            return name;
        }   
        
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
