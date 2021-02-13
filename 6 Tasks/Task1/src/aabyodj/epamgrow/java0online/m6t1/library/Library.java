package aabyodj.epamgrow.java0online.m6t1.library;

import aabyodj.console.Table;
import static aabyodj.epamgrow.java0online.m6t1.Util.createIfNeeded;
import aabyodj.epamgrow.java0online.m6t1.library.Book.Author;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Агрегатор списков книг и авторов
 * @author aabyodj
 */
public final class Library {
    
    /** Список книг */
    List<Book> books;
    
    /** Следующий свободный идентификатор книги */
    int nextBookId = 0;
    
    /** Файл книг */
    File booksFile;
    
    /** Признак того, что следует обновить файл книг */
    boolean booksChanged = true;
    
    /** Список авторов */
    List<Author> authors;
    
    /** Следующий свободный идентификатор автора */
    int nextAuthorId = 0;
    
    /** Файл авторов */
    File authorsFile;
    
    /** Признак того, что следует обновить файл авторов */
    boolean authorsChanged = true;

    Library(List<Book> books, File booksFile, List<Author> authors, File authorsFile) {
        this.books = books;
        this.booksFile = booksFile;
        this.authors = authors;
        this.authorsFile = authorsFile;
    }
    
    /**
     * Прочитать библиотеку из файлов книг и авторов. Если прочитать 
     * не удалось - создать пустую библиотеку.
     * @param booksFN Имя файла книг
     * @param authorsFN Имя файла авторов
     */
    public Library(String booksFN, String authorsFN) {
        this.authorsFile = new File(authorsFN).getAbsoluteFile();
        this.booksFile = new File(booksFN).getAbsoluteFile();
        loadOrCreateAll();
    }
    
    /**
     * Получить автора по его идентификатору
     * @param id Идентификатор автора
     * @return 
     */
    public Author getAuthor(int id) {
        for (var author: authors) {
            if (author.id == id) return author;
        }
        throw new NoSuchElementException(Author.NOT_FOUND);
    }
    
    /**
     * Получить индекс автора в списке по его идентификатору
     * @param id Идентификатор автора
     * @return Индекс автора в списке
     */
    public int getAuthIndex(int id) {
        int i = 0;
        for (var author: authors) {
            if (author.id == id) return i;
            i++;
        }
        throw new NoSuchElementException(Author.NOT_FOUND);
    }
    
    /**
     * Получить список имён авторов
     * @return 
     */
    public List<String> getAuthorsList() {
        List<String> result = new LinkedList<>();
        for (var author: authors) {
            result.add(author.name);
        }
        return result;
    }
    
    //Вывод для пустой таблицы
    static final String EMPTY_TABLE = "Пусто";
    
    /**
     * Получить сведения обо всех авторах в виде текстовой таблицы
     * @return 
     */
    public String getAuthorsTable() {
        if (authors.isEmpty()) return EMPTY_TABLE;
        
        //Создать пустую таблицу с шапкой
        Table result = new Table(new String[]{"id", "Полное имя"});
        
        //Задать выравнивание столбца "id" вправо
        result.getCol(0).setAlign(Table.Align.RIGHT);
        
        for (var author: authors) {
            result.addRow(new String[]{
                Integer.toString(author.id),
                author.name
            });
        }
        return result.toString();
    }
    
    /**
     * Добавить нового автора в каталог
     * @param name Имя нового автора
     * @return Новый автор
     */
    public Author addAuthor(String name) {
        int id = nextAuthorId;
        Author author = new Author(id, name, this);
        if (!authors.add(author)) 
            throw new RuntimeException("Не удалось добавить автора в каталог");
        nextAuthorId++; 
        authors.sort(Author::compareTo);
        authorsChanged = true;
        return author;
    }
    
    /**
     * Добавить новую книгу в каталог
     * @param type бумажная или электронная уеига
     * @param title Название книги
     * @param authors Авторы книги
     * @return Добавленная книга
     */
    public Book addBook(Book.Type type, String title, Author[] authors) {
        int id = nextBookId;
        Book book = new Book(id, type, title, authors, this);
        if (!books.add(book))
            throw new RuntimeException("Не удалось добавить книгу в каталог");
        nextBookId++;
        booksChanged = true;
        return book;
    }
    
    /**
     * Добавить новую книгу в каталог
     * @param type бумажная или электронная уеига
     * @param title Название книги
     * @param authIndexes индексы авторов в списке
     * @return 
     */
    public Book addBook(Book.Type type, String title, int[] authIndexes) {
        return addBook(type, title, authorsByIndexes(authIndexes));
    }
    
    /**
     * Получить книгу по её идентификатору
     * @param id Идентификатор книги
     * @return 
     */
    public Book getBook(int id) {
        for (var book: books) {
            if (book.id == id)
                return book;
        }
        throw new NoSuchElementException(Book.NOT_FOUND);
    }
    
    /**
     * Удалить книгу из каталога
     * @param id Идентификатор книги
     * @return Удалённая книга
     */
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
    
    //Шапка таблицы книг
    static final String[] HEAD = new String[]{"id", "Тип", "Название", "Автор"};
    
    /**
     * Получить сведения о книгах в виде текстовой таблицы
     * @return 
     */
    @Override
    public String toString() {
        if (books.isEmpty()) return EMPTY_TABLE;
        
        //Создать таблицу с шапкой
        Table result = new Table(HEAD);
        
        //Задать выравнивание для столбца "id" вправо
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
    
    /**
     * Получить авторов по их идентификаторам
     * @param ids Идентификаторы авторов
     * @return 
     */
    Author[] getAuthors(int[] ids) {
        Author[] result = new Author[ids.length];
        for (int i = 0; i < ids.length; i++) {
            result[i] = getAuthor(ids[i]);
        }
        return result;
    }
    
    /**
     * Получить автора по его индексу в каталоге
     * @param i Индекс автора
     * @return 
     */
    public Author getAuthorByIndex(int i) {
        return authors.get(i);
    }
    
    /**
     * Получить идентификатор автора по его имени
     * @param name Имя автора
     * @return Идентификатор автора или INVALID_ID если автор не найден
     */
    int getAuthorId(String name) {
        if (name.isBlank()) return Author.INVALID_ID;
        for (var author: authors) {
            if (author.name.equalsIgnoreCase(name))
                return author.id;
        }
        return Author.INVALID_ID;
    }

    /**
     * Получить авторов по их индексам в каталоге
     * @param indexes Индексы авторов
     * @return 
     */
    public Author[] authorsByIndexes(int[] indexes) {
        Author[] result = new Author[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = authors.get(indexes[i]);
        }
        return result;
    }
    
    /**
     * Выбрать из каталога книги заданного автора и вернуть их в новом 
     * экземпляре агрегатора. Если предмет поиска не задан - вернуть текущий
     * экземпляр агрегатора.
     * @param subject Предмет поиска
     * @return Новый экземпляр агрегатора с результатами поиска
     */
    public Library selectByAuthor(Author subject) {
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
        return new Library(result, booksFile, authors, authorsFile);
    }
    
    /**
     * Выбрать из каталога книги, название которых содержит заданную подстроку,
     * и вернуть их в новом экземпляре агрегатора. Если предмет поиска не 
     * задан - вернуть текущий экземпляр агрегатора.
     * @param subject Предмет поиска
     * @return Новый экземпляр агрегатора с результатами поиска
     */
    public Library selectByTitle(String subject) {
        if (subject == null) return this;
        if (subject.isBlank()) return this;
        subject = subject.strip().toUpperCase();
        List<Book> result = new LinkedList<>();
        for (var book: books) {
            if (book.title.toUpperCase().contains(subject)) {
                result.add(book);
            }
        }
        return new Library(result, booksFile, authors, authorsFile);
    }
    
    /**
     * Загрузить из файлов списки авторов и книг. Если какой-либо файл 
     * отсутствует - создать соответствующий пустой список.
     */
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
    
    /**
     * Загрузить из файла список авторов
     * @throws FileNotFoundException 
     */
    void loadAuthors() throws FileNotFoundException {
        Scanner in = new Scanner(authorsFile);
        authors = new LinkedList<>();
        nextAuthorId = 0;
        while (in.hasNextLine()) {
            try {
                Author author = new Author(in.nextLine(), this);
                if (author.id >= nextAuthorId) nextAuthorId = author.id + 1;
                authors.add(author);
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        authors.sort(Author::compareTo);
        authorsChanged = false;
    }
    
    /**
     * Загрузить из файла список книг
     * @throws FileNotFoundException 
     */
    void loadBooks() throws FileNotFoundException {
        Scanner in = new Scanner(booksFile);
        books = new LinkedList<>();
        nextBookId = 0;
        while (in.hasNextLine()) {
            try {
                Book book = new Book(in.nextLine(), this);
                if (book.id >= nextBookId) nextBookId = book.id + 1;
                books.add(book);
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        booksChanged = false;
    }

    /**
     * Сохранить список авторов в файл
     * @throws IOException 
     */
    public void saveAuthors() throws IOException {
        if (!authorsChanged) return;
        createIfNeeded(authorsFile);
        try (PrintStream out = new PrintStream(authorsFile)) {
            for (var author: authors) {
                out.println(author);
            }
            authorsChanged = false;
        }
    }

    /**
     * Сохранить список книг в файл
     * @throws IOException 
     */
    public void saveBooks() throws IOException {
        if (!booksChanged) return;
        createIfNeeded(booksFile);
        try (PrintStream out = new PrintStream(booksFile)) {
            for (var book: books) {
                out.println(book);
            }
            booksChanged = false;
        }        
    }
}
