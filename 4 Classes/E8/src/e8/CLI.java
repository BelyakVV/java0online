package e8;

import java.util.Scanner;

/**
 *   Интерфейс командной строки
 * @author aabyodj
 */
public final class CLI {
    /** Ширина экрана по умолчанию, в символах */
    public static final int SCREEN_WIDTH = 80;

    private final Scanner in;
    
    /** Главное меню приложения */
    private final Option[] menu;

    /**
     * Инициализация приложения
     * @param menu Главное меню приложения
     */
    public CLI(Option[] menu) {
        in = new Scanner(System.in);
        this.menu = menu;
    }
    
    /** Запуск приложения */
    public void run() {
        while (true) {
            getChoice(menu);
            waitForEnter();
            System.out.println();
        }
    }
    
    /**
     *   Меню: выбор одного из вариантов
     * @param options список вариантов
     */
    public void getChoice(Option[] options) {
        if (options.length < 1) return; //Пустой список
        int result;
        while (true) {
            System.out.println("Выберите:\n---------");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + " - " + options[i].text);
            }
            try {
                result = in.nextInt() - 1;
            } catch (Exception e) {
                result = -1;
                in.next();
            }
            if (result < 0 || result >= options.length) { //Валидация результата
                System.out.println("Нет такого варианта.");
            } else break; //Валидный результат
        }
        System.out.println("Выбрано: " + options[result].text);
        if (options[result].action != null)
            options[result].action.run();
        if (options[result].subMenu != null)
            getChoice(options[result].subMenu);
    }
    
    /**
     * Ввод значения типа int
     * @param hint текстовая подсказка
     * @return введённое значение
     */
    public int getInt(String hint) {
        if (hint != null) {
            if (!hint.isBlank()) System.out.print(hint + ": ");
        }
        try {
            return in.nextInt();
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
        if (hint != null) {
            if (!hint.isBlank()) System.out.print(hint + ": ");
        }
        try {
            return in.nextLong();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /** Ожидание нажатия ENTER */
    public void waitForEnter() {
        System.out.print("Нажмите ВВОД для продолжения...");
        in.nextLine();
        while (!in.hasNextLine());
    }
    
    /**
     * Возврат текущей ширины экрана в символах
     * @return Ширина экрана
     */
    public static int getScreenWidth() {
        return 130; //Серьёзно, JVM не умеет определять ширину экрана???
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
