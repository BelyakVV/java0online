package bouquet;

import static bouquet.Component.Color.BLACK;
import static bouquet.Component.Color.BLUE;
import static bouquet.Component.Color.CYAN;
import static bouquet.Component.Color.PURPLE;
import static bouquet.Component.Color.RED;
import static bouquet.Component.Color.WHITE;
import static bouquet.Component.Color.YELLOW;
import static bouquet.Component.Gender.FEMININE;
import static bouquet.Component.Gender.MASCULINE;

/**
 * Объект цветок - частный случай компонента цветочной композиции
 * @author aabyodj
 */
public abstract class Flower extends Component {
    /**
     * Создать цветок заданного цвета
     * @param color Индекс в массиве возможных цветов для данного цветка
     */
    Flower(int color) {
        super(color);
    }
    
    /**
     * Хризантема
     */
    public static class Chrisanthemum extends Flower {
        /**
         * Создать хризантему заданного цвета
         * @param color Индекс цвета в массиве возможных для хризантемы
         */
        public Chrisanthemum(int color) {
            super(color);
        }

        @Override
        Color[] getAvailableColors() {
            return new Color[]{WHITE, RED, YELLOW, PURPLE};
        }

        @Override
        int getGender() {
            return FEMININE.ordinal();
        }

        @Override
        String getName() {
            return "хризантема";
        }
    }
    
    /** Лилия */
    public static class Lily extends Flower {
        /**
         * Создать лилию заданного цвета
         * @param color Индекс цвета в массиве возможных для лилии
         */
        public Lily(int color) {
            super(color);
        }

        @Override
        Color[] getAvailableColors() {
            return new Color[]{BLACK, WHITE, RED, YELLOW, PURPLE};
        }

        @Override
        int getGender() {
            return FEMININE.ordinal();
        }

        @Override
        String getName() {
            return "лилия";
        }
    }

    /** Орхидея */
    public static class Orchid extends Flower {
        /**
         * Создать орхидею заданного цвета
         * @param color Индекс цвета в массиве возможных для орхидеи
         */
        public Orchid(int color) {
            super(color);
        }

        @Override
        Color[] getAvailableColors() {
            return new Color[]{WHITE, RED, YELLOW, BLUE, PURPLE};
        }

        @Override
        int getGender() {
            return FEMININE.ordinal();
        }

        @Override
        String getName() {
            return "орхидея";
        }
    }
    
    /** Гладиолус */
    public static class Gladiolus extends Flower {
        /**
         * Создать гладиолус заданного цвета
         * @param color Индекс в массиве возможных для гладиолуса цветов
         */
        public Gladiolus(int color) {
            super(color);
        }

        @Override
        Color[] getAvailableColors() {
            return new Color[]{WHITE, RED, YELLOW, PURPLE};
        }

        @Override
        int getGender() {
            return MASCULINE.ordinal();
        }

        @Override
        String getName() {
            return "гладиолус";
        }
    }
    
    /** Роза */
    public static class Rose extends Flower {
        /**
         * Создать розу заданного цвета
         * @param color Индекс в массиве возможных для розы расцветок
         */
        public Rose(int color) {
            super(color);
        }

        @Override
        Color[] getAvailableColors() {
            return new Color[]{BLACK, WHITE, RED, YELLOW, CYAN, PURPLE};
        }

        @Override
        int getGender() {
            return FEMININE.ordinal();
        }

        @Override
        String getName() {
            return "роза";
        }
    }
}