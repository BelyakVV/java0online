package cli;

import static cli.Table.BR;
import java.io.Console;
import java.util.List;
import java.util.Scanner;

/**
 *   Интерфейс командной строки
 * @author aabyodj
 */
public final class CLI {
    /** Главное меню приложения */
    private Option[] menu;
    
    static final Console CON = System.console();
    static final Scanner IN = new Scanner(System.in);

    public CLI() {        
    }

    /**
     * Инициализация приложения
     * @param menu Главное меню приложения
     */
    public CLI(Option[] menu) {
        setMenu(menu);
    }
    
    /** Запуск приложения */
    public void run() {
        if (menu == null) return;
        while (true) {
            getChoice(menu);
            waitForEnter();
            System.out.println();
        }
    }
    
    /**
     *   Меню: выбор одного из вариантов
     * @param options Список вариантов
     * @return Индекс выбранного варианта в массиве options
     */
    public int getChoice(Option[] options) {
        if (options.length < 1) return -1; //Пустой список
        int result;
        while (true) {
            System.out.println("Выберите:" + BR + "---------");
            //Вывод списка вариантов
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + " - " + options[i].text);
            }
            //Ввод ответа
            try {
                result = Integer.parseInt(readLine()) - 1;
            } catch (Exception e) {
                result = -1;
                //in.next();
            }
            //Валидация результата
            if (result < 0 || result >= options.length) { 
                System.out.println("Нет такого варианта.");
            } else break; //Валидный результат
        }
        System.out.println("Выбрано: " + options[result].text);
        //Запуск связанного действия, если задано
        if (options[result].action != null)
            options[result].action.run();
        //Обработка вложенного меню, если есть
        if (options[result].subMenu != null)
            getChoice(options[result].subMenu);
        return result;
    }
    
    /**
     * Ввод значения типа int
     * @param hint текстовая подсказка
     * @return введённое значение
     */
    public int getInt(String hint) {
        try {
            return Integer.parseInt(readLine(hint));
        } catch (Exception e) {
            return 0;
        }
    }    
    
    /**
     * Ввод значения типа long
     * @param hint текстовая подсказка
     * @return введённое значение
     */
    public long getLong(String hint) {
        try {
            return Long.parseLong(readLine(hint));
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Ввод значения типа String
     * @param hint Текстовая подсказка
     * @return Введённое значение
     */
    public String getString(String hint) {
        return readLine(hint);
    }
    
    public char[] getPass(String hint) {
        return readPassword(hint);
    }
    
    public void setMenu(Option[] menu) {
        this.menu = menu;
    }
    
    public String readLine() {
        if (CON != null) return CON.readLine();
        return IN.nextLine();
    }
    
    public String readLine​(String hint) {
        printHint(hint);
        return readLine();
    }
    
    public char[] readPassword() {
        if (CON != null) return CON.readPassword();
        return IN.nextLine().toCharArray();
    }
    
    public char[] readPassword(String hint) {
        printHint(hint);
        return readPassword();
    }
    
    void printHint(String hint) {
        if (hint != null) {
            if (!hint.isBlank()) System.out.print(hint + ": ");
        }
    }
    
    /** Ожидание нажатия ENTER */
    public void waitForEnter() {
        System.out.print("Нажмите ВВОД для продолжения...");
        readLine();
       // while (!in.hasNextLine());
    }    
    
    public static Option[] buildMenu(List<String> strings) {
        Option[] result = new Option[strings.size()];
        int i = 0;
        for (var string: strings) {
            result[i++] = new Option(string);
        }
        return result;
    }
    
    /**
     * Создать из массива строк меню для int getChoice(Option[] list).
     * @param strings Массив строк
     * @return 
     */
    public static Option[] buildMenu(String[] strings) {
        Option[] result = new Option[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Option(strings[i]);
        }
        return result;
    }
    
    static final String RESTART_FLAG = "RESTART_WITH_CONSOLE";
    
    static void restartWithConsole(String[] args) throws ClassNotFoundException {
        //System.getProperties().list(System.out);
//        System.out.println(System.getProperty("java.class.path"));
//        System.out.println(System.getProperty("jdk.module.path"));
//        System.out.println(System.getProperty("sun.java.command"));
//        System.out.println(System.getProperty("user.dir"));
        //System.out.println(CLI.class.getProtectionDomain().getCodeSource().getLocation());
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        System.out.println(Class.forName(trace[trace.length - 1].getClassName()).getProtectionDomain().getCodeSource().getLocation());
        System.exit(0);
        if (System.console() != null) return;
        if (args.length > 0) 
            if (args[0].contentEquals(RESTART_FLAG)) return;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.        
    }
    
    /**
     * Пункт меню для int getChoice(Option[] list)
     */
    public static class Option {
        /** Подсказка для пользователя */
        public final String text;

        /** Действие этого пункта меню, если есть, иначе null */
        public final Runnable action;
        
        /** Подменю, если есть, иначе null */
        public final Option[] subMenu;

        /**
         * Создать из строки пункт меню без действия
         * @param text Подсказка для пользователя
         */ 
        public Option(String text) {
            this(text, null, null);
        }
        
        /**
         * Создать пункт меню
         * @param text Подсказка для пользователя
         * @param action Действие
         */
        public Option(String text, Runnable action) {
            this(text, action, null);
        }

        /**
         * Создать пункт меню без действия, содержащий только вложенное меню.
         * @param text Подсказка для пользователя
         * @param subMenu Вложенное меню
         */
        public Option(String text, Option[] subMenu) {
            this(text, null, subMenu);
        }

        /**
         * Создать пункт меню, содержащий действие и вложенное меню
         * @param text Подсказка для пользователя
         * @param action Действие
         * @param subMenu Вложенное меню
         */
        public Option(String text, Runnable action, Option[] subMenu) {
            this.text = text;
            this.action = action;
            this.subMenu = subMenu;
        }
    }
}
