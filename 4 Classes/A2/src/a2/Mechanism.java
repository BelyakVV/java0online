package a2;

/**
 * Механизм: автомобиль и его составные части.
 * @author aabyodj
 */
interface Mechanism {
    /** Запустить механизм */
    void start() throws IllegalStateException;
    /** Остановить механизм */
    void stop() throws IllegalStateException;
}
