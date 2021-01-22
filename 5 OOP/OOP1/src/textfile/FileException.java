package textfile;

/**
 * Ошибки при операциях с файлами
 * @author aabyodj
 */
public abstract class FileException extends Exception {
    /**
     * Создать исключение с текстовым сообщением
     * @param message Текстовое сообщение
     */
    FileException(String message) {
        super(message);
    }

    /** Попытка удалить непустую директорию */
    public static class DirectoryNotEmptyException extends FileException {
        /**
         * Попытка удалить непустую директорию
         * @param name Имя директории
         */
        public DirectoryNotEmptyException(String name) {
            super("Директория \"" + name + "\" не пуста.");
        }
    }
    
    /** Недопустимое имя файла */
    public static class InvalidFileNameException extends FileException {
        /**
         * Недопустимое имя файла
         * @param name Имя файла
         */
        public InvalidFileNameException(String name) {
            super("Имя файла \"" + name + "\" недопустимо.");
        }
    }
    
    /** Обнаружен дубликат имени файла */
    public static class FileAlreadyExistsException extends FileException {
        /**
         * Обнаружен дубликат имени файла
         * @param fileName Имя файла
         * @param path Директория
         */
        public FileAlreadyExistsException(String fileName, String path) {
            super("Файл с именем \"" + fileName + "\" уже существует в директории \""
                + path + "\".");
        }
    }  
    
    /** Файл не найден */
    public static class FileNotFoundException extends FileException {
        /**
         * Файл не найден
         * @param fileName Имя файла
         * @param path Директория
         */
        public FileNotFoundException(String fileName, String path) {            
            super("Файл с именем \"" + fileName + "\" не найден в директории \""
                + path + "\".");
        }
    }
}
