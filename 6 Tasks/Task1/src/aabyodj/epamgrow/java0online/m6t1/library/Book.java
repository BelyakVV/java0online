package aabyodj.epamgrow.java0online.m6t1.library;

import static aabyodj.datafiles.Const.ARR_DLM;
import static aabyodj.datafiles.Const.ARR_DLM_REGEX;
import static aabyodj.datafiles.Const.FLD_DLM;
import static aabyodj.datafiles.Const.FLD_DLM_PTTRN;
import static aabyodj.epamgrow.java0online.m6t1.Util.toIntegers;
import java.util.Scanner;

/**
 * Книга
 * @author aabyodj
 */
public class Book {
    
    /** Идентификатор книги в каталоге */
    public final int id;    //TODO: проследить за уникальностью
    
    /** Тип книги: бумажная или электронная */
    public final Type type;
    
    /** Название книги */
    String title;
    
    /** Авторы книги */
    Author[] authors;   //TODO сделать списком
    
    /** Каталог, к которому относится книга */
    Library library;
    
    static final String EMPTY_TITLE = "Название не может быть пустым";
    static final String NOT_FOUND = "Нет такой книги";
    
    Book(int id, Type type, String title, Author[] authors, Library library) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.authors = authors;
        this.library = library;
    }

    /** 
     * Прочитать книгу из строки файла
     * @param line Исходная строка
     * @param library Каталог, к которому относится книга
     */
    Book(String line, Library library) {
        Scanner in = new Scanner(line).useDelimiter(FLD_DLM_PTTRN);
        this.library = library;
        id = in.nextInt();
        title = in.next();
        
        //Идентификаторы авторов в каталоге
        int[] authIds = toIntegers(in.next().split(ARR_DLM_REGEX));
        authors = library.getAuthors(authIds);
        
        //Бумажная или электронная книга
        type = Type.valueOf(in.next().strip());
    }

    /**
     * Задать единственного автора книги
     * @param author Новый автор
     */
    public void setAuthor(Author author) {
        setAuthors(new Author[]{author});
    }
    
    /**
     * Задать авторов книги
     * @param authors Авторы книги
     */
    public void setAuthors(Author[] authors) {
        this.authors = authors;
        library.booksChanged = true;
    }
    
    /**
     * Получить название книги
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Задать название книги
     * @param newTitle Новое название
     */
    public void setTitle(String newTitle) {
        if (newTitle.isBlank()) {
            throw new IllegalArgumentException(EMPTY_TITLE);
        }
        newTitle = newTitle.strip();
        if (newTitle.contentEquals(this.title)) 
            return;
        this.title = newTitle;
        library.booksChanged = true;
    }

    /**
     * Отформатировать сведения о книге в строку для записи в текстовый файл
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(id).append(FLD_DLM);
        result.append(title).append(FLD_DLM);
        for (var author: authors) {
            result.append(author.id).append(ARR_DLM);
        }
        result.setCharAt(result.length() - 1, FLD_DLM);
        result.append(type.name());
        return result.toString();
    }

    /**
     * Получить авторов книги в одну строку через запятую
     * @return 
     */
    public String authorsToString() {
        if (authors == null) return Author.UNKNOWN;
        if (authors.length < 1) return Author.UNKNOWN;
        StringBuilder result = new StringBuilder();
        for (var author: authors) {
            if (author != null) {
                result.append(author.name);
            } else {
                result.append(Author.UNKNOWN);
            }
            result.append(", ");
        }
        result.setLength(result.length() - 2);  //Удалить последнюю запятую и пробел
        return result.toString();
    }
    
    /**
     * Автор книги
     */
    public static final class Author implements Comparable {
        
        /** Идентификатор автора */
        public final int id;    //TODO: проследить за уникальностью
        
        /** Неверный идентификатор автора */
        public static final int INVALID_ID = -1;  
        
        /** Имя автора */
        String name;
        
        /** Агрегатор авторов и книг */
        Library library;
              
        /** Имя неизвестного автора */
        public static final String UNKNOWN = "неизвестный автор";
        
        static final String DUPLICATE = "Такой автор уже есть";
        static final String EMPTY_NAME = "Имя автора не может быть пустым";
        static final String NOT_FOUND = "Нет такого автора";
        
        Author(int id, String name, Library library) {
            this.id = id;
            setName(name);
            this.library = library;
        }
        
        /**
         * Прочитать автора из строки файла
         * @param line Исходная строка
         * @param library Агрегатор авторов и книг
         */
        Author(String line, Library library) {
            Scanner in = new Scanner(line).useDelimiter(FLD_DLM_PTTRN);
            id = in.nextInt();
            name = in.next();
            this.library = library;
        }

        /**
         * Получить имя автора
         * @return 
         */
        public String getName() {
            return name;
        }

        /**
         * Задать имя автора
         * @param newName Новое имя
         */
        public void setName(String newName) {
            if (newName.isBlank())
                throw new IllegalArgumentException(EMPTY_NAME);
            newName = newName.strip();
            if (newName.contentEquals(this.name)) return;
            if (library.getAuthorId(name) != INVALID_ID)
                throw new IllegalStateException(DUPLICATE);
            this.name = newName;
            library.authorsChanged = true;            
        }
        
        /**
         * Отформатировать сведения об авторе для записи в текстовый файл
         * @return Строка для записи в файл
         */
        @Override
        public String toString() {
            return Integer.toString(id) + FLD_DLM + name;
        }

        @Override
        public int compareTo(Object o) {
            return name.compareToIgnoreCase(((Author) o).name);
        }
    }
    
    /**
     * Тип книги: бумажная или электронная
     */
    public enum Type {        
        PAPER("бумажная книга", "бум"), 
        ELECTRONIC("электронная книга", "эл");
        
        /** Полное наименование данного типа книги */
        public final String fullName;
        
        /** Краткое ноименование данного типа книги */
        public final String shortName;
        
        Type(String fullName, String shortName) {
            this.fullName = fullName;
            this.shortName = shortName;
        }
        
        /**
         * Получить массив полных наименований типов книг
         * @return 
         */
        public static String[] getFullNames() {
            Type[] values = values();
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].fullName;
            }
            return result;
        }
    }
}
