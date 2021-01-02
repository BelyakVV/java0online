package e5;

/**
 *   Опишите класс, реализующий десятичный счетчик, который может увеличивать 
 * или уменьшать свое значение на единицу в заданном диапазоне. 
 * Предусмотрите инициализацию счетчика значениями по умолчанию и произвольными 
 * значениями. Счетчик имеет методы увеличения и уменьшения состояния, и метод
 * позволяющее получить его текущее состояние. Написать код, демонстрирующий все 
 * возможности класса.
 * @author aabyodj
 */
public class E5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DecimalCounter counter = new DecimalCounter();
        System.out.println("Значение счётчика по умолчанию: " + counter.getValue());
        System.out.print("Границы по умолчанию:");
        System.out.print(" нижняя = " + counter.getLoBound());
        System.out.println(", верхняя = " + counter.getHiBound());
        counter.setValue(Long.MIN_VALUE);
        System.out.println("Новое значение: " + counter.getValue());
        counter.inc();
        System.out.println("Увеличение на 1: " + counter.getValue());
        counter.dec();
        System.out.println("Уменьшение на 1: " + counter.getValue());
        counter = new DecimalCounter(72635);
        System.out.println("Новый счётчик с заданным значением: " 
                + counter.getValue());
        counter = new DecimalCounter(-85, -1000, 50);
        System.out.println("Новый счётчик с заданным значением и границами:");
        System.out.println("Значение: " + counter.getValue());
        System.out.print("Границы: нижняя = " + counter.getLoBound());
        System.out.println(", верхняя = " + counter.getHiBound());
        System.out.println("Попытка превысить верхнюю границу...");
        counter.setValue(counter.getHiBound() + 1);
    }
    
}
