package homelib;

import static homelib.Library.ARR_DLM;
import java.util.Scanner;
import static homelib.Library.ARR_DLM_REGEX;
import static homelib.Library.FLD_DLM;
import static homelib.Library.FLD_DLM_PTTRN;

/**
 *
 * @author aabyodj
 */
public class Book {
    final int id;
    final Type type;
    String title;
    Author[] authors;
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

    Book(String line, Library library) {
        Scanner in = new Scanner(line).useDelimiter(FLD_DLM_PTTRN);
        id = in.nextInt();
        title = in.next();
        int[] authIds = toIntegers(in.next().split(ARR_DLM_REGEX));
        authors = library.getAuthors(authIds);
        type = Type.valueOf(in.next());
        this.library = library;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
        library.booksChanged = true;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.isBlank())
            throw new IllegalArgumentException(EMPTY_TITLE);
        title = title.strip();
        if (title.contentEquals(this.title)) return;
        this.title = title;
        library.booksChanged = true;
    }

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
        result.setLength(result.length() - 2);
        return result.toString();
    }
    
    static int[] toIntegers(String[] strings) {
        int[] result = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }
        return result;
    }
    
    public static class Author {
        final int id;
        String name;
        Library library;
        
        public static final int INVALID_ID = -1;        
        public static final String UNKNOWN = "неизвестный автор";
        static final String DUPLICATE = "Такой автор уже есть";
        static final String EMPTY_NAME = "Имя автора не может быть пустым";
        static final String NOT_FOUND = "Нет такого автора";
        
        Author(int id, String name, Library library) {
            this.id = id;
            this.name = name;
            this.library = library;
        }
        
        Author(String line, Library library) {
            Scanner in = new Scanner(line).useDelimiter(FLD_DLM_PTTRN);
            id = in.nextInt();
            name = in.next();
            this.library = library;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (name.isBlank())
                throw new IllegalArgumentException(EMPTY_NAME);
            name = name.strip();
            if (name.contentEquals(this.name)) return;
            this.name = name;
            library.authorsChanged = true;            
        }
        
        @Override
        public String toString() {
            return Integer.toString(id) + FLD_DLM + name;
        }
    }
    
    public enum Type {
        PAPER("бумажная книга", "бум"), 
        ELECTRONIC("электронная книга", "эл");
        
        public final String fullName;
        public final String shortName;
        
        Type(String fullName, String shortName) {
            this.fullName = fullName;
            this.shortName = shortName;
        }
        
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
