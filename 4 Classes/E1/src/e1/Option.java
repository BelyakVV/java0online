package e1;


/**
 *
 * @author aabyodj
 */
public class Option {
    final String text;
    final Runnable action;

    public Option(String text, Runnable action) {
        this.text = text;
        this.action = action;
    }
}
