package dragon;

import cli.Table;
import dragon.Product.Material;
import dragon.Product.Type;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Агрегатор массива сокровищ
 * @author aabyodj
 */
public final class Treasures {
    /** Массив сокровищ */
    final Treasure[] treasures;
    /** Справочник драгоценных камней */
    final Gem[] gems;
    /** Справочник материалов */
    final Material[] materials;
    /** Справочник типов сокровищ */
    final Type[] types;
    /** Число типов предметов х число возможных материалов для каждого х 100 */
    int totalProductChance;
    /** Количество видов камней х 100 */
    int totalGemChance;
    
    /**
     * Загрузить параметры генерации из файлов и сгенерировать сокровища
     * @param treasuresCount Количество сокровищ
     * @param gemsFN Имя файла драгоценных камней
     * @param materialsFN Имя файла материалов
     * @param typesFN Имя файла типов предметов
     * @throws FileNotFoundException 
     */
    public Treasures(int treasuresCount, 
            String gemsFN, String materialsFN, String typesFN
    ) throws FileNotFoundException {
        //Загрузить справочник камней
        gems = loadGems(gemsFN);
        //Загрузить справочник материалов
        materials = loadMaterlials(materialsFN);
        //Загрузить справочник типов предметов
        types = loadTypes(typesFN);
        //Число возможных камней и типов предметов из всех материалов * 100 
        int totalChance = totalProductChance + totalGemChance;
        //Счётчик сокровищ
        int i = 0;
        treasures = new Treasure[treasuresCount];
        treasures[i++] = new Product(getType("унитаз"), getMaterial("золотой"), null);
        treasures[i++] = new Product(getType("рука"), getMaterial("бриллиантовый"), null);
        creation:
        while (i < treasuresCount) {
            //Случайный вид камня или предмета
            int roll = (int) (Math.random() * totalChance);
            //Смотрим, что получилось. Сначала предметы
            for (var type: types) {
                //Для каждого типа перебираем возможные для него материалы
                for (int j = 0; j < Math.min(type.materialChances.length, materials.length); j++) {
                    roll -= type.materialChances[j];
                    if (roll < 0) { //Тип предмета найден
                        //Сначала без камня
                        Gem gem = null;
                        //Возможно ли добавить камень в этот предмет?
                        if (type.totalGemChance > 0) {
                            //Случайный вид камня
                            roll = (int) (Math.random() * type.totalGemChance);
                            //Смотрим, что получилось. Перебираем возможные виды камней
                            for (int g = 0; g < Math.min(type.gemChances.length, gems.length); g++) {
                                roll -= type.gemChances[g];
                                if (roll < 0) { //Это он
                                    //Создать камень заданного типа
                                    gem = new Gem(gems[g]);
                                    break;
                                }
                            }
                        }
                        //Создать предмет найденного типа с найденным камнем
                        treasures[i++] = new Product(type, materials[j], gem);
                        continue creation;
                    }
                }
            }
            //Ни один тип предмета не подошёл. Перебираем виды камней
            for (var gem: gems) {
                roll -= gem.chance;
                if (roll < 0) { //Это он
                    //Создать камень этого вида
                    treasures[i++] = new Gem(gem);
                    continue creation;
                }
            }
        }
        //Отсортировать по возрастанию стоимости
        Arrays.sort(treasures);
    }

    /**
     * Создать новый экземпляр агрегатора
     * @param treasures Массив сокровищ
     * @param gems Справочник видов камней
     * @param materials Справочник материалов
     * @param types Справочник типов предметов
     * @param totalProductChance Число типов предметов х число возможных 
     * материалов для каждого х 100
     * @param totalGemChance Количество видов камней х 100
     */
    Treasures(Treasure[] treasures, Gem[] gems, Material[] materials, Type[] types, 
            int totalProductChance, int totalGemChance) {
        this.treasures = treasures;
        this.gems = gems;
        this.materials = materials;
        this.types = types;
        this.totalProductChance = totalProductChance;
        this.totalGemChance = totalGemChance;
    }
    
    /**
     * Получить материал по прилагательному мужского рода, например "золотой"
     * @param adjective Прилагательное
     * @return Материал
     */
    Material getMaterial(String adjective) {
        for (var material: materials) {
            if (material.adjectives[0].equalsIgnoreCase(adjective)) return material;
        }
        throw new NoSuchElementException();
    }
    
    /**
     * Получить тип предмета по его названию
     * @param name Название типа предмета
     * @return Тип предмета
     */
    Type getType(String name) {
        for (var type: types) {
            if (type.name.equalsIgnoreCase(name)) return type;
        }
        throw new NoSuchElementException();
    }
    
    /**
     * Получить самое дорогое по стоимости сокровище
     * @return 
     */
    public Treasure getMostExpensive() {
        if (treasures.length < 1) return null;
        return treasures[treasures.length - 1];
    }
    
    /**
     * Выбрать сокровища на заданную сумму. Жадный алгоритм: на каждой итерации
     * выбираем из оставшихся самое дорогое из подходящих.
     * @param total Суммарная стоимость сокровищ
     * @return Новый экземпляр агрегатора с сокровищами
     */
    public Treasures selectTotal(long total) {
        LinkedList<Treasure> result = new LinkedList<>();
        //Массив отсортирован по возрастанию
        for (int i = treasures.length - 1; i >= 0; i--) {
            if (treasures[i].getPrice() <= total) {
                result.add(treasures[i]);
                total -= treasures[i].getPrice();
            }
        }
        //Отсортировать получившийся список по возрастанию
        result.sort(Treasure::compareTo);
        //Поместить результат в новый экземпляр агрегатора
        return new Treasures(result.toArray(new Treasure[0]), 
                gems, materials, types, totalProductChance, totalGemChance);
    }
    
    /** Шапка таблицы */
    static final String[] HEAD = {"Наименование", "Цена"};
    
    /**
     * Отформатировать массив в виде таблицы
     * @return 
     */
    @Override
    public String toString() {
        if (treasures.length < 1) return "Пусто.";
        //Создать пустую таблицу с заданной шапкой
        Table result = new Table(HEAD);
        //Выровнять столбец цены вправо
        result.getCol(1).setAlign(Table.Align.RIGHT);
        for (var treasure: treasures) {
            //Добавить строку в таблицу
            result.addRow(new String[]{
                //Наименование с большой буквы
                upperFirst(treasure.getName()),
                //Цена
                String.format("%,d", treasure.getPrice())
            });
        }
        return result.toString();
    }
    
    /**
     * Сделать первую букву заглавной
     * @param string Исходная строка
     * @return Строка с большой буквы
     */
    static String upperFirst(String string) {
        if (string.isBlank()) return string;
        char[] result = string.strip().toCharArray();
        result[0] = Character.toUpperCase(result[0]);
        return new String(result);
    }
    
    /** Разделитель полей в файле */
    final static Pattern F_DELIMITER = Pattern.compile("\\s*;\\s*");
    /** Разделитель в массиве */
    final static String ARR_DELIMITER = "\\s*,\\s*";
    
    /**
     * Загрузить справочник видов драгоценных камней из файла
     * @param fileName Имя файла
     * @return Массив видов камней
     * @throws FileNotFoundException 
     */
    private Gem[] loadGems(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName));
        //До создания массива результат накапливается в списке
        LinkedList<Gem> result = new LinkedList<>();
        //Число видов камней * 100
        totalGemChance = 0;
        while (in.hasNextLine()) {
            try {
                //Прочитать очередную строку
                result.add(new Gem(in.nextLine().strip()));
                //Есть ли вероятность создания этого камня?
                if (result.getLast().chance > 0) totalGemChance += 100;
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        return result.toArray(new Gem[0]);
    }
    
    /**
     * Загрузить справочник материалов из файла
     * @param fileName Имя файла
     * @return Массив материалов
     * @throws FileNotFoundException 
     */
    private Material[] loadMaterlials(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName));
        //До создания массива результат накапливается в списке
        LinkedList<Material> result = new LinkedList<>();
        while (in.hasNextLine()) {
            try {
                //Прочитать очередную строку
                result.add(new Material(in.nextLine().strip()));
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        return result.toArray(new Material[0]);
    }

    /**
     * Загрузить справочник типов предметов из файла
     * @param fileName Имя файла
     * @return Массив типов предметов
     * @throws FileNotFoundException 
     */
    private Type[] loadTypes(String fileName) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fileName));
        //До создания массива результат накапливается в списке
        LinkedList<Type> result = new LinkedList<>();
        //Число возможных типов предметов из каждого из материалов * 100
        totalProductChance = 0;
        while (in.hasNextLine()) {
            try {
                //Прочитать очередную строку
                result.add(new Type(in.nextLine().strip()));
                totalProductChance += result.getLast().totalMaterialChance;
            } catch (Exception e) {
                //Строки с ошибками пропускаем
            }
        }
        result.add(new Type("рука", 5, new int[]{}, 1));
        result.add(new Type("унитаз", 30, new int[]{}, 0));
        return result.toArray(new Type[0]);
    }
}
