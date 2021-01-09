package a2;

import cli.CLI;
import cli.CLI.Option;

/**
 *   Создать объект класса Автомобиль, используя классы Колесо, Двигатель. 
 * Методы: ехать, заправляться, менять колесо, вывести на консоль марку 
 * автомобиля.
 * @author aabyodj
 */
public class A2 {
    /** Объект класса Автомобиль */
    static Automobile automobile = new Automobile("МАЗ-Купава 573150");
    
    /** Пункт главного меню "Ехать" */
    static final Option optionStart = new Option("Ехать", A2::start);
    /** Пункт главного меню "Остановиться" */
    static final Option optionStop = new Option("Остановиться", A2::stop);
    /** Главное меню */
    static final Option[] mainMenu = new Option[]{
        optionStart,
        new Option("Вывести остаток топлива", A2::printFuel),
        new Option("Заправиться", A2::fill),
        new Option("Поменять колесо", A2::changeWheel),
        new Option("Вывести марку автомобиля", A2::printModel),
        new Option("Завершить работу", A2::exit)
    };
    /** Интерфейс командной строки */
    static final CLI cli = new CLI(mainMenu);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Запуск приложения
        cli.run();
    }
    
    /** Ехать */
    static void start() {
        try {
            automobile.start();
            mainMenu[0] = optionStop;
        } catch (Exception e) {
            System.out.println("Невозможно ехать. " + e.getMessage());
        }
    }
    
    /** Остановиться */
    static void stop() {
        automobile.stop();
        mainMenu[0] = optionStart;
    }
    
    /** Вывести остаток топлива */
    static void printFuel() {
        System.out.println("В баке " + automobile.getFuel() + " литров топлива.");
    }
    
    /** Заправиться */
    static void fill() {
        if (automobile.isDriving()) {
            System.out.println("Заправка во время движения ещё не реализована.");
            return;
        }
        try {
            automobile.fill(cli.getDouble("Сколько топлива долить"));
        } catch (Exception e) {
            System.out.println("Невозможно. " + e.getMessage());
        }
        printFuel();
    }
    
    /** Поменять колесо */
    static void changeWheel() {
        //Меню выбора колеса, которое надо поменять
        var menu = CLI.buildMenu(automobile.getWheelPositions());
        //Поменять выбранное колесо
        automobile.changeWheel(cli.getChoice(menu));
    }
    
    /** Вывести марку автомобиля */
    static void printModel() {
        System.out.println(automobile.getModel());
    }
    
    /** Завершение работы */
    static void exit() {
        //Попрощаться 
        automobile.sayGoodbye();
        System.exit(0);
    }
}
