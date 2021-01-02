package e6;

/**
 *   Составьте описание класса для представления времени. Предусмотрте 
 * возможности установки времени и изменения его отдельных полей (час, минута, 
 * секунда) с проверкой допустимости вводимых значений. В случае недопустимых 
 * значений полей поле устанавливается в значение 0. Создать методы изменения 
 * времени на заданное количество часов, минут и секунд.
 * @author aabyodj
 */
public final class Time {

    /**
     * Инициализация полей значениями по умолчанию: hours = 0, minutes = 0,
     * seconds = 0.
     */
    public Time() {
        this(0, 0, 0);
    }
    
    /**
     * Инициализация полей заданными значениями.
     * @param hours Часы
     * @param minutes Минуты
     * @param seconds Секунды
     */
    public Time(int hours, int minutes, int seconds) {
        set(hours, minutes, seconds);
    }
    
    /** Часы */
    private int hours;
    
    /** Минуты */
    private int minutes;
    
    /** Секунды */
    private int seconds;

    /**
     * Установка полей в заданные значения
     * @param hours Часы
     * @param minutes Минуты
     * @param seconds Секунды
     */
    public void set(int hours, int minutes, int seconds) {
        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);        
    }
    
    /**
     * Возврат количества часов
     *
     * @return Часы
     */
    public int getHours() {
        return hours;
    }

    /**
     * Установка количества часов
     *
     * @param hours Новое значение часов
     */
    public void setHours(int hours) {
        if (hours < 0) hours = 0;
        this.hours = hours;
    }
    
    /**
     * Уменьшение количества часов
     * @param delta Интервал, на который следует уменьшить количество часов
     */
    public void minusHours(int delta) {
        plusHours(-delta);        
    }
    
    /**
     * Увеличение количества часов
     * @param delta Интервал, на который следует увеличить количество часов
     */
    public void plusHours(int delta) {
        setHours(hours + delta);
    }

    /**
     * Возврат количества минут
     * @return Минуты
     */
    public int getMinutes() {
        return minutes;
    }
        
    /**
     * Уменьшение количества минут
     * @param delta Интервал, на который следует уменьшить количество минут
     */
    public void minusMinutes(int delta) {
        plusMinutes(-delta);
    }
    
    /**
     * Увеличение количества минут
     * @param delta Интервал, на который следует увеличить количество минут
     */
    public void plusMinutes(int delta) {
        minutes += delta;
        if (minutes < 0) {
            plusHours((minutes - 59) / 60);
            minutes = (minutes + 1) % 60 + 59;
        } else {
            plusHours(minutes / 60);
            minutes = minutes % 60;
        }        
    }

    /**
     * Установка количества минут
     * @param minutes Новое значение минут
     */
    public void setMinutes(int minutes) {
        if (minutes < 0 || minutes > 59) minutes = 0;
        this.minutes = minutes;
    }

    /**
     * Возврат количества секунд
     * @return Секунды
     */
    public int getSeconds() {
        return seconds;
    }
    
    /**
     * Уменьшение количества секунд
     * @param delta Интервал, на который следует уменьшить количество секунд
     */
    public void minusSeconds(int delta) {
        plusSeconds(-delta);
    }
    
    /**
     * Увеличение количества секунд
     * @param delta Интервал, на который следует увеличить количество секунд
     */
    public void plusSeconds(int delta) {        
        seconds += delta;
        if (seconds < 0) {
            plusMinutes((seconds - 59) / 60);
            seconds = (seconds + 1) % 60 + 59;
        } else {
            plusMinutes(seconds / 60);
            seconds = seconds % 60;
        }        
    }

    /**
     * Установка количества секунд
     * @param seconds Новое значение секунд
     */
    public void setSeconds(int seconds) {
        if (seconds < 0 || seconds > 59) seconds = 0;
        this.seconds = seconds;
    }

}
