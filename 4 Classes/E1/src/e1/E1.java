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
    
    /** Изменение одного из значений */
    static Option[] menuChangeAny = {
        new Option("Изменить test1.a", () -> {
            test1.setA(cli.getInt("Введите новое значение"));
        }),
        new Option("Изменить test1.b", () -> {
            test1.setB(cli.getInt("Введите новое значение"));
        }),
        new Option("Отмена", () -> {})
    };
    
    /** Главное меню */
    static Option[] menuMain = {
        new Option("Вывести значения полей", () -> {
            test1.print();
            System.out.println();
        }),
        new Option("Изменить значение одного из полей", () -> {
            cli.getChoice(menuChangeAny);
            System.out.println();
        }),
        new Option("Вывести сумму значений полей", () -> {
            System.out.println(test1.getSum() + "\n");
        }),
        new Option("Вывести наибольшее из значений полей", () -> {
            System.out.println(test1.getMax() + "\n");
        }),        
        new Option ("Выход", () -> System.exit(0))
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        do {            
            cli.getChoice(menuMain);
        } while (true);
    }

    
}
