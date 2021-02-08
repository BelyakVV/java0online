package homelib;

import cli.Table;
import homelib.Book.Author;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author aabyodj
 */
public final class Library {
    List<Book> books;
    int nextBookId = 0;
    String booksFN;
    boolean booksChanged = true;
    List<Author> authors;
    int nextAuthorId = 0;
    String authorsFN;
    boolean authorsChanged = true;
    
    static final String BR = System.lineSeparator();

    Library(List<Book> books, String booksFN, List<Author> authors, String authorsFN) {
        this.books = books;
        this.booksFN = booksFN;
        this.authors = authors;
        this.authorsFN = authorsFN;
    }
    
    public Library(String booksFN, String authorsFN) {
        this.authorsFN = authorsFN;
        this.booksFN = booksFN;
        loadOrCreateAll();
    }
    
    public Author getAuthor(int id) {
        for (var author: authors) {
            if (author.id == id) return author;
        }
        throw new NoSuchElementException(Author.NOT_FOUND);
    }
    
    public int getAuthIndex(int id) {
        int i = 0;
        for (var author: authors) {
            if (author.id == id) return i;
            i++;
        }
        throw new NoSuchElementException(Author.NOT_FOUND);
    }
    
    public List<String> getAuthorsList() {
        List<String> result = new LinkedList<>();
        for (var author: authors) {
            result.add(author.name);
        }
        return result;
    }
    
    public String getAuthorsTable() {
        if (authors.isEmpty()) return "Пусто.";
        Table result = new Table(new String[]{"id", "Полное имя"});
        result.getCol(0).setAlign(Table.Align.RIGHT);
        for (var author: authors) {
            result.addRow(new String[]{
                Integer.toString(author.id),
                author.name
            });
        }
        return result.toString();
    }
    
    public Author addAuthor(String name) {
        if (name.isBlank())
            throw new IllegalArgumentException(Author.EMPTY_NAME);
        if (getAuthorId(name) != Author.INVALID_ID) 
            throw new IllegalStateException(Author.DUPLICATE);
        int id = nextAuthorId;
        Author author = new Author(id, name, this);
        if (!authors.add(author)) 
            throw new RuntimeException("Не удалось добавить автора в список");
        nextAuthorId++; 
        authors.sort((a1, a2) -> a1.name.compareToIgnoreCase(a2.name));
        authorsChanged = true;
        return author;
    }
    
//    Library addBook(Book book) {
//        if (!books.add(book))
//            throw new RuntimeException("Не удалось добавить книгу в библиотеку");
//        booksChanged = true;
//        return this;
//    }
    
    public Book addBook(Book.Type type, String title, Author[] authors) {
        int id = nextBookId;
        Book book = new Book(id, type, title, authors, this);
        if (books.add(book)) {
            nextBookId++;
            booksChanged = true;
            return book;
        }
        throw new RuntimeException("Не удалось добавить книгу в библиотеку");
    }
    
    public Book addBook(Book.Type type, String title, int[] authIndexes) {
        return addBook(type, title, authorsByIndexes(authIndexes));
    }
    
    public Book getBook(int id) {
        for (var book: books) {
            if (book.id == id)
                return book;
        }
        throw new NoSuchElementException(Book.NOT_FOUND);
    }
    
    public Book removeBook(int id) {
        int i = 0;
        for (var book: books) {
            if (book.id == id) {
                if (books.remove(i) != null) {
                    booksChanged = true;
                    return book;                        
                }
            }
            i++;
        }
        throw new NoSuchElementException(Book.NOT_FOUND);
    }
    
    static final String[] HEAD = new String[]{"id", "Тип", "Название", "Автор"};
    
    @Override
    public String toString() {
        if (books.isEmpty()) return "Ничего нет.";
        Table result = new Table(HEAD);
        result.getCol(0).setAlign(Table.Align.RIGHT);
        for (var book: books) {
            result.addRow(new String[]{
                Integer.toString(book.id),
                book.type.shortName,
                book.title,
                book.authorsToString()
            });
        }
        return result.toString();
    }
    
    Author[] getAuthors(int[] ids) {
        Author[] result = new Author[ids.length];
        for (int i = 0; i < ids.length; i++) {
            result[i] = getAuthor(ids[i]);
        }
        return result;
    }
    
    int getAuthorId(String name) {
        if (name.isBlank()) return Author.INVALID_ID;
        for (var author: authors) {
            if (author.name.equalsIgnoreCase(name))
                return author.id;
        }
        return Author.INVALID_ID;
    }

    public Author[] authorsByIndexes(int[] indexes) {
        Author[] result = new Author[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = authors.get(indexes[i]);
        }
        return result;
    }
    
    public Library onlyAuthor(Author subject) {
        if (subject == null) return this;
        List<Book> result = new LinkedList<>();
        int authId = subject.id;
        for (var book: books) {
            for (var author: book.authors) {
                if (author.id == authId) {
                    result.add(book);
                    break;
                }
            }
        }
        return new Library(result, booksFN, authors, authorsFN);
    }
    
    public Library onlyTitle(String subject) {
        if (subject.isBlank()) return this;
        subject = subject.strip().toUpperCase();
        List<Book> result = new LinkedList<>();
        for (var book: books) {
            if (book.title.toUpperCase().contains(subject)) {
                result.add(book);
            }
        }
        return new Library(result, booksFN, authors, authorsFN);
    }

    static final char ARR_DLM = ',';
    static final String ARR_DLM_REGEX = "\\s*" + ARR_DLM + "\\s*";
    static final char FLD_DLM = ';';
    static final Pattern FLD_DLM_PTTRN = Pattern.compile("\\s*" + FLD_DLM + "\\s*");
    
    void loadOrCreateAll() {
        try {
            loadAuthors();
        } catch (FileNotFoundException fileNotFoundException) {
            authors = new LinkedList<>();
        }
        try {
            loadBooks();
        } catch (FileNotFoundException fileNotFoundException) {
            books = new LinkedList<>();
        }
    }
    
    void loadAuthors() throws FileNotFoundException {
        Scanner file = new Scanner(new File(authorsFN));
        authors = new LinkedList<>();
        nextAuthorId = 0;
        while (file.hasNextLine()) {
            try {
                Author author = new Author(file.nextLine(), this);
                if (author.id >= nextAuthorId) nextAuthorId = author.id + 1;
                authors.add(author);
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        authors.sort((a1, a2) -> a1.name.compareToIgnoreCase(a2.name));
        authorsChanged = false;
    }
    
    void loadBooks() throws FileNotFoundException {
        Scanner file = new Scanner(new File(booksFN));
        books = new LinkedList<>();
        nextBookId = 0;
        while (file.hasNextLine()) {
            try {
                Book book = new Book(file.nextLine(), this);
                if (book.id >= nextBookId) nextBookId = book.id + 1;
                books.add(book);
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        booksChanged = false;
    }

    public void saveAuthors() throws IOException {
        if (!authorsChanged) return;
        File file = new File(authorsFN);
        if (!file.exists()) file.createNewFile();
        try (PrintStream out = new PrintStream(file)) {
            for (var author: authors) {
                out.println(author);
            }
            authorsChanged = false;
        }
    }

    public void saveBooks() throws IOException {
        if (!booksChanged) return;
        File file = new File(booksFN);
        if (!file.exists()) file.createNewFile();
        try (PrintStream out = new PrintStream(file)) {
            for (var book: books) {
                out.println(book);
            }
            booksChanged = false;
        }
        
    }
}
