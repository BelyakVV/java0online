package oop2;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс Payment с внутренним классом, с помощью объектов которого можно 
 * сформировать покупку из нескольких товаров.
 * @author aabyodj
 */
public class Payment {
    /** Список товаров */
    List<Item> items;
    
    /** Пустая покупка без товаров */
    public Payment() {
        this(null);
    }
    
    /**
     * Покупка с заданным списком товаров
     * @param items Список товаров
     */
    public Payment(List<Item> items) {
        if (items != null) {
            this.items = items;
        } else {
            this.items = new LinkedList<>();
        }
    }
    
    @Override
    public String toString() {
        if (items.size() < 1) return "Список товаров пуст.";
        //Общая стоимость
        int totalCost = 0;
        StringBuilder result = new StringBuilder("Список товаров:\n");
        for (var item: items) {
            totalCost += item.cost();
            result.append(item).append('\n');
        }
        result.append("Общая стоимость: ").append(totalCost);
        return result.toString();
    }
    
    /**
     * Добавить товар в список
     * @param item Товар
     * @return 
     */
    public boolean add(Item item) {
        return items.add(item);
    }
    
    /**
     * Посчитать общую стоимость
     * @return 
     */
    public int totalCost() {
        int result = 0;
        for (var item: items) {
            result += item.cost();
        }
        return result;
    }

    /** Пункт в списке покупок */
    public static class Item {
        /** Наименование */
        public final String name;
        /** Цена */
        public final int price;
        /** Количество */
        private int quantity;

        /**
         * Создать пункт в списке покупок
         * @param name Наименование
         * @param price Цена
         * @param quantity Количество
         */
        public Item(String name, int price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
        
        /**
         * Создать пункт в списке покупок. Наименование и цена заданы, 
         * количество принимается за 1.
         * @param name Наименование
         * @param price Цена
         */
        public Item(String name, int price) {
            this(name, price, 1);
        }

        /**
         * Получить количество
         * @return 
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * Задать количество
         * @param quantity Количество
         */
        public void setQuantity(int quantity) {
            if (quantity > 0) {
                this.quantity = quantity;
            } else this.quantity = 0;
        }
        
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(name);
            result.append(" (").append(price).append(" x ");
            result.append(quantity).append(" шт = ");
            result.append(cost()).append(')');
            return result.toString();
        }
        
        /**
         * Посчитать стоимость
         * @return 
         */
        public int cost() {
            return price * quantity;
        }
    }
}
