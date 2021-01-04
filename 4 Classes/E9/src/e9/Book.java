package e9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   Создать класс Book. Определить конструкторы, set- и get- методы и метод 
 * toString().
 *   Book: id, название, автор(ы), издательство, год издания, количество 
 * страниц, цена, тип переплета.
 * @author aabyodj
 */
public class Book {
    private static final String LINE_PARSE_DELIMITER = "\\s*;\\s*";
    /** Идентификатор */
    private int id;
    /** Название */
    private String title;
    /** Авторы */
    private int[] authors;
    /** Издатель */
    private int publisher;
    /** Год издания */
    private int year;
    /** Количество страниц */
    private int pages;
    /** Цена */
    private int price;
    /** Тип обложки */
    private int cover;
    
    /** Справочник авторов, издательств, типов обложки */
    private Reference reference;

    /**
     * Создать объект и заполнить его данными из строки
     * @param line Строка с данными о книге
     * @param reference Справочник авторов, издательств и типов обложки
     */
    private Book(String line, Reference reference) {
        Scanner in = new Scanner(line).useDelimiter(LINE_PARSE_DELIMITER);
        this.id = in.nextInt();
        this.title = in.next();
        this.authors = in.hasNext() ? parseIntArray(in.next()): new int[]{-1};
        this.publisher = in.hasNextInt() ? in.nextInt() : -1;
        this.year = in.hasNextInt() ? in.nextInt() : 0;
        this.pages = in.hasNextInt() ? in.nextInt() : 0;
        this.price = in.hasNextInt() ? in.nextInt() : 0;
        this.cover = in.hasNextInt() ? in.nextInt(): -1;
        this.reference = reference;
    }

    /**
     * Получить идентификатор книги
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Установить идентификатор книги
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Получить название книги
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Установить название книги
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Получить массив id авторов книги
     * @return
     */
    public int[] getAuthors() {
        return authors.clone();
    }

    /**
     * Установить массив id авторов книги
     * @param authors
     */
    public void setAuthors(int[] authors) {
        this.authors = authors.clone();
    }

    /**
     * Получить id издательства
     * @return 
     */
    public int getPublisher() {
        return publisher;
    }

    /**
     * Установить id издательства
     * @param publisher 
     */
    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }

    /**
     * Получить год издания книги
     * @return 
     */
    public int getYear() {
        return year;
    }

    /**
     * Установить год издания книги
     * @param year 
     */
    public void setYear(int year) {
        this.year = year;
    }

    /** 
     * Получить количество страниц
     * @return 
     */
    public int getPages() {
        return pages;
    }

    /**
     * Установить количество страниц
     * @param pages 
     */
    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * Получить цену книги
     * @return 
     */
    public int getPrice() {
        return price;
    }

    /**
     * Установить цену книги
     * @param price 
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Получить id типа обложки
     * @return 
     */
    public int getCover() {
        return cover;
    }

    /**
     * Установить id типа обложки
     * @param cover 
     */
    public void setCover(int cover) {
        this.cover = cover;
    }

    /**
     * Получить справочник авторов, издательств и типов обложки
     * @return 
     */
    public Reference getReference() {
        return reference;
    }

    /**
     * Задать справочник авторов, издательств и типов обложки
     * @param reference 
     */
    public void setReference(Reference reference) {
        this.reference = reference;
    }

    /** 
     * Форматирование книги как элемента библиографического списка
     * @return 
     */
    @Override
    public String toString() {
        String result = "(" + id + ") ";
        for (int i = 0; i < authors.length - 1; i++) {
            result += reference.authors.idToName(authors[i]) + ", ";
        }
        result += reference.authors.idToName(authors[authors.length - 1]) + " "
                + title + ". — " 
                + reference.publishers.idToName(publisher) + ", "
                + year + ". — "
                + pages + " с.";
        return result;
    }
    
    /**
     * Созданть объект книгу из строки, при неудаче вернуть null
     * @param line
     * @param reference
     * @return 
     */
    public static Book parse(String line, Reference reference) {
        try {
            return new Book(line, reference);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Разбор строки на массив int[]
     * @param line
     * @return 
     */
    private int[] parseIntArray(String line) {
        /** Элемент односвязного списка */
        class ListItem {
            int value;
            ListItem prev;
            ListItem(int value, ListItem prev) {
                this.value = value;
                this.prev = prev;
            }
        }
        //Поиск десятичных натуральных чисел
        Matcher mInt = (Pattern.compile("[0-9]+")).matcher(line);
        ListItem last = null;
        int c = 0; //Количество найденных чисел
        while (mInt.find()) {
            last = new ListItem(Integer.parseInt(mInt.group()), last);
            c++;
        }
        if (c < 1) 
            throw new java.lang.IllegalArgumentException("Входная строка не содержит чисел");
        int[] result = new int[c];
        while (last != null) {
            result[--c] = last.value;
            last = last.prev;
        }
        return result;
    }
    
    /** Элемент справочника: автор, издательство или тип обложки */
    public static class RefItem {
        /** Идентификатор элемента */
        public final int id;
        /** Наименование элемента */
        public final String name;

        /**
         * Создать новый элемент справочника
         * @param id Идентификатор
         * @param name Наименование
         */
        public RefItem(int id, String name) {
            this.id = id;
            this.name = name;
        }   

        /**
         * Создать новый элемент справочника из строки. 
         * @param line
         */
        public RefItem(String line) {
            Scanner in = new Scanner(line).useDelimiter(LINE_PARSE_DELIMITER);
            this.id = in.nextInt();
            this.name = in.next();
        }
        
        /**
         * Создать новый элемент справочника из строки, в случае ошибки вернуть
         * null.
         * @param line
         * @return Новый элемент или null
         */
        public static RefItem parse(String line) {
            try {
                return new RefItem(line);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Класс-агрегатор справочного массива: поиск элемента по id, возврат id по 
     * индексу в массиве, возврат элементов в виде массива строк, сортировка в
     * алфавитном порядке.
     */
    public static class RefArray {
        /** Справочный массив */
        private final RefItem[] refArray;
        /** Возвращаемое значение при неудачном поиске */
        private final String defaultName;

        private RefArray(RefItem[] refArray, String defaultName) {
            this.refArray = refArray;
            this.defaultName = defaultName;
        }

        /**
         * Заполнение справочного массива данными из файла
         * @param fileName Имя файла
         * @param defaultName Значение по умолчанию, возвращаемое при неудачном
         * поиске в справочнике
         * @throws FileNotFoundException 
         */
        public RefArray(String fileName, String defaultName) 
                throws FileNotFoundException {
            File file = new File(fileName);
            Scanner in = new Scanner(file);
            var result = new LinkedList<RefItem>();
            while (in.hasNextLine()) {
                RefItem item = RefItem.parse(in.nextLine());
                if (item != null) result.add(item);
            }
            this.refArray = result.toArray(new RefItem[0]);
            this.defaultName = defaultName;
        }
        
        /**
         * Возврат id по индексу в справочном массиве
         * @param i Индекс в массиве
         * @return id справочного элемента
         */
        public int getId(int i) {
            try {
                return refArray[i].id;
            } catch (Exception e) {
                return -1;
            }
        }
        
        /**
         * Возврат справочного элемента по его id
         * @param id
         * @return
         */
        public String idToName(int id) {
            for (var item: refArray) {
                if (item.id == id) return item.name;
            }
            return defaultName;
        }
        
        /**
         * Преобразование справочного массива в массив строк. Последняя строка
         * в возвращаемом массиве - значение по умолчанию.
         * @return 
         */
        public String[] toStrings() {
            String[] result = new String[refArray.length + 1];
            for (int i = 0; i < refArray.length; i++) 
                result[i] = refArray[i].name;
            result[refArray.length] = defaultName;
            return result;
        }
        
        /**
         * Сортировка справочника по алфавиту и возврат его в новом экземпляре 
         * агрегатора.
         * @return 
         */
        public RefArray alphabetical() {
            RefItem[] result = refArray.clone();
            Arrays.sort(result, (var i1, var i2) -> i1.name.compareTo(i2.name));
            return new RefArray(result, defaultName);
        }
    }

    /** Справочник авторов, издательств, типов обложки */
    public static class Reference {
        /** Авторы */
        public final RefArray authors;
        /** Издательства */
        public final RefArray publishers;
        /** Виды обложки */
        public final RefArray covers;

        /**
         * Чтение из файлов справочников авторов, издательств и видов обложки
         * @param authorsFileName Имя файла авторов
         * @param publishersFileName Имя файла издательств
         * @param coversFileName Имя файла видов обложки
         * @throws java.io.FileNotFoundException
         */
        public Reference(String authorsFileName, 
                         String publishersFileName, 
                         String coversFileName) throws FileNotFoundException {
            this.authors = new RefArray(authorsFileName, "Неизвестный автор").alphabetical();
            this.publishers = new RefArray(publishersFileName, "Неизвестный издатель").alphabetical();
            this.covers = new RefArray(coversFileName, "Неизвестная обложка");
        }
    }
}
