package cli;

import cli.CLI.Align;

/**
 * Формирование таблиц в текстовом виде
 * @author aabyodj
 */
public final class Table {
    private Col firstCol;
    private Col lastCol;
    
    private Row firstRow;
    private Row lastRow;
    
    private int colsCount;
    private int rowsCount;
    
    private int width;
    
    private Align align = Align.DEFAULT;
    
    private boolean needRecalcWidth;
    
    public Table(String head) {
        addRow(head);
    }
    
    public Col getCol(int i) {
        Col result = firstCol;
        while (result != null && i > 0) {            
            result = result.next;
            i--;
        }
        return result;
    }
    
    private void setHead(Row head) {
        if (head.size < 1)
            throw new IllegalArgumentException("Нулевая ширина заголовка");
        if (rowsCount > 1)
            throw new UnsupportedOperationException("Not supported yet.");
        head.align = Align.CENTER;
        colsCount = head.size;
        rowsCount = 1;
        width = colsCount - 1;
        firstRow = head;
        lastRow = head;
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
        needRecalcWidth = false;
    }
    
    public int getWidth() {
        recalcWidth();
        return width;
    }
    
    private void addRow(Row row) {
        if (rowsCount < 1) {
            setHead(row);
            return;
        }
        if (row.size != colsCount) 
            throw new UnsupportedOperationException("Not supported yet.");
        row.table = this;
        row.next = null;
        lastRow.next = row;
        lastRow = row;
        rowsCount++;
        Cell cell = row.firstCell;
        Col col = firstCol;
        while (cell != null) {
            col.maxWidth = Math.max(col.maxWidth, cell.text.length());
            col.widthSum += cell.text.length();
            col.lastCell.nextInCol = cell;
            col.lastCell = cell;    
            cell.col = col;
            cell.nextInCol = null;
            cell = cell.nextInRow;
            col = col.next;
        }
        needRecalcWidth = true;
    }
    
    public void addRow (String row) {
        addRow(row.split(Row.SPLIT_REGEX));
    }
    
    public void addRow(String[] fields) {
        addRow(new Row(fields));
    }

    @Override
    public String toString() {
        String hr1 = CLI.repeatChar(CLI.H_SEPARATOR, getWidth());
        StringBuilder hr2SB = new StringBuilder(hr1);
        Col col = firstCol;
        int pos = col.getWidth();
        col = col.next;
        while (col != null) {
            hr2SB.setCharAt(pos, CLI.CR_SEPARATOR);
            pos += col.getWidth() + 1;
            col = col.next;
        }
        StringBuilder result = new StringBuilder(hr1).append('\n');
        Row row = firstRow;
        result.append(row.toString()).append('\n').append(hr2SB).append('\n');
        row = row.next;
        while (row != null) {
            //System.out.println(row.getClass());
            result.append(row.toString()).append('\n');
            row = row.next;
        }
        result.append(hr1);
        return result.toString();
    }
    
    private void recalcWidth() {
        if (!needRecalcWidth) return;
        width = colsCount - 1;
        Col col = firstCol;
        while (col != null) {
            col.width = col.maxWidth;
            width += col.width;
            col = col.next;
        }
        needRecalcWidth = false;
    }
    
    public static class Col {
        private Table table;
        private Col next;
        private Cell firstCell;
        private Cell lastCell;
        private Align align = Align.DEFAULT;
        private int size;
        private int maxWidth;
        private int minWidth;
        private int width;
        private int widthSum;

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

        public Align getAlign() {
            return align;
        }

        public void setAlign(Align align) {
            this.align = align;
        }

        public int getMinWidth() {
            return minWidth;
        }

        public void setMinWidth(int minWidth) {
            this.minWidth = minWidth;
        }
        
        public int getWidth() {
            if (table != null) table.recalcWidth();
            return width;
        }

        public void setWidth(int width) {
            if (table != null) table.recalcWidth();
            this.width = Math.max(width, minWidth);
        }
    }

    public static final class Row {
        public static String SPLIT_REGEX = "\\" + CLI.V_SEPARATOR;
        
        Table table;
        Row next;
        Cell firstCell;
        Cell lastCell;
        int size;
        
        Align align = Align.DEFAULT;
        
        Row(String[] fields) {
            for (var cell: fields) {
                addCell(cell);
            }
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            Cell cell = firstCell;
            while (cell != null) {
                //System.out.println(cell.getClass());
                result.append(cell.toString()).append(CLI.V_SEPARATOR);
                cell = cell.nextInRow;
            }
            result.setLength(result.length() - 1);
            return result.toString();
        }
        
        public void addCell(String text) {
            if (table != null) throw new UnsupportedOperationException("Not supported yet.");
            Cell newCell = new Cell(text, null, this);
            if (firstCell == null) firstCell = newCell;
            if (lastCell != null) lastCell.nextInRow = newCell;
            lastCell = newCell;
            size++;
        }
    }

    public static final class Cell {
        
        private String text;
        private Align align = Align.DEFAULT;
        //private Align defaultAlign = Align.LEFT;
        private Col col;
        private Row row;
        private Cell nextInCol;
        private Cell nextInRow;

        private Cell(String text) {
            setText(text);
        }
        
        private Cell(String text, Col col, Row row) {
            this(text);
            this.col = col;
            this.row = row;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Align getAlign() {
            return align;
        }

        public void setAlign(Align align) {
            this.align = align;
        }

//        public Align getDefaultAlign() {
//            return defaultAlign;
//        }
//
//        public void setDefaultAlign(Align defaultAlign) {
//            if (defaultAlign == Align.DEFAULT) defaultAlign = Align.LEFT;            
//            this.defaultAlign = defaultAlign;
//        }

        public Col getCol() {
            return col;
        }

        public Row getRow() {
            return row;
        }

        @Override
        public String toString() {
            if (col == null) return text;
            Align actual = align;
            //System.out.println(actual);
            if ((actual == Align.DEFAULT) && (row != null)) {
                actual = row.align;
            }
            if (actual == Align.DEFAULT) actual = col.align;
            if ((actual == Align.DEFAULT) && (col.table != null))
                actual = col.table.align;
            if (actual == Align.DEFAULT) actual = Align.LEFT;
            switch (actual) {
                case LEFT:   return CLI.alignLeft(text, col.getWidth());
                case CENTER: return CLI.alignCenter(text, col.getWidth());
                //case RIGHT: 
                default:     return CLI.alignRight(text, col.getWidth());
            }
        }        
    }
}
