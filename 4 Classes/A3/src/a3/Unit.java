package a3;

/**
 * Административная единица: государство, область, район, город и т.д.
 * @author aabyodj
 */
abstract class Unit {
    /** Название */
    final String name;
    /** Площадь */
    final double area;
    /** Подчинённые административные единицы */
    final Unit[] subUnits;

    /**
     * Создать административную единицу с заданным названием, площадью и 
     * составными частями.
     * @param name Название
     * @param area Площадь
     * @param subUnits Подчинённые административные единицы
     */
    Unit(String name, double area, Unit[] subUnits) {
        this.name = name;
        this.area = area;
        this.subUnits = subUnits;
    }
    
    /**
     * Создать неделимую административную единицу с заданным названием и нулеой
     * площадью.
     * @param name Название
     */
    Unit(String name) {
        this(name, 0, null);
    }
    
    /**
     * Создать неделимую административную единицу с заданными названием и 
     * площадью.
     * @param name Название
     * @param area Площадь
     */
    Unit(String name, double area) {
        this(name, area, null);
    }
    
    /**
     * Создать административную единицу с заданными названием и составными
     * частями и автоматически вычислить площадь.
     * @param name Название
     * @param subUnits Подчинённые административные единицы
     */
    Unit(String name, Unit[] subUnits) {
        this.name = name;
        this.subUnits = subUnits;
        //Вычисление площади
        double myArea = 0;
        for (var unit: subUnits) {
            myArea += unit.getArea();
        }
        this.area = myArea;
    }

    /**
     * Создать копию
     * @param unit 
     */
    Unit(Unit unit) {
        this.name = unit.name;
        this.area = unit.area;
        this.subUnits = unit.subUnits;
    }

    /**
     * Получить название
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Получить площадь
     * @return 
     */
    public double getArea() {
        return area;
    }    
}
