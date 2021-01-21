package a3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Территориальная единица: район, область
 * @author aabyodj
 */
class Territory extends Unit {
    /** Столица или административный центр */
    final Locality capital;
    /** 
     * Количество населённых пунктов, непосредственно подчинённых данной 
     * территориальной единице
     */
    final int localities;
    
    /**
     * Создать территорию с заданными названием, административным центром и 
     * площадью, без подчинённых территорий (столица автоматически включается в 
     * список подчинённых единиц)
     * @param name Название
     * @param capital Административный центр
     * @param area Площадь
     */
    Territory(String name, Locality capital, double area) {
        super(name, area, new Unit[]{capital});
        this.capital = capital;
        this.localities = 1;
    }
    
    /**
     * Создать территорию с заданными названием, административным центром и 
     * непосредственно подчинёнными единицами (территориями и/или населёнными 
     * пунктами)
     * @param name Название
     * @param capital Административный центр
     * @param subUnits Подчинённые единицы
     */
    Territory(String name, Locality capital, Unit[] subUnits) {
        super(name, subUnits);
        this.capital = capital;
        //Подсчёт количества населённых пунктов
        int c = 0;        
        for (var unit: subUnits) {
            if (unit instanceof Locality) c++;
        }
        this.localities = c;
    }
    
    /**
     * Создать копию
     * @param territory 
     */
    Territory(Territory territory) {
        super(territory);
        this.capital = territory.capital;
        this.localities = territory.localities;
    }
    
    /**
     * Получить административный центр (столицу)
     * @return 
     */
    public Locality getCapital() {
        return capital;
    } 
    
    /**
     * Получить массив непосредственно подчинённых населённых пунктов
     * @return 
     */
    Locality[] getLocalities() {
        if ((null == subUnits) || 0 == localities) return new Locality[0];
        Locality[] result = new Locality[localities];
        int i = 0;
        for (var unit: subUnits) {
            if (unit instanceof Locality)
                result[i++] = (Locality) unit;
        }
        return result;
    }
    
    /**
     * Получить массив всех населённых пунктов на данной территории
     * @return 
     */
    Locality[] getAllLocalities() {        
        if (null == subUnits) return new Locality[0];
        LinkedList<Locality[]> list = new LinkedList<>();
        list.add(getLocalities());
        int c = list.peekLast().length;
        for (var unit: subUnits) {
            if (unit instanceof Territory) {
                list.add(((Territory) unit).getAllLocalities());
                c += list.peekLast().length;
            }
        }
        Locality[] result = new Locality[c];
        c = 0;
        for (var array: list) {
            for (var locality: array) {
                result[c++] = locality;
            }
        }
        return result;
    }
    
    /**
     * Получить массив непосредственно подчинённых территорий
     * @return 
     */
    Territory[] getSubTerritories() {
        if (null == subUnits) return new Territory[0];
        Territory[] result = new Territory[subUnits.length - localities];
        int i = 0;
        for (var unit: subUnits) {
            if (unit instanceof Territory)
                result[i++] = (Territory) unit;
        }
        return result;
    }
    
    /**
     * Загрузить территориальную единицу из файла
     * @param fileName Имя файла
     * @return
     * @throws FileNotFoundException 
     */
    static Territory load(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner in = new Scanner(file);
        //Первая непустая строка с удалённым комментарием (если он был)
        Scanner line = nextLine(in);
        //Название. Наличие обязательно.
        String myName = line.next(); //throws NullPointerException
        //Административный центр. Наличие обязательно.
        Locality myCapital = new Locality(line.next(), 0); //throws NoSuchElementException
        if (line.hasNextDouble()) { //Указана площадь
            //Дальше файл не читаем
            in.close();
            return new Territory(myName, myCapital, line.nextDouble());
        }
        //Список подчинённых объектов
        LinkedList<Unit> subUnits = new LinkedList<>();
        while (in.hasNextLine()) { //Лучше while (true) но так нагляднее
            //Следующая непустая строка с удалённым комментарием
            line = nextLine(in);
            if (null == line) break; //Конец файла
            //Название объекта или имя файла
            String name = line.next();
            if (line.hasNextDouble()) { //Если сразу указана площадь
                //Это населённый пункт
                Locality locality = new Locality(name, line.nextDouble());
                if (locality.name.equalsIgnoreCase(myCapital.name))
                    //Удаление дублирования с административным центром
                    myCapital = locality;
                //Добавить в список
                subUnits.add(locality);
                continue;
            }
            //Это не населённый пункт
            if (line.hasNext()) { //Если указана столица
                Locality capital = new Locality(line.next(), 0);
                /* 
                TODO: столица территории ДОЛЖНА присутствовать в списке городов 
                этой или любой вышестоящей территории.
                Найти и удалить дубликаты.
                */
                //Обязательно должна быть указана площадь
                if (line.hasNextDouble()) {
                    subUnits.add(new Territory(name, capital, line.nextDouble()));
                    continue;
                }                    
            }
            //Площадь не указана - пытаемся читать файл.
            subUnits.add(Territory.load(name));
        }
        in.close();
        //Успех
        return new Territory(myName, myCapital, subUnits.toArray(new Unit[0]));
    }
    
    /** Часть строки до комментария */
    static final Pattern NOT_A_COMMENT = Pattern.compile("[^#]*");
    /** Остаток строки и все последующие пустые строки */
    static final Pattern EOL = Pattern.compile(".*\\R*");
    /** Разделитель полей в файле */
    static final Pattern DELIMITER = Pattern.compile("\\s*;\\s*");
    
    /**
     * Создать новый экземпляр сканера, содержащий следующую непустую строку с
     * удалённым комментарием (если он был). Если ничего не найдено, то null.
     * @param in Исходный сканер
     * @return Сканер с непустой строкой или null
     */
    static Scanner nextLine(Scanner in) {
        String line;
        do {
            //Часть строки до комментария
            line = in.findInLine(NOT_A_COMMENT);
            //Пропустить остаток строки и все последующие пустые строки
            in.skip(EOL);
            //Ещё не конец?
            if (null == line) return null;           
        } while (line.isBlank()); //Нужны только непустые строки
        //Удаляем пробелы в начале и в конце и задаём разделитель
        return new Scanner(line.strip()).useDelimiter(DELIMITER);
    }
}
