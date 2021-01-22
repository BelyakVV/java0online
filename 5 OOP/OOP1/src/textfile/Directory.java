package textfile;

import java.util.LinkedList;
import java.util.List;
import textfile.FileException.DirectoryNotEmptyException;
import textfile.FileException.FileAlreadyExistsException;
import textfile.FileException.FileNotFoundException;
import textfile.FileException.InvalidFileNameException;

/**
 * Директория является частным случаем файла
 * @author aabyodj
 */
public class Directory extends File {
    /** Содержимое директории */
    List<File> files;

    /**
     * Создать пустую директорию
     * @param name Имя директории
     */
    public Directory(String name) {
        super(name);
        files = new LinkedList<>();
    }

    /**
     * Дополнить директорию файлом
     * @param object Файл
     * @throws FileException 
     */
    @Override
    public void append(Object object) throws FileException {
        File file = (File) object;
        if (file.name.isEmpty()) //Имя файла не может быть пустым
            throw new InvalidFileNameException(file.name);
        if (this.contains(file.name)) //Дубликаты имён не допускаются
            throw new FileAlreadyExistsException(file.name, getFullName() + '/');
        files.add(file);
        file.parent = this;
    }

    /** Напечатать содержимое директории */
    @Override
    public void print() {
        if (parent == null) {
            System.out.println("Содержимое корневой директории:");
        } else {
            System.out.println("Содержимое директории " + getFullName() + ':');
        }
        for (var file: files) {
            System.out.println(file.name);
        }
        System.out.println("Всего файлов и директорий: " + files.size());
    }

    /**
     * Проверка, содержит ли директория файл с заданным именем
     * @param fileName Имя файла
     * @return true, если файл найден
     */
    public boolean contains(String fileName) {
        for (var file: files) {
            if (file.name.compareTo(fileName) == 0) return true;
        }
        return false;
    }    
    
    /**
     * Проверка, является ли директория корневой
     * @return true, если это корневая директория
     */
    public boolean isRoot() {
        return parent == null && this instanceof Directory;
    }
    
    /**
     * Поиск в директории файла с заданным именем
     * @param fileName Имя файла
     * @return Найденный файл
     * @throws textfile.FileException.FileNotFoundException 
     */
    public File findFirst(String fileName) throws FileNotFoundException {
        for (var file: files) {
            if (file.name.compareTo(fileName) == 0) return file;
        }
        throw new FileNotFoundException(fileName, getFullName() + '/');
    }
    
    /**
     * Удаление заданного файла
     * @param file Файл
     * @throws textfile.FileException.DirectoryNotEmptyException 
     */
    public void remove(File file) throws DirectoryNotEmptyException {
        //Это может быть директория
        if (file instanceof Directory) 
            //Она должна быть пуста
            if (((Directory) file).files.size() > 0)
                throw new DirectoryNotEmptyException(file.getFullName() + '/');
        file.parent = null; //Пустой аргумент - в любом случае NullPointerException
        files.remove(file);
    } 
}
