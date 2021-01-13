package cli;

import static cli.Table.Align.*;
import java.util.Arrays;

/**
 * Создание и отображение таблиц для текстового интерфейса
 * @author aabyodj
 */
public final class Table {
    /** Псевдографика: пересечение вертикальной и горизонтальной линий */
    public static final char CR_SEPARATOR = '+';
    /** Псевдографика: горизонтальная линия */
    public static final char H_SEPARATOR = '-';
    /** Псевдографика: вертикальная линия */
    public static final char V_SEPARATOR = '|';
    /** Пробел */
    public static final char SPACE = ' ';
    
    /** Первый столбец */
    private Col firstCol;
    /** Последний столбец */
    private Col lastCol;
    /** Количество столбцов */
    private int colsCount;
    
    /** Первая строка */
    private Row firstRow;
    /** Последняя строка */
    private Row lastRow;
    /** Количество строк */
    private int rowsCount;
    
    /** Ширина таблицы в символах, с учётом горизонтальных разделителей */
    private int width;
    /** Признак необходимости пересчитать ширину таблицы и всех столбцов */
    private boolean needRecalcWidth = true;
    
    /** Горизонтальное выравнивание элементов таблицы по умолчанию */
    private Align align = LEFT;
    
    /** 
     * Создание пустой таблицы с заданной шапкой (верхней строкой)
     * @param head Шапка таблицы
     */
    public Table(String[] head) {
        addRow(head);
    }

    /** 
     * Получить горизонтальное выравнивание элементов таблицы по умолчанию 
     * @return 
     */
    public Align getAlign() {
        return align;
    }
    
    /**
     * Задать горизонтальное выравнивание элементов таблицы по умолчанию
     * @param align 
     */
    public void setAlign(Align align) {
        this.align = align;
    }
    
    /**
     * Получить столбец таблицы по его номеру
     * @param i
     * @return 
     */
    public Col getCol(int i) {
        Col result = firstCol;
        while (result != null && i > 0) {            
            result = result.next;
            i--;
        }
        return result;
    }
    
    /**
     * Получить последнюю строку таблицы
     * @return 
     */
    public Row getLastRow() {
        return lastRow;
    }
    
    /**
     * Задать шапку таблицы (верхнюю строку)
     * @param head 
     */
    private void setHead(Row head) {
        if (head.size < 1)
            throw new IllegalArgumentException("Нулевая ширина заголовка");
        if (rowsCount > 1) //Таблица не пуста
            throw new UnsupportedOperationException("Not supported yet.");
        if (head.table != null) //Строка принадлежит другой таблице
            throw new UnsupportedOperationException("Not supported yet.");
        head.table = this;
        //По умолчанию ячейки шапки выравниваются по центру
        if (DEFAULT == head.align) head.align = CENTER;
        // Количество столбцов таблицы = количеству ячеек в шапке
        colsCount = head.size;
        firstRow = head;
        lastRow = head;
        head.prev = null;
        head.next = null;
        //В таблице только шапка
        rowsCount = 1;
        //Пересчитываем ширину таблицы
        width = colsCount - 1; //Учёт вертикальных разделителей
        Cell cell = head.firstCell;
        firstCol = new Col(this, cell);
        lastCol = firstCol;
        while (true) {
            cell.col = lastCol;
            cell.nextInCol = null;
            cell = cell.nextInRow;
            if (cell == null) break;
            lastCol.next = new Col(this, cell);
            lastCol = lastCol.next;
        }
        //Все ширины корректно подсчитаны в процессе установки шапки
        needRecalcWidth = false;
    }
    
    /**
     * Получить полную ширину таблицы с учётом вертикальных разделителей.
     * @return 
     */
    public int getWidth() {
        //Пересчитать ширину
        recalcWidth();
        return width;
    }
    
    /**
     * Добавить строку в конец таблицы
     * @param row 
     */
    void addRow(Row row) {
        if (row.table != null) //Строка уже принадлежит какой-то таблице
            row = row.clone();
        //Привязать к таблице
        row.table = this;
        //Количество столбцов в таблице и в строке не совпадает
        if ((colsCount != row.size) && (rowsCount != 0) && !row.isSeparator)
            throw new UnsupportedOperationException("Not supported yet.");        
        //Если таблица ещё пуста
        if (firstRow == null) {
            rowsCount = 0;
            row.prev = null;
            firstRow = row;
        } else { //В таблице уже что-то есть
            row.prev = lastRow; 
            lastRow.next = row;           
        }
        lastRow = row;
        row.next = null;
        //Если это горизонтальный разделитель - на этом всё
        if (row.isSeparator) return;
        //Количество строк в таблице
        rowsCount++;        
        if (rowsCount == 1) { //Это шапка (верхняя строка)
            //По умолчанию шапка выравнивается по центру
            if (row.align == DEFAULT) row.align = CENTER;
            //Первая ячейка в строке
            Cell cell = row.firstCell;
            //Создать в таблице новый столбец
            firstCol = new Col(this, cell);
            lastCol = firstCol;
            //Перебираем ячейки в строке
            while (true) {
                cell.col = lastCol;
                cell.nextInCol = null;
                cell = cell.nextInRow;
                if (cell == null) break;
                lastCol.next = new Col(this, cell);
                lastCol = lastCol.next;
            }
            //Количество столбцов в таблице
            colsCount = row.size;
            //Горизонтальный разделитель после шапки
            addHSeparator();
        } else { //Это не шапка
            Cell cell = row.firstCell;
            Col col = firstCol;
            while (cell != null) {
                //Ширина самой широкой ячейки в столбце
                col.maxWidth = Math.max(col.maxWidth, cell.text.length());
                //Сумма ширин содержимого всех ячеек столбца
                col.widthSum += cell.text.length();
                col.lastCell.nextInCol = cell;
                col.lastCell = cell;
                cell.col = col;
                cell.nextInCol = null;
                cell = cell.nextInRow;
                col = col.next;
            }
            //Окончательные значения ширин столбцов надо пересчитывать
            needRecalcWidth = true;            
        }
    }
    
    /**
     * Созданть новую строку таблицы из массива строк текста и добавленить её
     * в конец таблицы
     * @param fields Массив строк текста
     */
    public void addRow(String[] fields) {
        addRow(new Row(fields));
    }

    /** Добавить в конец таблицы горизонтальный разделитель */
    public void addHSeparator() {
        lastRow.next = new Row(this, lastRow, null, true);
        lastRow = lastRow.next;
    }

    /** Наружный горизонтальный разделитель */
    private String outerHrSeparator = null;
    /** Внутренний горизонтальный разделитель */
    private String innerHrSeparator = null;
    
    /** Создать горизонтальные разделители для текущей ширины */
    private void createSeparators() {
        //Разделитель без пересечений
        outerHrSeparator = repeatChar(H_SEPARATOR, getWidth()); 
        //Разделитель с пересечениями
        StringBuilder hr2 = new StringBuilder(outerHrSeparator);
        Col col = firstCol;
        //Точка пересечения
        int pos = col.getWidth();
        col = col.next;
        while (col != null) {
            //Ставим пересечение для предыдущего столбца
            hr2.setCharAt(pos, CR_SEPARATOR);
            //Точка пересечения для текущего
            pos += col.getWidth() + 1; //+1 для вертикального разделителя
            col = col.next;
        }
        innerHrSeparator = hr2.toString();
    }
    
    /**
     * Получить готовую таблицу
     * @return 
     */
    @Override
    public String toString() {
        if (rowsCount == 0 || colsCount == 0) return ""; //Пустая таблица
        //Создать горизонтальные разделители
        createSeparators();
        //Начинаем с разделителя без пересечений
        StringBuilder result = new StringBuilder(outerHrSeparator).append('\n');
        //Первая строка таблицы
        Row row = firstRow;
        //Перебираем строки таблицы
        while (row != null) { 
            result.append(row.toString()).append('\n');
            row = row.next;
        }
        //Горизонтальный разделитель без пересечений
        result.append(outerHrSeparator);
        return result.toString();
    }
    
    /**
     * Пересчёт ширины таблицы и ширин всех столбцов в случае необходимости
     */
    private void recalcWidth() {
        //Надо ли пересчитывать?
        if (!needRecalcWidth) return; //Не надо
        //Учитываем вертикальные разделители
        width = colsCount - 1;
        Col col = firstCol;
        while (col != null) {
            //TODO:  Реализовать ограничение максимальной ширины
            col.width = col.maxWidth;
            width += col.width;
            col = col.next;
        }
        //В следующий раз не пересчитывать
        needRecalcWidth = false;
    }

    /**
     * Ограничить длину строки и дописать в конце многоточие. Если строка уже
     * укладывается в заданный размер - вернуть её без изменений.
     * @param string Исходная строка
     * @param limit Ограничение на длину
     * @return Строка не длинее limit
     */
    public static String limitString(String string, int limit) {
        if (string.length() <= limit) {
            return string;
        }
        StringBuilder result = new StringBuilder(string);
        result.setLength(limit);
        if (limit > 3) {
            result.replace(limit - 3, limit, "...");
        }
        return result.toString();
    }

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
     * Горизонтальное выравнивание текста
     */
    public enum Align {
        /**
         * Выравнивание по умолчанию
         */
        DEFAULT {
            @Override
            public String apply(String string, int width) {
                return LEFT.apply(string, width);
            }
        }, 

        /**
         * Выравнивание влево
         */
        LEFT {
            @Override
            public String apply(String string, int width) {
                if (string.length() > width) {
                    return limitString(string, width);
                }
                return string + repeatChar(SPACE, width - string.length());
            }
        }, 

        /**
         * Выравнивание по центру
         */
        CENTER {
            @Override
            public String apply(String string, int width) {
                if (string.length() > width) {
                    return limitString(string, width);
                }
                int startPos = (width - string.length()) / 2;
                return repeatChar(SPACE, startPos) + string 
                        + repeatChar(SPACE, width - startPos - string.length());
            }
        }, 

        /**
         * Выравнивание вправо
         */
        RIGHT {
            @Override
            public String apply(String string, int width) {
                if (string.length() > width) {
                    return limitString(string, width);
                }
                return repeatChar(SPACE, width - string.length()) + string;
            }
        };
        

        /**
         * Применить к строке соответствующее выравнивание при заданной ширине. 
         * Если строка длинее, чем надо - обрезать лишнее и добавить в конце 
         * многоточие.
         * @param string Исходная строка
         * @param width Заданная ширина
         * @return
         */
        public abstract String apply(String string, int width);
    }
    
    /** Столбец таблицы */
    public final class Col {
        /** Таблица, которой принадлежит данный столбец */
        private Table table;
        /** Следующий столбец таблицы */
        private Col next;
        /** Первая ячейка в стобце */
        private Cell firstCell;
        /** Последняя ячейка в столбце */
        private Cell lastCell;
        /** 
         * Выравнивание по умолчанию для ячеек столбца. Имеет приоритет перед
         * выравниванием таблицы 
         */
        private Align align = DEFAULT;
        /** Количество ячеек в столбце */
        private int size;
        /** Ширина содержимого самой широкой ячейки столбца */
        private int maxWidth;
        /** Минимально допустимая ширина столбца без учёта разделителей */
        private int minWidth;
        /** Ширина столбца без учёта разделителей */
        private int width;
        /** Сумма ширин содержимого всех ячеек столбца */
        private int widthSum;

        /**
         * Создать столбец, принадлежащий заданной таблице и содержащий данную
         * ячейку
         * @param table Таблица
         * @param cell Ячейка
         */
        private Col (Table table, Cell cell) {
            this.table = table;
            firstCell = cell;
            lastCell = cell;
            size = 1;
            width = cell.text.length();
            maxWidth = width;
            minWidth = width;
            widthSum = width;
            table.width += width;
        }

        /** 
         * Получить выравнивание по умолчанию для ячеек столбца
         * @return  
         */
        public Align getAlign() {
            return align;
        }
        
        /**
         * Задать выравнивание по умолчанию для ячеек столбца
         * @param align 
         */
        public void setAlign(Align align) {
            this.align = align;
        }

        /**
         * Получить эффективное выравнивание по умолчанию для ячеек столбца
         * @return 
         */
        public Align getEffectiveAlign() {
            if (align != DEFAULT) {
                return align;
            } else {
                return (null != table) ? table.getAlign() : DEFAULT;
            }
        }

        /**
         * Получить минимально допустимую ширину столбца без учёта разделителей
         * @return 
         */
        public int getMinWidth() {
            return minWidth;
        }

        /**
         * Задать минимально допустимую ширину столбца без учёта разделителей
         * @param minWidth 
         */
        public void setMinWidth(int minWidth) {
            this.minWidth = minWidth;
        }
        
        /**
         * Получить ширину столбца без учёта разделителей
         * @return 
         */
        public int getWidth() {
            if (table != null) table.recalcWidth();
            return width;
        }

        /**
         * Принудительно задать ширину столбца без учёта разделителей
         * @param width 
         */
        public void setWidth(int width) {
            if (table != null) table.recalcWidth();
            this.width = Math.max(width, minWidth);
        }
    }

    /** Строка таблицы */
    public final class Row {
        //public static final String SPLIT_REGEX = "\\" + V_SEPARATOR;
        
        /** Таблица, которой принадлежит данная строка */
        private Table table;
        private Row prev;
        /** Следующая строка в таблице */
        private Row next;
        /** Первая ячейка данной строки */
        private Cell firstCell;
        /** Последняя ячейка данной строки */
        private Cell lastCell;
        /** Количество ячеек в строке */
        private int size;
        /** Признак того, что это не строка, а горизонтальный разделитель */
        private final boolean isSeparator;
        
        /** 
         * Выравнивание по умолчанию для ячеек в данной строке. Имеет приоритет
         * перед выравниванием столбцов и выравниванием таблицы.
         */
        private Align align = DEFAULT;
        
        /**
         * Создать пустую строку таблицы или горизонтальный разделитель
         * @param table Привязать к указанной таблице
         * @param prev Предыдущая строка
         * @param next Следующая строка
         * @param isSeparator Если true, то это не строка, а горизонтальный разделитель
         */
        Row(Table table, Row prev, Row next, boolean isSeparator) {
            this.table = table;
            this.prev = prev;
            this.next = next;
            this.isSeparator = isSeparator;
        }
        
        /**
         * Создать новую строку таблицы из массива строк текста: каждый элемент 
         * массива - содержимое новой ячейки.
         * @param fields 
         */
        Row(String[] cells) {
            isSeparator = false;
            for (var cell: cells) {
                this.addCell(cell);
            }
        }

        /**
         * Получить ячейку по её номеру в строке. Нумерация от 0.
         * @param i
         * @return i-я ячейка или null при выходе за пределы строки
         */
        public Cell getCell(int i) {
            Cell result = firstCell;
            while (result != null && i > 0) {        
                result = result.nextInRow;
                i--;
            }
            return result;
        }
        
        @Override
        public Row clone() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        public String toString() {
            if (isSeparator) { //Это не строка таблицы, а горизонтальный разделитель
                if (table != null) { //Является частью таблицы
                    return innerHrSeparator;                    
                } else {
                    return null;
                }
            }
            StringBuilder result = new StringBuilder();
            Cell cell = firstCell;
            while (cell != null) {
                result.append(cell.toString()).append(V_SEPARATOR);
                cell = cell.nextInRow;
            }
            //После крайней правой ячейки вертикальный разделитель не нужен
            result.setLength(result.length() - 1);
            return result.toString();
        }
        
        /**
         * Создать новую ячейку, содержащую заданный текст, и поместить её в 
         * конец
         * @param text 
         */
        public void addCell(String text) {
            if (isSeparator)
                throw new UnsupportedOperationException("Горизонтальный разделитель не может содержать ячейки");
            if (table != null) //Строка уже является частью таблицы
                throw new UnsupportedOperationException("Not supported yet.");
            //Создать новую ячейку
            Cell newCell = new Cell(text, null, this);
            //Если это первая ячейка в строке
            if (firstCell == null) firstCell = newCell;
            if (lastCell != null) lastCell.nextInRow = newCell;
            lastCell = newCell;
            //Количество ячеек в строке
            size++;
        }
    }

    /** Ячейка таблицы */
    public final class Cell {
        /** Содержимое ячейки */
        private String text;
        /** 
         * Выравнивание содержимого ячейки. Если не задано, то последовательно 
         * проверяется выравнивание строки, затем столбца, затем таблицы.
         */
        private Align align = DEFAULT;
        /** Столбец, которому принадлежит данная ячейка */
        private Col col;
        /** Строка, которой принадлежит данная ячейка */
        private Row row;
        /** Следующая ячейка в столбце */
        private Cell nextInCol;
        /** Следующая ячейка в строке */
        private Cell nextInRow;
        
        /**
         * Создать ячейку, содержащую данный текст, и принадлежащую данному
         * столбцу и данной строке
         * @param text
         * @param col
         * @param row 
         */
        private Cell(String text, Col col, Row row) {
            setText(text);
            this.col = col;
            this.row = row;
        }

        /** 
         * Получить текст, содержащийся в данной ячейке
         * @return 
         */
        public String getText() {
            return text;
        }

        /** 
         * Задать текстовое содержимое для данной ячейки
         * @param text 
         */
        public void setText(String text) {
            int oldLength = (this.text != null) ? this.text.length() : 0;
            this.text = text.strip();
            if ((this.text.length() != oldLength) && (col != null)) {
                if (col.table != null)
                    col.table.needRecalcWidth = true;
            }
        }

        /** 
         * Получить горизонтальное выравнивание содержимого ячейки
         * @return 
         */
        public Align getAlign() {
            return align;
        }

        /**
         * Задать горизонтальное выравнивание содержимого ячейки
         * @param align 
         */
        public void setAlign(Align align) {
            this.align = align;
        }
        
        /**
         * Получить действующее горизонтальное выравнивание содержимого ячейки
         * @return 
         */
        public Align getEffectiveAlign() {
            Align result = align;
            //Сначала собственное значение
            if (result != DEFAULT) return result;
            //Выравнивание строки
            if (row != null) result = row.align;
            if ((result != DEFAULT) ||(col == null)) return result;
            //Выравнивание столбца
            result = col.align;
            if ((result != DEFAULT) || (col.table == null)) return result;
            //Выравнивание таблицы
            return col.table.align;
        }

        /**
         * Получить столбец, которому принадлежит данная ячейка
         * @return 
         */
        public Col getCol() {
            return col;
        }

        /**
         * Получить строку, которой принадлежит данная ячейка
         * @return 
         */
        public Row getRow() {
            return row;
        }

        @Override
        public String toString() {
            if (col == null) return text; //Ячейка не принадлежит ни столбцу, ни таблице
            //Выровнять содержимое по ширине
            return getEffectiveAlign().apply(text, col.getWidth());
        }        
    }
}
