package dragon;

import static dragon.Gem.Size.BIG;
import static dragon.Gem.Size.NORMAL;
import static dragon.Gem.Size.SMALL;
import java.util.Scanner;

/**
 * Разновидность сокровища - драгоценный камень, а также элемент справочника 
 * видов драгоценных камней.
 * @author aabyodj
 */
public class Gem extends Treasure {
    /** 
     * Наименование - массив имён существительных в разной форме, например:
     * "бриллиант", "бриллиантом", "бриллиантами".
     */
    final String[] nouns;
    /** Вероятность создания камня данного вида НЕ в составе предмета */
    final int chance;
    /** Базовая цена камня данного вида */
    final long basePrice;
    /** Размер данного экземпляра камня */
    final Size size;
    /** Цена данного экземпляра камня */
    final long price;

    /**
     * Создать экземпляр камня заданного вида 
     * @param gem Вид камня
     */
    Gem(Gem gem) {
        this.nouns = gem.nouns;
        this.chance = gem.chance;
        this.basePrice = gem.basePrice;
        //Размер камня
        double random = Math.random();
        if (random < .33) {
            size = SMALL;
        } else if (random < .66) {
            size = NORMAL;
        } else size = BIG;
        //Цена камня в зависимости от размера
        this.price = (long) (basePrice * Math.pow(10, random * 2 - 1));
    }
    
    /**
     * Создать элемент справочника видов камней из строки текста
     * @param line Исходная строка
     */
    Gem(String line) {
        Scanner in = new Scanner(line).useDelimiter(Treasures.F_DELIMITER);
        //Базовая цена камня данного вида
        basePrice = in.nextLong();
        //Вероятность создания камня данного вида НЕ в составе предмета
        chance = in.nextInt();
        //Массив имён существительных
        nouns = in.next().split(Treasures.ARR_DELIMITER);
        //Для элемента справочника эти поля смысла не имеют
        price = 0;
        size = NORMAL;
    }

    /**
     * Получить наименование драгоценного камня
     * @return 
     */    
    @Override
    public String getName() {
        //Прилагательное и существительное в именительном падеже, единственном числе
        return size.adjectives[0] + nouns[0];
    }

    /**
     * Получить цену камня
     * @return 
     */
    @Override
    public long getPrice() {
        return price;
    }
    
    /** Прилагательные, характеризующие размер драгоценного камня */
    enum Size {
        SMALL(new String[]{"маленький ", "маленьким ", "маленькими "}),
        NORMAL(new String[]{"", "", ""}),
        BIG(new String[]{"большой ", "большим ", "большими "});
        
        public final String[] adjectives;
        
        private Size(String[] adjectives) {
            this.adjectives = adjectives;
        }
    }
}
