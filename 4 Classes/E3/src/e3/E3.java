package e3;

/**
 *   Создайте класс с именем Student, содержащий поля: фамилия и инициалы, 
 * номер группы, успеваемость (массив из пяти элементов). Создайте массив из 
 * десяти элементов такого типа. Добавьте возможность вывода фамилий и номеров 
 * групп студентов, имеющих оценки, равные только 9 или 10.
 * @author aabyodj
 */
public class E3 {
    /** Минимальная оценка отличника */
    static final int MIN_MARK = 9;
    
    /** Массив студентов */
    private static final Student[] students = {
        new Student("Иванов", "ИИ", 123456, new int[]{3, 10, 8, 6, 9}),
        new Student("Петров", "ПП", 123456, new int[]{10, 9, 9, 10, 10}),
        new Student("Сергеев", "СС", 654321, new int[]{8, 8, 3, 7, 6}),
        new Student("Фомин", "ФФ", 654321, new int[]{9, 10, 9, 9, 9}),
        new Student("Григорьев", "ГГ", 123456, new int[]{6, 8, 7, 6, 7}),
        new Student("Евгеньев", "ЕЕ", 123456, new int[]{5, 7, 9, 9, 6}),
        new Student("Ким", "ЧЫ", 000000, new int[]{0, 0, 0, 0, 0}),
        new Student("Козлов", "БЕ", 741852, new int[]{10, 10, 9, 10, 9}),
        new Student("Баранов", "МЕ", 741852, new int[]{9, 10, 10, 9, 9}),
        new Student("Быков", "МУ", 741852, new int[]{10, 9, 9, 10, 9}),
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean listIsEmpty = true;
        studentsLoop:
        for (var student: students) {
            for (var mark: student.getMarx()) {
                if (mark < MIN_MARK) continue studentsLoop;
            }
            if (listIsEmpty) {
                System.out.println("Список отличников:\n------------------");
                listIsEmpty = false;
            }
            System.out.println(student.getSurname() 
                    + ", группа " + student.getGroup());
        }
        if (listIsEmpty) System.out.println("Отличников нет.");
    }
    
}
