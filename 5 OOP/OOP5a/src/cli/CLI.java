package cli;

import static cli.Table.BR;
import java.util.Scanner;

/**
 *   Интерфейс командной строки
 * @author aabyodj
 */
public final class CLI {
    /** Главное меню приложения */
    private final Option[] menu;
    
    private final Scanner in;    

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
                result = in.nextInt() - 1;
            } catch (Exception e) {
                result = -1;
                in.next();
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
