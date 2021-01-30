package oop2;

import oop2.Payment.Item;

/**
 * 5. Basics of OOP. Задача 2.
 * Создать класс Payment с внутренним классом, с помощью объектов которого можно 
 * сформировать покупку из нескольких товаров.
 * @author aabyodj
 */
public class OOP2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Payment payment = new Payment();
        payment.add(new Item("Телевизор", 500, 1));
        payment.add(new Item("Чипсы", 4, 3));
        payment.add(new Item("Пиво", 3, 5));
        System.out.println(payment);
    }
    
}
