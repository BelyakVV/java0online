package task1;

import homelib.Library;

/**
 *
 * @author aabyodj
 */
public class Task1 {
    
    static Library library = new Library("books.txt", "authors.txt");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(library);
    }
    
}
