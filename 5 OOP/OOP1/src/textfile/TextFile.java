package textfile;

/**
 * Текстовый файл
 * @author aabyodj
 */
public class TextFile extends File {
    /** Содержимое текстового файла */
    String content;
    
    /**
     * Создать новый пустой текстовый файл
     * @param name Имя файла
     */
    public TextFile(String name) {
        super(name);
        content = "";
    }

    /**
     * Дополнить текстовый файл строкой
     * @param object Строка
     */
    @Override
    public void append(Object object) {
        content = ((String) content).concat((String) object);
    }

    /** Напечатать содержимое файла */
    @Override
    public void print() {
        System.out.println("Содержимое файла " + getFullName() + ':');
        System.out.println(content);
    }
}
