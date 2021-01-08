package e2;

/**
 * Класс с двумя переменными
 * @author aabyodj
 */
public class Test2 {

    private int a;
    private int b;

    /**
     * Создание экземпляра класса и инициализация полей значениями по умолчанию:
     * a = 1, b = 2
     */
    public Test2() {
        this(1, 2);
    }

    /**
     * Создание и инициализация экземпляра класса
     * @param a
     * @param b
     */
    public Test2(int a, int b) {
        this.a = a;
        this.b = b;
    }

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

    @Override
    public String toString() {
        return "Test2{" + "a=" + a + ", b=" + b + '}';
    }
}
