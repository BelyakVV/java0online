package oop3;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс Календарь с внутренним классом, с помощью объектов которого можно 
 * хранить информацию о выходных и праздничных днях.
 * @author aabyodj
 */
class Calendar {
    /** Список праздников */
    final List<Holiday> holidays;
    /** Признак необходимости сортировки списка праздников */
    boolean needSort;
      
    /**
     * Создать календарь из связного списка праздников
     * @param holidays Список праздников
     */
    Calendar(LinkedList<Holiday> holidays) {
        this.holidays = holidays;
        needSort = true;
    }
    
    /** Создать пустой календарь */
    public Calendar() {
        this(new LinkedList<>());
    }
    
    /**
     * Создать календарь из списка праздников
     * @param holidays Список праздников
     */
    public Calendar(List<Holiday> holidays) {
        this(new LinkedList<>(holidays));
    }
    
    /**
     * Создать календарь из массива праздников
     * @param holidays Массив праздников
     */
    public Calendar(Holiday[] holidays) {
        this(Arrays.asList(holidays));
    }
    
    /**
     * Добавить праздник в календарь
     * @param holiday Праздник
     * @return true, если успешно
     */
    public boolean add(Holiday holiday) {
        //Список более не полностью отсортирован
        needSort = true;
        return holidays.add(holiday);
    }
    
    /**
     * Добавить список праздников в календарь
     * @param holidays Список праздников
     * @return true, если успешно
     */
    public boolean addAll(List<Holiday> holidays) {
        needSort = true;
        return this.holidays.addAll(holidays);
    }
    
    /**
     * Добавить массив праздников в календарь
     * @param holidays Массив праздников
     * @return true, если успешно
     */            
    public boolean addAll(Holiday[] holidays) {
        return addAll(Arrays.asList(holidays));
    }

    @Override
    public String toString() {
        //Отсортировать праздники по дате
        sort();
        StringBuilder result = new StringBuilder();
        for(var holiday: holidays) {
            result.append(holiday).append('\n');
        }
        //Удалить последний перевод строки
        if (result.length() > 0) result.setLength(result.length() - 1);
        return result.toString();
    }

    /** Отсортировать праздники в списке по дате */
    public void sort() {
        //Надо ли сортировать?
        if (!needSort) return;
        holidays.sort((h1, h2) -> h1.compareTo(h2));
        //В следующий раз сортировать не надо
        needSort = false;
    }
    
    /**
     * Внутренний класс, с помощью объектов которого можно хранить информацию о 
     * выходных и праздничных днях.
     */
    public static class Holiday implements Comparable {
        /** Дата праздника */
        public final LocalDate date;
        /** Название праздника */
        public final String name;
        
        /**
         * Создать праздник с заданной датой и названием
         * @param date Дата праздника
         * @param name Название праздника
         */
        public Holiday(LocalDate date, String name) {
            this.date = date;
            this.name = name;
        }      
        
        @Override
        public int compareTo(Object o) {
            return date.compareTo(((Holiday) o).date);
        }
        
        @Override
        public String toString() {
            return date 
                    + ": " + name;
        }
    }
}
