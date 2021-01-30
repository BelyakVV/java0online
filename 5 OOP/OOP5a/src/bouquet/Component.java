package bouquet;

/**
 * Компонент цветочной композиции - цветок или упаковка.
 * @author aabyodj
 */
public abstract class Component {
    /** Цвет */
    final Color color;
    
    /**
     * Создать компонент заданного цвета
     * @param color Индекс в массиве возможных для данного компонента расцветок
     */
    Component(int color) {
        this.color = getAvailableColors()[color];
    }    
    
    @Override
    public String toString() {
        //Цвет + название
        return color.getAdjective(getGender()) + ' ' + getName();
    }    
    
    /**
     * Получить массив возможных для данного компонента расцветок
     * @return Массив расцветок
     */
    abstract Color[] getAvailableColors();
    
    /**
     * Получить род имени существительного - названия компонента
     * @return 0 - мужской род, 1 - женский, 2 - средний
     */
    abstract int getGender();

    /**
     * Получить название данного компонента, без цвета.
     * @return 
     */
    abstract String getName();
    
    /** Расцветки цветков и упаковки */
    public enum Color {
        NONE(new String[]{
            "бесцветный", "бесцветная", "бесцветное"
        }),
        BLACK(new String[]{
            "чёрный", "чёрная", "чёрное"
        }),
        WHITE(new String[]{
            "белый", "белая", "белое"
        }),
        RED(new String[]{
            "красный", "красная", "красное"
        }),
        YELLOW(new String[]{
            "жёлтый", "жёлтая", "жёлтое"
        }),
        GREEN(new String[]{
            "зелёный", "зелёная", "зелёное"
        }),
        CYAN(new String[]{
            "голубой", "голубая", "голубое"
        }),
        BLUE(new String[]{
            "синий", "синяя", "синее"
        }),        
        PURPLE(new String[]{
            "фиолетовый", "фиолетовая", "фиолетовое"
        }),        
        MAGENTA(new String[]{
            "малиновый", "малиновая", "малиновое"
        });
        
        final String[] adjectives;

        Color(String[] adjectives) {
            this.adjectives = adjectives;
        }
        
        public String getAdjective(int gender) {
            return adjectives[gender];
        }
        
        @Override
        public String toString() {
            return adjectives[0];
        }
    }
    
    /** Род имён существительных и прилагательных */
    enum Gender {
        MASCULINE, FEMININE, NEUTRAL
    }
}
