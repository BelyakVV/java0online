package e3;

/**
 *   Информация о студенте:
 * фамилия и инициалы, 
 * номер группы, 
 * успеваемость (массив из пяти элементов)
 * @author aabyodj
 */
public class Student {
    /** Размер массива marx (= количество хранимых оценок) */
    public static final int ENGELS = 5;
    
    /** Фамилия */
    private String surname;
    /** Инициалы */
    private String initials;
    /** Номер группы */
    private int group;
    /** Оценки */
    private final int[] marx = new int[ENGELS];

    public Student(String surname, String initials, int group, int[] marx) {
        this.surname = surname;
        this.initials = initials;
        this.group = group;
        this.setMarx(marx);
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getMark(int i) {
        return marx[i];
    }
    
    public void setMark(int mark, int i) {
        marx[i] = mark;
    }
    
    public int[] getMarx() {
        int[] result = new int[ENGELS];
        System.arraycopy(marx, 0, result, 0, ENGELS);
        return result;
    }

    public void setMarx(int[] marx) {
        System.arraycopy(marx, 0, this.marx, 0, ENGELS);
    }
}
