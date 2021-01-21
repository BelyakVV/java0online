package tour;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Туристическая путёвка, а также базовый класс для фильтра путёвок
 * @author aabyodj
 */
class Tour {
    /** Тип путёвки: отдых, лечение, шоппинг и т.д. */
    Type type;
    /** Пункт назначения */
    Destination destination;
    /** Вид транспорта */
    Transport transport;
    /** Питание */
    Meal meal;
    /** Длительность поездки в днях. Для фильтра - минимальная длительность. */
    int length;
    /** Цена в копейках. Для фильтра - минимальная цена. */
    int price;

    /** Пустой конструктор для возможности создания пустого фильтра */
    Tour() {        
    }

    /**
     * Создать путёвку из строки текста
     * @param line Строка
     * @param delimiter Разделитель полей
     * @param tours Агрегатор справочников стран и пунктов назначения
     */
    Tour(String line, Pattern delimiter, Tours tours) {
        Scanner in = new Scanner(line).useDelimiter(delimiter);
        //Тип путёвки
        type = Type.valueOf(in.next());
        //Пункт назначения
        destination = tours.getDestination(in.nextInt());
        //Вид транспорта
        transport = Transport.valueOf(in.next());
        //Питание
        meal = Meal.valueOf(in.next());
        //Длительность
        length = in.nextInt();
        //Цена
        price = in.nextInt();
    }    
}
