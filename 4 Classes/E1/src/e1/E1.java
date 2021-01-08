package e1;

/**
 *   Простейшие классы и объекты. Задача №1.
 *   Создайте класс Test1 двумя переменными. Добавьте метод вывода на экран и 
 * методы изменения этих переменных. Добавьте метод, который находит сумму 
 * значений этих переменных, и метод, который находит наибольшее значение из 
 * этих двух переменных.
 * @author aabyodj
 */
public class E1 {
    
    static Test1 test1 = new Test1();
    
    /** Command-line interface */
    static CLI cli = new CLI();
    
    /** Меню: изменение одного из значений a или b */
    static CLI.Option[] menuChangeAny = {
        new CLI.Option("Изменить test1.a", () -> {
            test1.setA(cli.getInt("Введите новое значение"));
        }),
        new CLI.Option("Изменить test1.b", () -> {
            test1.setB(cli.getInt("Введите новое значение"));
        }),
        new CLI.Option("Отмена", () -> {})
    };
    
    /** Главное меню */
    static CLI.Option[] menuMain = {
        new CLI.Option("Вывести значения полей", () -> {
            test1.print();
            //System.out.println();
        }),
        new CLI.Option("Изменить значение одного из полей", () -> {
            cli.getChoice(menuChangeAny);
            //System.out.println();
        }),
        new CLI.Option("Вывести сумму значений полей", () -> {
            System.out.println(test1.getSum());
        }),
        new CLI.Option("Вывести наибольшее из значений полей", () -> {
            System.out.println(test1.getMax());
        }),        
        new CLI.Option ("Выход", () -> System.exit(0))
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        do {            
            cli.getChoice(menuMain);
            cli.waitForEnter();
            System.out.println();
        } while (true);
    }

    
}
