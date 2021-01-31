package homelib;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author aabyodj
 */
public class Book {
    final int id;
    final Type type;
    String title;
    Author[] authors;

    static final String A_DELIMITER = "\\s*,\\s*";
    static final Pattern F_DELIMITER = Pattern.compile("\\s*;\\s*");
    
    Book(int id, Type type, String title, Author[] authors) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.authors = authors;
    }

    Book(String line, Library library) {
        Scanner in = new Scanner(line).useDelimiter(F_DELIMITER);
        id = in.nextInt();
        title = in.next();
        int[] authIds = toIntegers(in.next().split(A_DELIMITER));
        authors = library.getAuthors(authIds);
        type = Type.valueOf(in.next());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('(').append(id);
        result.append(type.shortName).append(") ");
        result.append(authorsToString()).append(" - ");
        result.append(title);
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

        public static final String UNKNOWN = "неизвестный автор";
        
        Author(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        Author(String line) {
            Scanner in = new Scanner(line).useDelimiter(F_DELIMITER);
            id = in.nextInt();
            name = in.next();
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
        
        public String[] getFullNames() {
            Type[] values = values();
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].fullName;
            }
            return result;
        }
    }
}
