package a3;

import java.io.FileNotFoundException;

/**
 *   Создать объект класса Государство, используя классы Область, Район, Город. 
 * Методы: вывести на консоль столицу, количество областей, площадь, областные 
 * центры.
 * @author aabyodj
 */
class State extends Territory {    
    /**
     * Преобразовать объект "Территориальная единица" в объект "Государство"
     * @param state 
     */
    State(Territory state) {
        super(state);
    }
    
    /**
     * Получить массив областей
     * @return 
     */
    Region[] getRegions() {
        Territory[] regions = getSubTerritories();
        Region[] result = new Region[regions.length];
        for (int i = 0; i < regions.length; i++) {
            result[i] = new Region(regions[i]);
        }
        return result;
    }

    /**
     * Загрузить из файла
     * @param fileName Имя файла
     * @return
     * @throws FileNotFoundException 
     */
    static State load(String fileName) throws FileNotFoundException {
         return new State(Territory.load(fileName));
    }
    
    /** Класс Область */
    class Region extends Territory {  
    /**
     * Преобразовать объект "Территориальная единица" в объект "Область"
     * @param state 
     */
        Region(Territory region) {
            super(region);
        }
    
    /**
     * Получить массив районов
     * @return 
     */
        District[] getDistricts() {
            Territory[] districts = getSubTerritories();
            District[] result = new District[districts.length];
            for (int i = 0; i < districts.length; i++) {
                result[i] = new District(districts[i]);
            }
            return result;
        }
    }

    /** Объект Район */
    static class District extends Territory {
    /**
     * Преобразовать объект "Территориальная единица" в объект "Район"
     * @param state 
     */
        District(Territory district) {
            super(district);
        }
    }
}
