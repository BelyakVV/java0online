package aabyodj.console;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Интерфейс командной строки
 * @author aabyodj
 */
public final class CommandLineInterface {
    
    /** Главное меню приложения */
    private Option[] menu;
    
    //TODO: избавиться
    static final Scanner IN = new Scanner(System.in);
    
    //Для waitForEnter()
    boolean justHitEnter = false;

    public CommandLineInterface() {        
    }

    /**
     * Инициализация приложения
     * @param menu Главное меню приложения
     */
    public CommandLineInterface(Option[] menu) {
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
            System.out.println("Выберите:" + Const.BR + "---------");
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
    

	/**
     * Ввод пароля
     * @param hint Текстовая подсказка
     * @return Введённый пароль
     */
    public char[] getPass(String hint) {
        return readPassword(hint);
    }
    
    /**
     * Задать главное меню приложения
     * @param menu Новое меню
     */
    public void setMenu(Option[] menu) {
        this.menu = menu;
    }
    
    /**
     * Ввести значение типа String
     * @return Введённое значение
     */
    public String readLine() {
        justHitEnter = false;       //Для waitForEnter()
        if (Const.CON != null) {    //Если есть System.console()
            return Const.CON.readLine();
        }
        return IN.nextLine();
    }
    
    /**
     * Ввести значение типа String
     * @param hint Текстовая подсказка
     * @return Введённое значение
     */
    public String readLine(String hint) {
        printHint(hint);
        return readLine();
    }
    
    /**
     * Ввести пароль
     * @return Введённый пароль
     */
    public char[] readPassword() {
        justHitEnter = false;           //Для waitForEnter()
        if (Const.CON != null) {        //Если есть System.console()
            return Const.CON.readPassword();
        }
        
        //TODO: отключить эхо
        return IN.nextLine().toCharArray();
    }
    
    /**
     * Ввести пароль
     * @param hint Текстовая подсказка
     * @return Введённый пароль
     */
    public char[] readPassword(String hint) {
        printHint(hint);
        return readPassword();
    }
    
    /**
     * Вывести текстовую подсказку
     * @param hint Текстовая подсказка
     */
    static void printHint(String hint) {
        if (hint != null) {
            if (!hint.isBlank()) {
                System.out.print(hint + ": ");
            }
        }
    }
    
    /**
     * Постраничный вывод
     * @param text Исходный текст
     */
    public void printByPages(String text) {
        String[] lines = text.split(Const.BR);
        int row = getRows();    //Количество строк в консоли
        if (lines.length >= row) {
            int line = 0;
            while (line < lines.length) {
                while ((row > 0) && (line < lines.length)) {
                    System.out.println(lines[line++]);
                    row--;
                }
                justHitEnter = false;
                waitForEnter();
                row = getRows();
            }
        } else {
            System.out.println(text);
            justHitEnter = false;
        }        
    }

    /**
     * Вывод сообщения об ошибке
     * @param msg Описание ошибки
     */
    public static void printErrorMsg(String msg) {
        if (msg.isBlank()) {
            throw new IllegalArgumentException("Не указано описание ошибки");
        }
        char[] message = msg.toCharArray();
        message[0] = Character.toLowerCase(message[0]);
        System.out.println("Ошибка: " + new String(message));
    }
    
    /** Ожидание нажатия ENTER */
    public void waitForEnter() {
        if (justHitEnter) return;
        System.out.print("Нажмите ВВОД для продолжения...");
        readLine();
        justHitEnter = true;
    }    
    
    //TODO вынести константы в отдельный класс
    static final int DEFAULT_ROWS = 25;
    static final int MAX_COLS = 10000;
    static final int MAX_ROWS = 10000;
    
    /**
     * Получить высоту консоли в строках (отсчёт от 0)
     * @return 
     */
    static int getRows() {
    //FIXME
    
        //if (CON == null) 
            return DEFAULT_ROWS;
//        saveCursorPosition();
//        gotoXY(MAX_COLS, MAX_ROWS);
//        Position position = getCursorPosition();
//        restoreCursorPosition();
//        return position.y + 1;
    }
    
    static final String CSI = "\u001b[";
    
    static final String SCP = CSI + 's';
    
    /**
     * Сохранить позицию курсора
     */
    public void saveCursorPosition() {
        System.out.print(SCP);
    }
    
    static final String RCP = CSI + 'u';
    
    /**
     * Восстановить позицию курсора
     */    
    public void restoreCursorPosition() {
        System.out.print(RCP);
    }
    
    static final String DSR = CSI + "6n";
    static final Pattern DSR_PTRN = Pattern.compile("(\\d+);(\\d+)R");
    
    static final String NO_DSR = "Функция определения координат курсора не поддерживается.";
    
    /**
     * Получить позицию курсора
     * @return Позиция курсора
     */
    Position getCursorPosition() {
    /*
        FIXME
        https://stackoverflow.com/questions/62026230/reading-cursor-position-in-a-java-console-application
    */
        
        //if (System.in.available() > 0) System.in.readAllBytes();
        System.out.print(DSR);
        try {
            if (System.in.available() < 1)
                throw new RuntimeException(NO_DSR);
        } catch (IOException ex) {
            throw new RuntimeException(NO_DSR);
        }
        //Scanner in = new Scanner(System.in);
        Matcher matcher = DSR_PTRN.matcher(IN.nextLine());
        //Matcher matcher = DSR_PTRN.matcher("");
        if (!matcher.find())
            throw new RuntimeException(NO_DSR);
        int x = Integer.parseInt(matcher.group(1)) - 1;
        int y = Integer.parseInt(matcher.group(2)) - 1;
        //System.out.printByPages("x = " + x + "; y = " + y);
        return new Position(x, y);
    }
    
    /**
     * Установить позицию курсора в заданные координаты (отсчёт от 0)
     * @param x
     * @param y 
     */
    public void gotoXY(int x, int y) {
        x = Math.max(1, x + 1);
        y = Math.max(1, y + 1);
        String cup = CSI + Integer.toString(x) + ';' + Integer.toString(y) + 'H';
        System.out.print(cup);
    }
    
    /**
     * Установить позицию курсора
     * @param pos Заданная позиция
     */
    public void setCursorPosition(Position pos) {
        gotoXY(pos.x, pos.y);
    }
    
    /**
     * Позиция курсора
     */
    public class Position {
        public int x;
        public int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
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
    
    /**
     * Перезапустить приложение в попытке получить в своё распоряжение консоль
     * @param args Аргументы командной строки, полученные функцией main()
     * @throws ClassNotFoundException 
     */
    static void restartWithConsole(String[] args) throws ClassNotFoundException {
    //FIXME
        
        //System.getProperties().list(System.out);
//        System.out.printByPages(System.getProperty("java.class.path"));
//        System.out.printByPages(System.getProperty("jdk.module.path"));
//        System.out.printByPages(System.getProperty("sun.java.command"));
//        System.out.printByPages(System.getProperty("user.dir"));
        //System.out.printByPages(CommandLineInterface.class.getProtectionDomain().getCodeSource().getLocation());
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
