package tour;

import cli.Table;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Агрегатор путёвок, пунктов назначения и фильтра
 * @author aabyodj
 */
public class Tours {
    /** Фильтр путёвок по заданным критериям */
    public final Filter filter;
    
    /** Справочник стран */
    Country[] countries;
    /** Справочник пунктов назначения */
    Destination[] destinations;
    /** Массив путёвок */
    Tour[] tours;
    
    /**
     * Загрузить из файлов путёвки, страны и пункты назначения
     * @param toursFN Имя файла с путёвками
     * @param countriesFN Имя файла со странами
     * @param destFN Имя файла с пунктами назначения
     * @throws FileNotFoundException 
     */
    public Tours(String toursFN, String countriesFN, String destFN) throws FileNotFoundException {
        //Загрузить справочник стран
        countries = loadCountries(countriesFN);
        //Загрузить справочник пунктов назначения
        destinations = loadDestinations(destFN);
        //Загрузить путёвки
        tours = loadTours(toursFN);
        //Создать пустой фильтр
        filter = new Filter(this);
    }

    /**
     * Получить страну по её id
     * @param id
     * @return 
     */
    Country getCountry(int id) {
        for (var country: countries) {
            if (country.id == id) return country;
        }
        throw new NoSuchElementException("Страна id=" + id + " отсутствует в справочнике.");
    }

    /**
     * Получить пункт назначения по его id
     * @param id
     * @return 
     */
    Destination getDestination(int id) {
        for (var destination: destinations) {
            if (destination.id == id) return destination;
        }
        throw new NoSuchElementException("Пункт назначения id=" + id + " отсутствует в справочнике.");
    }
    
    /** Шапка таблицы */
    private static final String[] HEAD = {
        "Пункт назначения", "Тип", "Транспорт", "Питание", "Длительность", "Цена"
    };
    
    /** 
     * Применить фильтр к массиву путёвок и отформатировать результат в виде 
     * таблицы 
     */
    @Override
    public String toString() {
        //Текущие настройки фильтра
        String result = filter.toString() + "\n";
        //Применить фильтр
        var filtered = filter.select();
        if (filtered.size() < 1) {
            return result + "Ничего не найдено.";
        }
        //Создать пустую таблицу с заданной шапкой
        Table table = new Table(HEAD);
        //Выровнять столбец "Длительность" по центру
        table.getCol(4).setAlign(Table.Align.CENTER);
        //Выровнять столбец "Цена" вправо
        table.getLastCol().setAlign(Table.Align.RIGHT);
        for (Tour tour: filtered) {
            //Добавить строку в таблицу
            table.addRow(new String[]{
                //Пункт назначения
                tour.destination.toString(),
                //Тип поездки
                tour.type.value, 
                //Вид транспорта
                tour.transport.value, 
                //Питание
                tour.meal.value, 
                //Длительность
                formatDaysNom(tour.length), 
                //Цена
                formatPrice(tour.price)
            });
        }
        return result + table.toString();
    }

    /**
     * Отформатировать число дней в именительном падеже
     * @param days Число дней
     * @return 
     */
    static String formatDaysNom(int days) {
        int d100 = days % 100;
        int d10 = days % 10;
        if ((d100 > 4 && d100 < 21) || (d10 > 4) || (d10 == 0)) {
            return Integer.toString(days) + " дней";
        }
        if (d10 > 1) {
            return Integer.toString(days) + " дня";
        }
        return Integer.toString(days) + " день";
    }

    /**
     * Отформатировать число дней в родительном падеже
     * @param days Число дней
     * @return 
     */
    static String formatDaysGen(int days) {
        int d100 = days % 100;
        int d10 = days % 10;
        if ((d100 > 1 && d100 < 21) || (d10 > 1)) {
            return Integer.toString(days) + " дней";
        }
        return Integer.toString(days) + " дня";
    }

    /**
     * Отформатировать цену в рублях и копейках
     * @param price Цена, заданная в копейках
     * @return 
     */
    static String formatPrice(int price) {
        return String.format("%,d,%02d", price / 100, price % 100);
    }

    /** Разделитель полей в файле */
    private static final Pattern DELIMITER = Pattern.compile("\\s*;\\s*");
    
    /**
     * Загрузить справочник стран из файла
     * @param fileName Имя файла
     * @return
     * @throws FileNotFoundException 
     */
    private Country[] loadCountries(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName));
        //До создания массива результат хранится здесь
        LinkedList<Country> result = new LinkedList<>();
        while (in.hasNextLine()) {
            try {
                //Попытка прочитать очередную строку
                result.add(new Country(in.nextLine(), DELIMITER));
            } catch (Exception e) {
                //Строки с ошибками пропускаются
            }
        }
        //Сортировка списка стран по алфавиту
        result.sort((c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
        return result.toArray(new Country[0]);
    }

    /**
     * Загрузить справочник пунктов назначения из файла
     * @param fileName Имя файла
     * @return
     * @throws FileNotFoundException 
     */
    private Destination[] loadDestinations(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName));
        //До создания массива результат накапливается в списке
        LinkedList<Destination> result = new LinkedList<>();
        while (in.hasNextLine()) {
            try {
                //Попытка прочитать очередную строку
                result.add(new Destination(in.nextLine(), DELIMITER, this));
            } catch (Exception e) {
                //Строки с ошибками пропускаются
            }
        }
        //Сортировка пунктов назначения по алфавиту
        result.sort((d1, d2) -> d1.name.compareToIgnoreCase(d2.name));
        return result.toArray(new Destination[0]);
    }

    /** 
     * Загрузка массива путёвок из файла
     * @param fileName Имя файла
     * @return
     * @throws FileNotFoundException 
     */
    private Tour[] loadTours(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName));
        //До создания массива результат накапливается в списке
        LinkedList<Tour> result = new LinkedList<>();
        while (in.hasNextLine()) {
            try {
                //Попытка прочитать очередную строку
                result.add(new Tour(in.nextLine(), DELIMITER, this));
            } catch (Exception e) {
                //Строки с ошибками пропускаются
            }
        }
        //Сортировка массива путёвок по возрастанию цены
        result.sort((t1, t2) -> Integer.compare(t1.price, t2.price));
        return result.toArray(new Tour[0]);
    }
}
