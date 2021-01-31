package homelib;

import cli.Table;
import homelib.Book.Author;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author aabyodj
 */
public final class Library {
    List<Book> books;
    int nextBookId = 0;
    String booksFN = "books.txt";
    List<Author> authors;
    int nextAuthorId = 0;
    String authorsFN = "authors.txt";
    
    static final String BR = System.lineSeparator();
    
    public Library() {
        loadOrCreateAll();
    }
    
    public Library(String booksFN, String authorsFN) {
        this.authorsFN = authorsFN;
        this.booksFN = booksFN;
        loadOrCreateAll();
    }
    
    public List<String> getAuthorsList() {
        List<String> result = new LinkedList<>();
        for (var author: authors) {
            result.add(author.name);
        }
        return result;
    }
    
    public Library addAuthor(String name) {
        Author author = new Author(nextAuthorId++, name);
        authors.add(author);
        return this;
    }
    
    public Library addBook(Book.Type type, String title, int[] authIndexes) {
        Book book = new Book(nextBookId++, type, title, authorsByIndexes(authIndexes));
        books.add(book);
        return this;
    }
    
    static final String[] HEAD = new String[]{"id", "Название", "Автор"};
    
    @Override
    public String toString() {
        if (books.size() < 1) return "Ничего нет.";
        Table result = new Table(HEAD);
        result.getCol(0).setAlign(Table.Align.RIGHT);
        for (var book: books) {
            result.addRow(new String[]{
                Integer.toString(book.id),
                book.title,
                book.authorsToString()
            });
        }
        return result.toString();
    }
    
    Author getAuthor(int id) {
        for (var author: authors) {
            if (author.id == id) return author;
        }
        return null;
    }
    
    Author[] getAuthors(int[] ids) {
        Author[] result = new Author[ids.length];
        for (int i = 0; i < ids.length; i++) {
            result[i] = getAuthor(ids[i]);
        }
        return result;
    }

    Author[] authorsByIndexes(int[] indexes) {
        Author[] result = new Author[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = authors.get(i);
        }
        return result;
    }
    
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
                Author author = new Author(file.nextLine());
                if (author.id >= nextAuthorId) nextAuthorId = author.id + 1;
                authors.add(author);
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
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
    }
}
