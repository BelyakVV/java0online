package tour;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Пункт назначения. Элемент справочника пунктов назначения.
 * @author aabyodj
 */
class Destination {
    /** Идентификатор пункта назначения */
    final int id;
    /** Текстовое наименование пункта назначения */
    final String name;
    /** Страна, в которой находится пункт назначения */
    final Country country;
    
    /**
     * Создать элемент справочника пунктов назначения из строки текста
     * @param line Входная строка
     * @param delimiter Разделитель полей в строке
     * @param tours Агрегатор справочников стран и пунктов назначения
     */
    Destination(String line, Pattern delimiter, Tours tours) {
        Scanner in = new Scanner(line).useDelimiter(delimiter);
        //Идентификатор
        id = in.nextInt();
        //Текстовое наименование
        name = in.next();
        //Получить страну по её id
        country = tours.getCountry(in.nextInt());
    }

    /**
     * Получить пункт назначения и страну в текстовом виде в следующем формате:
     * Пункт назначения (Страна)
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name);
        result.append(" (").append(country).append(')');
        return result.toString();
    }
}
