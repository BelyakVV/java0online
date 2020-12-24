/*
 * Строки. Регулярные выражения. Задача №2.
 *
 *   Дана строка, содержащая xml-документ. Напишите анализатор, позволяющий
 * последовательно возвращать содержимое узлов xml-документа и его тип
 * (открывающий тег, закрывающий тег, содержимое тега, тег без тела).
 * Пользоваться готовыми парсерами XML для решения данной задачи нельзя.
 **
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
        String xmlDoc = fileRead(FILE_NAME); //Чтение документа из файла
        XMLNode root = new XMLNode(xmlDoc); //Разбор
        System.out.println(root.toString()); //Вывод
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

/** Рекурсивный XML-парсер */
class XMLNode {
    static final String XML_TAG_PATTERN = "<(.+?)(\\s.+?)?>(.*?)</\\1>|<(.+?)/>";
    static Pattern tagSearch = Pattern.compile(XML_TAG_PATTERN, Pattern.DOTALL);

    /** Рекурсивное преобразование строки в дерево XML */
    XMLNode(String xmlString) {
        xmlString = xmlString.strip();
        Matcher m = tagSearch.matcher(xmlString);
        if (m.find()) { //В переданной строке есть XML-тег
            name = m.group(1);
            if (name != null) { //Имеется закрывающий тег
                if (m.group(2) != null) params = m.group(2);
                if (m.group(3) != null) { //Внутри текущего тега что-то есть
                    hasInner = true;
                    firstChild = new XMLNode(m.group(3));
                    if (firstChild.name.isBlank() 
                            && firstChild.nextSibling == null) {
                        content = firstChild.content; //В содержимом нет XML-тегов
                        firstChild = null;
                        if (content.isBlank()) {
                            hasInner = false;
                            content = "";
                        }
                    } 
                }
                xmlString = xmlString.substring(m.end()); //Остаток строки               
                if (!xmlString.isBlank()) { //Текущий тег занял не всю строку
                    nextSibling = new XMLNode(xmlString);
                }
            } else { //Это одиночный тег
                name = m.group(4);
            }
        } else { //В переданной строке нет XML-тегов, только текстовое содержимое
            content = xmlString;
            hasInner = true;
        }
    }
    
    /** Рекурсивный вывод содержимого дерева XML */
    @Override
    public String toString() {
        return getOpeningTag() + getInner() + getClosingTag();
    }
    
    /** Вывод открывающего тега (если он есть) для текущего узла */
    public String getOpeningTag() {
        if (name.isBlank()) return "";
        String result;
        if (hasInner) {
            result = "Открывающий тег: ";
        } else {
            result = "Тег без тела: ";
        }
        result += "<" + name;
        if (!params.isBlank()) result += params;
        if (!hasInner) result += "/";
        return result + ">\n";
    }
    
    /** Вывод содержимого текущего узла */
    public String getInner() {
        if (!hasInner) return ""; //Пустой узел            
        String result = "";
        XMLNode child = firstChild;
        if (child != null) { //Имеется поддерево
            while (child != null) {
                result += child.toString();
                child = child.nextSibling;
            }
        } else { //Поддерева нет, только текстовое содержимое
            result = "Содержимое тега: " + content + "\n";
        }
        return result;
    }
    
    /** Вывод закрывающего тега (если он нужен) для текущего узла */
    public String getClosingTag() {
        if (name.isBlank() || !hasInner) {
            return ""; //Если это одиночный тег либо только текстовое содержимое
        } else {
            return "Закрывающий тег: </" + name + ">\n";
        }        
    }
    
    /** Имя тега: <name params>content</name> */ 
    private String name = "";
    /** Параметры тега: <name params>content</name> */
    private String params = "";
    /** Текстовое содержимое тега: <name params>content</name> */
    private String content = "";
    /** Первый потомок */
    private XMLNode firstChild;
    /** Следующий сестринский элемент */
    private XMLNode nextSibling;
    /** Признак наличия поддерева и/или текстового содержимого */
    private boolean hasInner;
}