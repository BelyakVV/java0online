package cli;

import java.util.Arrays;
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
            System.out.println("Выберите:\n---------");
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
     * Возврат текущей ширины экрана в символах
     * @return Ширина экрана
     */
    public static int getScreenWidth() {
        return 130; //Серьёзно, JVM не умеет определять ширину экрана???
    }

    /** Псевдографика: пересечение вертикальной и горизонтальной линий */
    public static final char CR_SEPARATOR = '+';
    /** Псевдографика: горизонтальная линия */
    public static final char H_SEPARATOR = '-';
    /** Псевдографика: вертикальная линия */
    public static final char V_SEPARATOR = '|';
    /** Пробел */
    public static final char SPACE = ' ';
    
    /**
     * Сгенерировать из заданного символа строку заданной длины 
     * @param ch Символ
     * @param length Длина строки
     * @return 
     */
    public static String repeatChar(char ch, int length) {
        char[] result = new char[length];
        Arrays.fill(result, ch);
        return new String(result);
    }
    
    /**
     * Ограничить длину строки и дописать в конце многоточие. Если строка уже 
     * укладывается в заданный размер - вернуть её без изменений.
     * @param string Исходная строка
     * @param limit Ограничение на длину
     * @return Строка не длинее limit
     */
    public static String limitString(String string, int limit) {
        if (string.length() <= limit) return string;
        StringBuilder result = new StringBuilder(string);
        result.setLength(limit);
        if (limit > 3) result.replace(limit - 3, limit, "...");
        return result.toString();
    }
    
    /**
     * Выравнивание строки влево при заданной ширине. 
     * Если строка короче, чем надо - дополнить справа пробелами; если длинее - 
     * обрезать лишнее и добавить в конце многоточие.
     * @param string Исходная строка
     * @param width Заданная ширина
     * @return
     */
    public static String alignLeft(String string, int width) {
        if (string.length() > width) return limitString(string, width);
        return string + repeatChar(SPACE, width - string.length());
    }
    
    /**
     * Выравнивание строки по центру при заданной ширине. 
     * Если строка короче, чем надо - дополнить слева и справа пробелами; если 
     * длинее - обрезать лишнее и добавить в конце многоточие.
     * @param string Исходная строка
     * @param width Заданная ширина
     * @return
     */
    public static String alignCenter(String string, int width) {
        if (string.length() > width) return limitString(string, width);
        int startPos = (width - string.length()) / 2;
        return repeatChar(SPACE, startPos) + string 
                + repeatChar(SPACE, width - startPos - string.length());
    }
    
    /**
     * Выравнивание строки вправо при заданной ширине. 
     * Если строка короче, чем надо - дополнить слева пробелами; если длинее - 
     * обрезать лишнее и добавить в конце многоточие.
     * @param string Исходная строка
     * @param width Заданная ширина
     * @return
     */
    public static String alignRight(String string, int width) {
        if (string.length() > width) return limitString(string, width);
        return repeatChar(SPACE, width - string.length()) + string;
    }

    public static String T_R_SPLIT_REGEX = "\\" + V_SEPARATOR;
    
    public static String[] formatTable(String[] rows) {
        if (rows.length < 2) return rows;
        String[][] cells = new String[rows.length][];
        cells[0] = rows[0].split(T_R_SPLIT_REGEX);
        if (cells[0].length < 2) return rows;
        int[] width = new int[cells[0].length];
        int[] minWidth = new int[width.length];
        int totalWidth = 0;
        for (int j = 0; j < width.length; j++) {
            int cellWidth = cells[0][j].length();
            width[j] = cellWidth;
            minWidth[j] = cellWidth;
            totalWidth += cellWidth;
        }
        int screenWidth = getScreenWidth();
        //Ширина экрана за вычетом сепараторов
        int netSW = screenWidth - width.length + 1;
        //В случае выхода за границы экрана - пропорционально уменьшаем минимальные ширины
        if (totalWidth > netSW) { 
            int newTotalWidth = 0;
            for (int j = 0; j < width.length - 1; j++) {
                width[j] = width[j] * netSW / totalWidth;
                minWidth[j] = width[j];
                newTotalWidth += width[j];
            }
            width[width.length - 1] = netSW - newTotalWidth;
            minWidth[width.length - 1] = width[width.length - 1];
        }
        int nonEmptyRows = 1; //Шапка тоже считается
        for (int i = 1; i < rows.length; i++) {
            if (rows[i] == null) continue;
            cells[i] = rows[i].split(T_R_SPLIT_REGEX);
            int[] rowWidth = new int[width.length];
            int widthSum = 0;
            for (int j = 0; j < width.length; j++) {
                if (j < cells[i].length) {
                    int cellWidth = cells[i][j].length();
                    rowWidth[j] = Math.max(cellWidth, minWidth[j]);
                    widthSum += cellWidth;
                } else {
                    rowWidth[j] += minWidth[j];
                }
            }
            //Учитываем только строки, где есть хотя бы одна непустая ячейка
            if (widthSum > 0) {
                for (int j = 0; j < width.length; j++) {
                    width[j] += rowWidth[j];
                }
                nonEmptyRows++;
            } else {                
                rows[i] = "";
            }            
        }
        if (nonEmptyRows < 2) return rows;
        //Суммарная ширина всех ячеек таблицы
        int sumWidth = 0;
        for (int j = 0; j < width.length; j++) {
            sumWidth += width[j];
        }
        totalWidth = 0;
        StringBuilder newRow = new StringBuilder();
        for (int j = 0; j < width.length - 1; j++) {
            //width[j] = width[j] * (netSW * nonEmptyRows) / sumWidth / nonEmptyRows;
            //Окончательная ширина каждого столбца            
            width[j] = width[j] * netSW / sumWidth;
            totalWidth += width[j];
            newRow.append(alignCenter(cells[0][j], width[j])).append(V_SEPARATOR);
        }
        width[width.length - 1] = netSW - totalWidth;
        newRow.append(alignCenter(cells[0][width.length - 1], 
                                                 width[width.length - 1]));
        rows[0] = newRow.toString();
        for (int i = 1; i < rows.length; i++) {
            if (rows[i].length() == 0) continue;
            newRow.setLength(0);
            for (int j = 0; j < width.length; j++) {
                newRow.append(alignLeft(cells[i][j], width[j])).append(V_SEPARATOR);
            }
            newRow.setLength(newRow.length() - 1);
            rows[i] = newRow.toString();
        }
        return rows;        
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
    
    public enum Align {
        DEFAULT, LEFT, CENTER, RIGHT;
    }
}
