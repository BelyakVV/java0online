package a1;

import cli.CLI;

/**
 *   Агрегация и композиция. Задача 1.
 *   Создать объект класса Текст, используя классы Предложение, Слово. Методы: 
 * дополнить текст, вывести на консоль текст, заголовок текста.
 * @author aabyodj
 */
public class A1 {
    /** Объект класса Текст */
    static Text text = new Text("Заголовок текста",
        "Этот текст очень короткий и состоит всего из двух предложений, но его "
        + "можно дополнить. Это очень просто!");
    
    /** Интерфейс командной строки */
    static CLI cli = new CLI(new CLI.Option[]{
        new CLI.Option("Вывести заголовок", A1::printTitle),
        new CLI.Option("Вывести текст", A1::printText),
        new CLI.Option("Дополнить текст", A1::appendText),
        new CLI.Option("Выход", () -> System.exit(0))});

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Запуск приложения
        cli.run();
    }    
    
    /** Вывести заголовок текста */
    static void printTitle() {
        System.out.println(text.getTitle());
    }
    
    /** Вывести текст */
    static void printText() {
        System.out.println(text.getText());
    }
    
    /** Дополнить текст и вывести результат */
    static void appendText() {
        text.append(cli.getLine("Введите строку"));
        System.out.println("Дополненный текст:\n" + text.getText());
    }
}
