package tour;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Элемент справочника стран
 * @author aabyodj
 */
class Country {
    /** Идентификатор страны */
    final int id;
    /** Текстовое наименование страны */
    final String name;
    
    /**
     * Создать элемент справочника стран из строки текста
     * @param line Входная строка
     * @param pattern Разделитель полей в строке
     */
    Country(String line, Pattern pattern) {
        Scanner in = new Scanner(line).useDelimiter(pattern);
        //Идентификатор
        id = in.nextInt();
        //Текстовое наименование
        name = in.next();
    }
    
    @Override
    public String toString() {
        return name;
    }
}
