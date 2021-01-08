package e1;

/**
 *
 * @author aabyodj
 */
public class Test1 {
   
    private int a;
    private int b;

    /**
     * Get the value of a
     *
     * @return the value of a
     */
    public int getA() {
        return a;
    }

    /**
     * Set the value of a
     *
     * @param a new value of a
     */
    public void setA(int a) {
        this.a = a;
    }

    /**
     * Get the value of b
     *
     * @return the value of b
     */
    public int getB() {
        return b;
    }

    /**
     * Set the value of b
     *
     * @param b new value of b
     */
    public void setB(int b) {
        this.b = b;
    }
 
    /** Печать текущих значений полей a и b */
    public void print() {
        System.out.println("a = " + a + ", b = " + b);
    }
    
    /** 
     * Возврат суммы полей a и b
     * @return сумма полей
     */
    public int getSum() {
        return a + b;
    }
    
    /**
     * Возврат большего из значений a или b
     * @return большее значение
     */
    public int getMax() {
        return a > b ? a : b;
    }
}
