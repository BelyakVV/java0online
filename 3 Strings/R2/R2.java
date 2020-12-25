/*
 * Строки. Регулярные выражения. Задача №2.
 *
 *   Дана строка, содержащая xml-документ. Напишите анализатор, позволяющий
 * последовательно возвращать содержимое узлов xml-документа и его тип
 * (открывающий тег, закрывающий тег, содержимое тега, тег без тела).
 * Пользоваться готовыми парсерами XML для решения данной задачи нельзя.
 **
 *   Часть строки до первого и после последнего тегов отбрасывается
 *   Вход: файл "R2.xml"
 *   Выход: содержимое узлов xml-документа
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class R2 {
    static final String FILE_NAME = "R2.xml";

    public static void main(String[] args) throws IOException {
        final String XML_TAG_PATTERN = "<(\\s*[^/]+?)>|</(.+?)>|<([^/]+?)/>";
        Pattern tagSearch = Pattern.compile(XML_TAG_PATTERN, Pattern.DOTALL);
        String xmlDoc = fileRead(FILE_NAME).strip(); //Чтение документа из файла
        Matcher m = tagSearch.matcher(xmlDoc);
        int pos = 0; //Позиция поиска в строке
        while (m.find()) { //Пока есть теги
            if (pos > 0) { //Содержимое строки до первого тега отбрасывается
                //Содержимое тега
                String inner = xmlDoc.substring(pos, m.start());
                if (!inner.isBlank()) {
                    inner = inner.strip().replaceAll("\\s+", " ");
                    System.out.println("Содержимое тега: " + inner);
                }
            }
            pos = m.end();
            if (m.group(1) != null) {
                System.out.print("Открывающий тег: ");
            } else if (m.group(2) != null) {
                System.out.print("Закрывающий тег: ");
            } else {
                System.out.print("Тег без тела: ");
            }
            System.out.println(m.group(0).replaceAll("\\s+", " "));
        }
    }

    /** Чтение текстового файла целиком в одну строковую переменную */
    static String fileRead(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        String result = "";
        while (line != null) {
            result += line + "\n";
            line = reader.readLine();
        }
        return result;
    }
}