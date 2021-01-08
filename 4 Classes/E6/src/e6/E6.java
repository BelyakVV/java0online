package e6;

/**
 *   Составьте описание класса для представления времени. Предусмотрте 
 * возможности установки времени и изменения его отдельных полей (час, минута, 
 * секунда) с проверкой допустимости вводимых значений. В случае недопустимых 
 * значений полей поле устанавливается в значение 0. Создать методы изменения 
 * времени на заданное количество часов, минут и секунд.
 * @author aabyodj
 */
public class E6 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Time time = new Time(14, 8, 0);
        time.minusSeconds(7201);
        System.out.println("Часы: " + time.getHours() 
                + ", минуты: " + time.getMinutes() 
                + ", секунды: " + time.getSeconds());
    }
    
}
