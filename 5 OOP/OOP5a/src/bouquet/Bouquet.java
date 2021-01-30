package bouquet;

import bouquet.Component.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * Цветочная композиция
 * @author aabyodj
 */
public class Bouquet {
    /** Переход на новую строку */
    public static final String BR = System.lineSeparator();
    
    /** Цветы в составе композиции */
    LinkedList<Flower> flowers = new LinkedList<>();
    /** Упаковка */
    Wrapper wrapper = null;
    
    /**
     * Добавить цветок в композицию
     * @param flower Цветок
     * @return Текущая композиция
     */
    public Bouquet add(Flower flower) {
        flowers.add(flower);
        return this;
    }
    
    /**
     * Создать цветок заданного класса и цвета и добавить его в композицию
     * @param flower Класс цветка
     * @param color Индекс цвета цветка в массиве возможных для данного класса
     * @return Текущая композиция
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public Bouquet add(Class flower, int color) 
            throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException 
    {
        return add((Flower) createComponent(flower, color));
    }
    
    /**
     * Задать упаковку
     * @param wrapper Упаковка
     * @return Текущая композиция
     */
    public Bouquet setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
        return this;
    }
    
    /**
     * Создать упаковку заданного класса и цвета и использовать её в композиции
     * @param wrapper Класс упаковки
     * @param color Индекс цвета упаковки в массиве возможных для данного класса
     * @return Текущая композиция
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public Bouquet setWrapper(Class wrapper, int color) 
            throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException 
    {
        return setWrapper((Wrapper) createComponent(wrapper, color));
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Цветы:");
        //Есть ли цветы в композиции?
        if (flowers.size() > 0) {
            for (var flower: flowers) {
                result.append(BR).append(flower);
            }            
        } else {
            //Композиция пуста
            result.append(" нет.");
        }
        result.append(BR);
        if (wrapper != null) {
            result.append("Упаковка: ").append(wrapper);
        } else {
            result.append("Без упаковки.");
        }
        return result.toString();
    }
    
    /**
     * Создать экземпляр компонента композиции (цветок или упаковку)
     * @param component Класс компонента
     * @param color Цвет компонента
     * @return
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public static Component createComponent(Class component, int color) 
            throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException 
    {
        //Будем искать конструктор, принимающий один параметр типа int
        Class[] parameters = new Class[]{int.class};
        return (Component) component.getConstructor(parameters).newInstance(color);
    }
    
    /**
     * Получить массив возможных цветов для заданного класса компонента 
     * композиции (цветка или упаковки)
     * @param component Класс компонента
     * @return Массив возможных цветов
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public static Color[] colorsInStock(Class component) 
            throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException 
    {
        //На самом деле клонировать необязательно, но вдруг реализация getAvailableColors() изменится?
        return createComponent(component, 0).getAvailableColors().clone();
    }
    
    /**
     * Получить массив доступных классов цветков
     * @return Массив классов цветков
     */
    public static Class[] flowersInStock() {
        Class[] nest = Flower.class.getNestMembers();        
        LinkedList<Class> result = new LinkedList<>();
        for (Class c: nest) {
            if (!c.equals(Flower.class)) //Необходимо исключить абстрактный класс Flower
                result.add(c);
        }
        return result.toArray(new Class[0]);
    }
    
    /**
     * Получить массив доступных классов упаковки
     * @return Массив классов упаковки
     */
    public static Class[] wrappersInStock() {
        Class[] nest = Wrapper.class.getNestMembers();        
        LinkedList<Class> result = new LinkedList<>();
        for (Class c: nest) {
            if (!c.equals(Wrapper.class)) //Необходимо исключить абстрактный класс Wrapper
                result.add(c);
        }
        return result.toArray(new Class[0]);
    }
       
    /**
     * Преобразовать массив классов компонентов композиции (цветков и упаковок)
     * в массив названий этих компонентов
     * @param classes Массив классов компонентов композиции
     * @return Массив названий компонентов композиции
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public static String[] toStrings(Class[] classes) 
            throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException 
    {
        String[] result = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            /* Да, это неэффективно. Здесь нужны статические абстрактрые классы,
            но Java этого не позволяет. По-хорошему эти компоненты вообще не 
            должны быть представлены в виде иерархии классов.  */
            result[i] = createComponent(classes[i], 0).getName();
        }
        return result;
    }
    
    /**
     * Преобразовать массив объектов в массив строк посредством вызова для 
     * каждого из этих объектов его метода toString()
     * @param objects Массив объектов
     * @return Массив строк
     */
    public static String[] toStrings(Object[] objects) {
        String[] result = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            result[i] = objects[i].toString();
        }
        return result;
    }
}
