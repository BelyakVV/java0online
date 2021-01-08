package e2;

/**
 *   Создйте класс Test2 двумя переменными. Добавьте конструктор с входными 
 * параметрами. Добавьте конструктор, инициализирующий члены класса по 
 * умолчанию. Добавьте set- и get- методы для полей экземпляра класса.
 * @author aabyodj
 */
public class E2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Инициализация по умолчанию: " 
                + new Test2().toString());
        Test2 test2 = new Test2(0, 0);
        System.out.println("Инициализация нулевыми значениями: " 
                + test2.toString());
        test2.setA(8);
        System.out.println("Новое значение поля a: " + test2.getA());
        test2.setB(53);
        System.out.println("Новое значение поля b: " + test2.getB());
    }
    
}
