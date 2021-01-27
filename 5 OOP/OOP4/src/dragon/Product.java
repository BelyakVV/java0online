package dragon;

import java.util.Scanner;

/**
 * Разновидность сокровища - рукотворный предмет
 * @author aabyodj
 */
public class Product extends Treasure {
    /** Тип предмета */
    final Type type;
    /** Материал */
    final Material material;
    /** Драгоценный камень (камни) в составе предмета */
    final Gem gem;
    /** Максимальное количество драгоценных камней в составе предмета */
    final static int MAX_GEMS_COUNT = 10;
    /** Коэффициент цены для предметов с камнями */
    final static double GEM_FACTOR = 1.2;
    /** Базовая цена предмета без учёта стоимости материала и украшений */
    final double basePrice;
    /** Полная цена предмета */
    final long price;
    
    /**
     * Создать предмет заданного типа из заданного материала с заданным камнем
     * @param type Тип предмета
     * @param material Материал
     * @param gem Драгоценный камень
     */
    Product(Type type, Material material, Gem gem) {
        this.type = type;
        this.material = material;
        this.gem = gem;
        //Насколько базовая цена больше минимальной
        double priceDelta = (type.maxPrice - type.minPrice) * Math.random();
        //Базовая цена
        basePrice = type.minPrice + priceDelta;
        //Учитываем стоимость материала
        double pr = basePrice * material.cost;
        //Есть ли в составе драгоценный камень?
        if (gem != null) {
            //Камень один или несколько?
            if (type.gemNumber > 1) {
                //Случайное количество от 2 до MAX_GEMS_COUNT
                pr += gem.getPrice() * (Math.random() * (MAX_GEMS_COUNT - 2) + 2);
            } else {
                //Один камень
                pr += gem.getPrice();
            }
            //Для инкрустированных предметов стоимость работы выше
            pr = pr * GEM_FACTOR;
        }
        this.price = (long) pr;
    }    
    
    /**
     * Получить наименование предмета
     * @return 
     */
    @Override
    public String getName() {
        StringBuilder result = new StringBuilder();
        //Наименование материала в виде прилагательного в нужной форме
        result.append(material.adjectives[type.genderNumber.ordinal()]);
        //Название типа предмета
        result.append(' ').append(type.name);
        if (gem != null) { //Если в составе есть камень
            //Наименование размера камня в нужной форме
            result.append(" с ").append(gem.size.adjectives[type.gemNumber]);
            //Наименование вида камня в нужной форме
            result.append(gem.nouns[type.gemNumber]);
        }
        return result.toString();
    }

    /**
     * Получить цену предмета
     * @return 
     */
    @Override
    public long getPrice() {
        return price;
    }
    
    /** Материал, из которого может быть изготовлен предмет */
    static class Material {
        /** Цена материала */
        final long cost;
        /** 
         * Наименование материала в виде массива прилагательных в разных формах:
         * "золотой", "золотая", "золотое", "золотые" и т.д.
         */
        final String[] adjectives;
        
        /**
         * Создание материала из строки текста
         * @param line Исходная строка
         */
        Material(String line) {
            Scanner in = new Scanner(line).useDelimiter(Treasures.F_DELIMITER);
            //Цена
            cost = in.nextLong();
            //Массив прилагательных
            adjectives = in.next().split(Treasures.ARR_DELIMITER);
        }
    }
    
    /** Число и род имени прилагательного */
    enum GenderNumber {
        MASCULINE, FEMININE, NEUTER, PLURAL
    }
   
    /** Тип предмета */
    static class Type {
        /** Наименование типа */
        final String name;
        /** Требуемое число и род имени прилагательного */
        final GenderNumber genderNumber;
        /** Минимальная базовая цена предмета данного типа */
        final int minPrice;
        /** Максимальная базовая цена предмета данного типа */
        final int maxPrice;
        /** Массив вероятностей создания предметов данного типа из разных материалов */
        final int[] materialChances;
        /** Число материалов с ненулевой вероятностью создания из них предмета данного типа х 100 */
        final int totalMaterialChance;
        /** Массив вероятностей добавления камней разного вида в предмет данного типа */
        final int[] gemChances;
        /** Число видов камней с ненулевой вероятностью добавления в предмет данного типа х 100 */
        final int totalGemChance;
        /** Возможное число камней: 1 = 1, 2 = несколько, 0 = не определено */
        final int gemNumber;

        /**
         * Создать элемент справочника типов предметов
         * @param name Имя типа
         * @param price Базовая цена
         * @param materialChances Массив вероятностей создания предметов данного 
         * типа из разных материалов
         * @param genderNumber Требуемое число и род имени прилагательного
         */
        Type(String name, int price, int[] materialChances, int genderNumber) {
            this.name = name;
            this.minPrice = price;
            this.maxPrice = price;
            this.materialChances = materialChances;
            totalMaterialChance = calcTotalMaxChance(materialChances);
            this.genderNumber = GenderNumber.values()[genderNumber];
            //Без драгоценных камней
            gemChances = null;
            totalGemChance = 0;
            gemNumber = 0;
        }
        
        /**
         * Создать элемент справочника типов предметов из строки текста
         * @param line Исходная строка
         */
        Type(String line) {
            Scanner in = new Scanner(line).useDelimiter(Treasures.F_DELIMITER);
            //Наименование
            name = in.next();
            //Минимальная базовая цена
            minPrice = in.nextInt();
            //Максимальная базовая цена
            maxPrice = in.nextInt();
            //Массив вероятностей создания предметов данного типа из разных материалов
            materialChances = integers(in.next().split(Treasures.ARR_DELIMITER));
            //Число материалов с ненулевой вероятностью создания из них предмета данного типа * 100
            totalMaterialChance = calcTotalMaxChance(materialChances);
            //Требуемое число и род имени прилагательного
            genderNumber = GenderNumber.values()[in.nextInt()];
            //Массив вероятностей добавления камней разного вида в предмет данного типа
            int[] gc;
            //Число видов камней с ненулевой вероятностью добавления в предмет данного типа * 100
            int tgc;
            //Возможное число камней: 1 = 1, 2 = несколько, 0 = не определено
            int gn;
            try { //Если проситать - то только всё сразу
                gc = integers(in.next().split(Treasures.ARR_DELIMITER));
                tgc = calcTotalMaxChance(gc);
                gn = in.nextInt();
            } catch (Exception e) {
                gc = null;
                tgc = 0;
                gn = 0;
            }
            gemChances = gc;
            totalGemChance = tgc;
            gemNumber = gn;
        }

        /**
         * В массиве вероятностей все ненулевые элементы принять за 100 и
         * подсчитать сумму
         * @param chances Массив вероятностей
         * @return Сумма ненулевых элементов, принятых за 100
         */
        private int calcTotalMaxChance(int[] chances) {
            int result = 0;
            for (int chance: chances) {
                if (chance > 0)
                    result += 100;
            }
            return result;
        }
        
        /**
         * В массиве строк каждый элемент преобразовать в целое число
         * @param strings Массив строк
         * @return Массив целых чисел
         */
        private int[] integers(String[] strings) {
            int[] result = new int[strings.length];
            for (int i = 0; i < strings.length; i++) {
                result[i] = Integer.parseInt(strings[i]);
            }
            return result;
        }
    }
}
