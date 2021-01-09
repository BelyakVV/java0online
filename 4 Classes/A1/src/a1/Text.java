package a1;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   Класс Текст: Предложение, Слово. Методы: дополнить текст, вывести на 
 * консоль текст, заголовок текста.
 * @author aabyodj
 */
public final class Text {
    /** Заголовок текста */
    private Sentence title;
    /** Собственно текст */
    private final LinkedList<Sentence> text = new LinkedList<>();
        
    /** Точка, вопросительный знак, восклицательный знак */
    static final String PQX = "\\.\\?\\!";
    /** Начало предложения */
    static final String SOS = "[^\\s" + PQX + ']';
    /** Конец предложения */
    static final String EOS = '[' + PQX + ']';
    /** Поиск предложений */
    static final Pattern P_SENT = Pattern.compile(SOS + ".*?" + EOS + '+');

    /**
     * Создать объект текст с заданным содержимым и заголовком
     * @param title Заголовок текста
     * @param text Собственно текст
     */
    public Text(String title, String text) {
        setTitle(title);
        append(text);
    }

    /**
     * Дополнить текст
     * @param string Чем дополнить
     */
    public void append(String string) {
        //Последнее предложение из уже имеющихся
        Sentence last = text.peekLast();
        if (last != null) {
            if (last.isIncomplete()) { //Не завершается точкой, вопросительным либо восклицательным знаком
                //Будем дополнять его
                string = last.toString() + ' ' + string;
                text.removeLast();
            }
        }
        //Поиск предложений
        Matcher mSent = P_SENT.matcher(string);
        //Конец последнего найденного предложения
        int lastEnd = 0;
        while (mSent.find()) {
            lastEnd = mSent.end();
            text.add(new Sentence(mSent.group()));
        }        
        if (lastEnd < string.length()) { //После последнего предложения что-то есть
            text.add(new Sentence(string.substring(lastEnd)));
        }
    }
    
    /**
     * Получить заголовок текста
     * @return 
     */
    public String getTitle() {
        return title.toString();
    }
    
    /**
     * Установить заголовок текста
     * @param title 
     */
    public void setTitle(String title) {
        this.title = new Sentence(title);
    }
    
    /**
     * Получить содержимое текста
     * @return 
     */
    public String getText() {
        if (text.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        //Добавляем все предложения
        text.forEach(sentence -> result.append(sentence).append(' '));
        //Удаляем последний пробел
        result.setLength(result.length() - 1);
        return result.toString();
    }

    @Override
    public String toString() {
        return getTitle() + "\n\n" + getText();
    }
    
    /** Класс Предложение */
    private static class Sentence {
        /** Слова */
        private final LinkedList<Word> words = new LinkedList<>();
        
        /** 
         * Поиск слов. Слова включают в себя знаки препинания, стоящие сразу 
         * после них 
         */
        private static final Pattern P_WORD = Pattern.compile("\\S+");

        /** 
         * Создать объект Предложение из строки
         * @param string 
         */
        private Sentence(String string) {
            //Поиск слов
            Matcher mWord = P_WORD.matcher(string);
            while (mWord.find()) {
                words.add(new Word(mWord.group()));
            }
        } 
        
        /**
         * Признак незавершённости предложения: есть ли в конце точка, 
         * вопросительный или восклицательный знак
         * @return true, если предложение не завершено
         */
        public boolean isIncomplete() {
            if (words.isEmpty()) return true;
            return !words.peekLast().isLastInSentence();
        }

        @Override
        public String toString() {
            if (words.isEmpty()) return "";
            StringBuilder result = new StringBuilder();
            //Добавляем все слова
            words.forEach(word -> result.append(word).append(' '));
            //Убираем последний пробел
            result.setLength(result.length() - 1);
            return result.toString(); 
        }
    }

    /** Класс Слово */
    private static class Word {
        /** Собственно слово */
        private final String word;

        /**
         * Создать объект слово
         * @param word 
         */
        private Word(String word) {
            this.word = word;
        }

        @Override
        public String toString() {
            return word;
        }

        /**
         * Есть ли в конце завершающий предложение знак препинания: точка,
         * вопросительный или восклицательный знак
         * @return true если есть завершающий знак
         */
        public boolean isLastInSentence() {
            if (word.isBlank()) return false;
            return P_SENT.matcher(word).matches();
        }
    }    
}
