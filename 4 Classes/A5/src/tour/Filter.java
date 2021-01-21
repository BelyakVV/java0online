package tour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static tour.Tours.formatDaysGen;
import static tour.Tours.formatPrice;

/**
 * Фильтр путёвок по заданным критериям
 * @author aabyodj
 */
public class Filter extends Tour {
    /** Агрегатор путёвок и справочников стран и пунктов назначения */
    Tours tours;
    /** Страна назначения */
    Country country;
    /** Максимальная длительность поездки */
    int maxLength;
    /** Максимальная цена */
    int maxPrice;
    
    /**
     * Создать пустой фильтр
     * @param tours Агрегатор путёвок и справочников стран и пунктов назначения
     */
    Filter(Tours tours) {
        this.tours = tours;
    }
    
    /**
     * Получить список названий стран
     * @return 
     */
    public LinkedList<String> getCountries() {
        LinkedList<String> result = new LinkedList<>();
        for (Country c: tours.countries) {
            result.add(c.toString());
        }
        return result;
    }
    
    /**
     * Задать для фильтрации страну по её индексу в массиве справочника. 
     * В случае выхода за границу массива - обнулить поле.
     * @param i
     */
    public void setCountry(int i) {
        try {
            country = tours.countries[i];
        } catch (Exception e) {
            country = null;
        }
        //Не производить поиск по пункту назначения
        destination = null;
    }
    
    /**
     * Получить список названий пунктов назначения
     * @return 
     */
    public LinkedList<String> getDestinations() {
        LinkedList<String> result = new LinkedList<>();
        for (var d: tours.destinations) {
            result.add(d.toString());
        }
        return result;
    }

    /**
     * Задать для фильтрации пункт назначения по его индексу в массиве 
     * справочника. В случае выхода за границу массива - обнулить поле.
     * @param i 
     */
    public void setDestination(int i) {
        try {
            destination = tours.destinations[i];
        } catch (Exception e) {
            destination = null;
        }
        //Не производить поиск по стране
        country = null;
    }

    /**
     * Получить список наименований типов путёвок
     * @return 
     */
    public LinkedList<String> getTypes() {
        LinkedList<String> result = new LinkedList<>();
        for (var t: Type.values()) {
            result.add(t.value);
        }
        return result;
    }
    
    /**
     * Задать для фильтрации тип путёвки по его порядковому номеру в 
     * перечислении. В случае выхода за границу массива - обнулить поле.
     * @param i 
     */
    public void setType(int i) {
        try {
            type = Type.values()[i];
        } catch (Exception e) {
            type = null;
        }
    }

    /**
     * Получить список наименований видов транспорта
     * @return 
     */
    public LinkedList<String> getTrTypes() {
        LinkedList<String> result = new LinkedList<>();
        for (var t: Transport.values()) {
            result.add(t.value);
        }
        return result;
    }

    /**
     * Задать для фильтрации вид транспорта по его порядковому номеру в 
     * перечислении. В случае выхода за границу массива - обнулить поле.
     * @param i 
     */
    public void setTransport(int i) {
        try {
            transport = Transport.values()[i];
        } catch (Exception e) {
            transport = null;
        }
    }

    /**
     * Получить список наименований планов питания
     * @return 
     */
    public LinkedList<String> getMeals() {
        LinkedList<String> result = new LinkedList<>();
        for (var m: Meal.values()) {
            result.add(m.value);
        }
        return result;
    }

    /**
     * Задать для фильтрации план питания по его порядковому номеру в 
     * перечислении. В случае выхода за границу массива - обнулить поле.
     * @param i 
     */
    public void setMeal(int i) {
        try {
            meal = Meal.values()[i];
        } catch (Exception e) {
            meal = null;
        }
    }
    
    /**
     * Задать для фильтации минимальную и максимальную длительность поездки в 
     * днях. Значения меньше 1 при поиске игнорируются. Если значащая 
     * максимальная длительность меньше значащей минимальной, то они меняются 
     * местами.
     * @param minLength Минимальная желаема длительность поездки в днях
     * @param maxLength Максимальная желаемая длительность поездки в днях
     */
    public void setLengthBounds(int minLength, int maxLength) {
        if (maxLength > minLength || minLength < 1 || maxLength < 1) {
            this.length = minLength;
            this.maxLength = maxLength;
        } else {
            this.length = maxLength;
            this.maxLength = minLength;
        }
    }
    
    /**
     * Задать для фильтрации минимальную и максимальную стоимость путёвки в
     * копейках. Значения меньше 1 при поиске игнорируются. Если значащая 
     * максимальная стоимость меньше значащей минимальной, то они меняются 
     * местами.
     * @param minPrice Минимальная желаемая стоимость поездки в копейках
     * @param maxPrice Максимальная желаемая стоимость поездки в копейках
     */
    public void setPriceBounds(int minPrice, int maxPrice) {
        if (maxPrice > minPrice || minPrice < 1 || maxPrice < 1) {
            this.price = minPrice;
            this.maxPrice = maxPrice;
        } else {
            this.price = maxPrice;
            this.maxPrice = minPrice;
        }
    }
    
    /**
     * Определить, заданы ли какие-либо критерии для поиска.
     * @return true, если критериев для поиска нет
     */
    public boolean isEmpty() {
        return type == null && country == null && destination == null 
                && transport == null && meal == null && length < 1 
                && maxLength < 1 && price < 1 && maxPrice < 1;
    }

    @Override
    public String toString() {
        //Заданы ли какие-либо критерии для поиска?
        if (isEmpty()) {
            return "Без фильтра";
        }
        StringBuilder result = new StringBuilder("Фильтр: ");
        //Задан ли пункт назначения?
        if (destination != null) {
            result.append(destination).append(", ");
            //Если нет - задана ли страна?
        } else if (country != null) {
            result.append(country).append(", ");
        }
        //Задан ли тип путёвки?
        if (type != null) {
            result.append(type.value).append(", ");
        }
        //Задан ли вид транспорта?
        if (transport != null) {
            result.append(transport.value).append(", ");
        }
        //Задан ли план питания?
        if (meal != null) {
            result.append(meal.value).append(", ");
        }
        //Заданы ли рамки длительности поездки?
        if (length > 0 || maxLength > 0) {
            result.append("длительность");
            //Задана ли нижняя граница?
            if (length > 0) {
                result.append(" от ");
                if (maxLength > 0) {
                    //Заданы и нижняя, и верхняя граница
                    result.append(length).append(" до ");
                    //Отформатировать длительность в родительном падеже
                    result.append(formatDaysGen(maxLength)).append(", ");
                } else {
                    //Задана только нижняя граница
                    result.append(formatDaysGen(length)).append(", ");
                }
            } else {
                //Задана только верхняя граница
                result.append(" до ").append(formatDaysGen(maxLength)).append(", ");
            }
        }
        //Заданы ли рамки стоимости?
        if (price > 0 || maxPrice > 0) {
            result.append("цена");
            //Задана ли нижняя граница?
            if (price > 0) {
                //Отформатировать стоимость в рублях и копейках
                result.append(" от ").append(formatPrice(price));
            }
            //Задана ли верхняя граница?
            if (maxPrice > 0) {
                result.append(" до ").append(formatPrice(maxPrice));
            }
            //Больше добавить нечего
            return result.toString();
        }
        //Удалить последнюю запятую и пробел
        return result.substring(0, result.length() - 2);
    }

    /**
     * Определить, подходит ли заданная путёвка под текущие критерии
     * @param tour Заданная путёвка
     * @return 
     */
    boolean matches(Tour tour) {
        //Тип путёвки
        if (this.type != null) {
            if (tour.type != this.type) {
                return false;
            }
        }
        //Пункт назначения
        if (this.destination != null) {
            if (tour.destination.id != this.destination.id) {
                return false;
            }
            //Страна, если не задан пункт назначения
        } else if (country != null) {
            if (tour.destination.country.id != country.id) {
                return false;
            }
        }
        //Вид транспорта
        if (this.transport != null) {
            if (tour.transport != this.transport) {
                return false;
            }
        }
        //План питания
        if (this.meal != null) {
            if (tour.meal != this.meal) {
                return false;
            }
        }
        //Минимальная длительность в днях
        if (this.length > 0) {
            if (tour.length < this.length) {
                return false;
            }
        }
        //Максимальная длительность
        if (maxLength > 0) {
            if (tour.length > maxLength) {
                return false;
            }
        }
        //Минимальная цена в копейках
        if (this.price > 0) {
            if (tour.price < this.price) {
                return false;
            }
        }
        //Максимальная цена
        if (maxPrice > 0) {
            if (tour.price > maxPrice) {
                return false;
            }
        }
        //Все проверки пройдены
        return true;
    }
    
    /**
     * Получить список путёвок, соответствующих текущим критериям
     * @return 
     */
    List<Tour> select() {
        //Полный список путёвок из агрегатора
        List<Tour> result = Arrays.asList(tours.tours);
        //Страна
        result = selectCountry(result);
        //Пункт назначения
        result = selectDestination(result);
        //Тип путёвки
        result = selectType(result);
        //Вид транспорта
        result = selectTransport(result);
        //План питания
        result = selectMeal(result);
        //Длительность
        result = selectLength(result);
        //Цена
        result = selectPrice(result);
        return result;
    }

    /**
     * Выбрать из списка только путёвки с заданной страной
     * @param list Список путёвок
     * @return 
     */
    private List<Tour> selectCountry(List<Tour> list) {
        //Должна быть задана страна и при этом не задан пункт назначения
        if (country == null || destination != null) return list;
        LinkedList<Tour> result = new LinkedList<>();
        for (var tour: list) {
            if (tour.destination.country.id == country.id)
                result.add(tour);
        }
        return result;
    }

    /**
     * Выбрать из списка путёвки с заданным пунктом назначения
     * @param list Список путёвок
     * @return 
     */
    private List<Tour> selectDestination(List<Tour> list) {
        //Должен быть задан пункт назначения
        if (destination == null) return list;
        LinkedList<Tour> result = new LinkedList<>();
        for (var tour: list) {
            if (tour.destination.id == destination.id)
                result.add(tour);
        }
        return result;
    }

    /**
     * Выбрать из списка путёвки заданного типа
     * @param list Список путёвок
     * @return 
     */
    private List<Tour> selectType(List<Tour> list) {
        //Тип в фильтре должен быть задан
        if (type == null) return list;
        LinkedList<Tour> result = new LinkedList<>();
        for (var tour: list) {
            if (tour.type == type)
                result.add(tour);
        }
        return result;
    }

    /**
     * Выбрать из списка путёвки с заданным видом транспорта
     * @param list Список путёвок
     * @return 
     */
    private List<Tour> selectTransport(List<Tour> list) {
        //Вид транспорта в фильтре должен быть задан
        if (transport == null) return list;
        LinkedList<Tour> result = new LinkedList<>();
        for (var tour: list) {
            if (tour.transport == transport)
                result.add(tour);
        }
        return result;
    }

    /**
     * Выбрать из списка путёвки с заданным планом питания
     * @param list Список путёвок
     * @return 
     */
    private List<Tour> selectMeal(List<Tour> list) {
        //План питания в фильтре должен быть задан
        if (meal == null) return list;
        LinkedList<Tour> result = new LinkedList<>();
        for (var tour: list) {
            if (tour.meal == meal)
                result.add(tour);
        }
        return result;
    }

    /**
     * Выбрать из списка путёвки с заданной длительностью
     * @param list Список путёвок
     * @return 
     */
    private List<Tour> selectLength(List<Tour> list) {
        //В фильтре должна быть задана как минимум одна граница
        if (length < 1 && maxLength < 1) return list;
        LinkedList<Tour> result = new LinkedList<>();
        if (maxLength < 1) { //Задана только нижняя граница
            for (var tour: list) {
                if (tour.length >= length)
                    result.add(tour);
            }
            return result;
        }
        if (length < 1) { //Задана только верхняя граница
            for (var tour: list) {
                if (tour.length <= maxLength)
                    result.add(tour);
            }
            return result;
        }
        //Заданы обе границы
        for (var tour: list) {
            if (tour.length >= length && tour.length <= maxLength)
                result.add(tour);
        }
        return result;
    }

    /**
     * Выбрать из списка путёвки с заданной стоимостью
     * @param list Список путёвок
     * @return 
     */
    private List<Tour> selectPrice(List<Tour> list) {
        //В фильтре должна быть задана как минимум одна граница
        if (price < 1 && maxPrice < 1) return list;
        LinkedList<Tour> result = new LinkedList<>();
        if (maxPrice < 1) { //Задана только нижняя граница
            for (var tour: list) {
                if (tour.price >= price)
                    result.add(tour);
            }
            return result;
        }
        if (price < 1) { //Задана только верхняя граница
            for (var tour: list) {
                if (tour.price <= maxPrice)
                    result.add(tour);
            }
            return result;
        }
        //Заданы и нижняя, и верхняя границы
        for (var tour: list) {
            if (tour.price >= price && tour.price <= maxPrice)
                result.add(tour);
        }
        return result;
    }
}
