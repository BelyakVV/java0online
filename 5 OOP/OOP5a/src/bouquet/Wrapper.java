package bouquet;

import static bouquet.Component.Color.MAGENTA;
import static bouquet.Component.Color.NONE;
import static bouquet.Component.Color.RED;
import static bouquet.Component.Color.WHITE;
import static bouquet.Component.Color.YELLOW;
import static bouquet.Component.Gender.FEMININE;
import static bouquet.Component.Gender.MASCULINE;

/**
 * Упаковка - частный случай компонента цветочной композиции.
 * @author aabyodj
 */
public abstract class Wrapper extends Component {
    /**
     * Создать упаковку заданного цвета
     * @param color Индекс в массиве возможных для данной упаковки расцветок
     */
    Wrapper(int color) {
        super(color);
    }
    
    /** Целлулоидная упаковка */
    static class Celluloid extends Wrapper {
        /**
         * Создать целлулоидную упаковку заданного цвета
         * @param color Индекс в массиве возможных для целлулоида расцветок
         */
        public Celluloid(int color) {
            super(color);
        }

        @Override
        Color[] getAvailableColors() {
            return new Color[]{NONE, RED};
        }

        @Override
        int getGender() {
            return MASCULINE.ordinal();
        }

        @Override
        String getName() {
            return "целлулоид";
        }
    }
    
    /** Бумажная упаковка */
    static class Paper extends Wrapper {
        /**
         * Создать бумажную упаковку заданного цвета
         * @param color Индекс в массиве возможных для бумаги расцветок
         */
        public Paper(int color) {
            super(color);
        }

        @Override
        Color[] getAvailableColors() {
            return new Color[]{WHITE, RED, YELLOW, MAGENTA};
        }

        @Override
        int getGender() {
            return FEMININE.ordinal();
        }

        @Override
        String getName() {
            return "бумага";
        }
    }
}
