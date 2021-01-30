package textfile;

import textfile.FileException.FileAlreadyExistsException;
import textfile.FileException.InvalidFileNameException;

/**
 * Базовый класс для текстового файла и директории
 * @author aabyodj
 */
public abstract class File {
    /** Родительская директория */
    Directory parent;
    /** Имя файла */
    String name;

    /**
     * Создать пустой файл
     * @param name Имя файла
     */
    public File(String name) {
        this.name = name;
    } 
    
    /**
     * Дополнить файл
     * @param object Файл для директории, строка для текстового файла
     * @throws FileException 
     */
    public abstract void append(Object object) throws FileException; 
    
    /** Напечатать содержимое файла */
    public abstract void print();
    
    /**
     * Удалить файл
     * @throws FileException 
     */
    public void remove() throws FileException {
        parent.remove(this);
    }
    
    /**
     * Получить имя файла
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Получить полное имя файла: абсолютный путь + имя
     * @return 
     */
    public String getFullName() {
        if (parent != null) return getPath() + name;
        if (this instanceof Directory) return "";
        return name;
    }
    
    /**
     * Получить путь к файлу: абсолютное имя родительской директории
     * @return 
     */
    public String getPath() {
        if (parent != null) return parent.getFullName() + '/';
        if (this instanceof Directory) return "";
        return null;
    }
    
    /**
     * Переименовать файл
     * @param newName Новое имя
     * @throws FileException 
     */
    public void rename(String newName) throws FileException {
        if (parent != null) { //Если не корневая директория
            if (newName.isEmpty()) //Пустые имена не допускаются
                throw new InvalidFileNameException(newName); 
            if (parent.contains(newName)) //Дубликаты имён не допускаются
                throw new FileAlreadyExistsException(newName, getPath());
        } 
        name = newName;
    }
}
