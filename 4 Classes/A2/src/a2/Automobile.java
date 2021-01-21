package a2;

/**
 *   Объект класса Автомобиль: Колесо, Двигатель. Методы: ехать, заправляться,
 * менять колесо, вывести на консоль марку автомобиля.
 * @author aabyodj
 */
class Automobile implements Mechanism {
    /** Марка автомобиля */
    private final String model;
    /** Двигатель */
    private final Engine engine = new Engine(this);
    /** Количество топлива в баке */
    private double fuel = 0;
    /** Колёса */
    private final Wheel[] wheels = new Wheel[]{
        new Wheel("Переднее левое"),
        new Wheel("Переднее правое"),
        new Wheel("Заднее левое"),
        new Wheel("Заднее правое")
    };
    
    /** Признак того, что автомобиль едет */
    private boolean driving = false;

    /**
     *  Создать объект Автомобиль заданной марки
     * @param model 
     */
    Automobile(String model) {
        this.model = model;
    }

    /**
     * Получить марку автомобиля 
     * @return 
     */
    public String getModel() {
        return model;
    }

    /**
     * Получить остаток топлива в баке 
     * @return 
     */
    public double getFuel() {
        return fuel;
    }

    /**
     * Определить, едет ли автомобиль в настоящий момент
     * @return 
     */
    public boolean isDriving() {
        return driving;
    }
    
    /**
     * Получить список наименований установленных колёс: переднее левое, 
     * переднее правое и т.д.
     * @return 
     */
    public String[] getWheelPositions() {
        String[] result = new String[wheels.length];
        for (int i = 0; i < wheels.length; i++) {
            result[i] = wheels[i].position;
        }
        return result;
    }
    
    /**
     * Поменять заданное колесо на новое
     * @param i Индекс колеса в массиве
     */
    public void changeWheel(int i) {
        wheels[i] = new Wheel(wheels[i].position);
    }
    
    /**
     * Заправиться заданным количеством топлива
     * @param fuel 
     */
    public void fill(double fuel) {
        if (0 == fuel) return;
        if (engine.running) 
            throw new IllegalStateException("Нельзя заправляться при работающем двигателе.");
        if (fuel < 0) 
            throw new UnsupportedOperationException("Слив топлива ещё не реализован.");
        this.fuel += fuel;
    }

    /** Ехать */
    @Override
    public void start() {
        if (driving) { //Уже едем
            return;
        }
        //Пробуем завестись
        try {
            engine.start();
        } catch (Exception e) { //Не удалось
            stop();
            throw e;
        }
        /*
        Теперь пробуем крутить колёса. Они крутятся независимо друг от друга.
        */
        //Первый отказ любого из колёс
        IllegalStateException fault = null;
        for (var wheel : wheels) {
            try {
                wheel.start();
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                if (null == fault) fault = e;
            }
        }
        if (fault != null) { //Какое-то из колёс отказалось крутиться
            stop();
            throw fault;
        }
        //Признак того, что машина едет
        driving = true;  
        System.out.println(model + ": \"Ура! Я еду!\"");
    }

    /** Остановиться */
    @Override
    public void stop() {
        //Заглушить двигатель на ходу
        engine.stop();
        //Потом остановить колёса
        for (var wheel: wheels) {
            wheel.stop();
        }
        //Признак, что машина не едет
        driving = false;
        System.out.println(model + ": \"Постою немножко.\"");
    }
    
    /** Попрощаться */
    public void sayGoodbye() {
        //Если едем - остановиться
        if (driving) stop();
        System.out.println(model + ": \"До свидания, хозяин!\"");
    }

    /** Класс Двигатель */
    private static class Engine implements Mechanism {
        /** Автомобиль, которому принадлежит двигатель */
        private final Automobile automobile;
        /** Признак того что двигатель заведён */        
        private boolean running = false;
                
        /** Самоназвание при выводе статуса */
        private static final String ME = "Двигатель: \"";
        /** Сообщение об отсутствии топлива */
        private static final String NO_FUEL_MSG = ME
                + "Работа без топлива ещё не реализована.\"";
        
        /**
         * Создать объект Двигатель, прринадлежащий заданному автомобилю
         * @param automobile 
         */
        Engine(Automobile automobile) {
            this.automobile = automobile;
        }

        /** Завестись */
        @Override
        public void start() {
            if (running) return; //Уже завелись
            if (0 == automobile.fuel) //Нет топлива
                throw new UnsupportedOperationException(NO_FUEL_MSG);
            //Заводимся...
            System.out.print(ME + "ДРРРЫМ-");
            //При этом тратится немного топлива
            automobile.fuel = Math.max(automobile.fuel - 1, 0);
            if (0 == automobile.fuel) { //Топлива больше нет
                System.out.println("кхе-кхе!\"");
                throw new UnsupportedOperationException(NO_FUEL_MSG);
            }
            //Признак того, что завелись
            running = true;
            System.out.println("дым-дым-дым-дым...\"");
        }

        /** Заглушить */
        @Override
        public void stop() {
            if (!running) return; //Не заведён          
            System.out.println(ME + "ДЫР!\"");
            //Признак того, что не заведён
            running = false;
        }
    }

    /** Класс Колесо */
    private static class Wheel implements Mechanism {
        /** Позиция колеса в автомобиле: переднее левое, переднее правое и т.д. */
        private final String position;
        /** Самоназвание при выводе статуса */
        private final String me;
        /** Сообщение о лопнувшем колесе */
        private final String burstExcMsg;
        
        /** Признак того, что колесо лопнуло */
        private boolean burst = false;
        /** Признак того, что колесо сейчас крутится */
        private boolean spinning = false;
        /** 
         * Процент изношенности. Вероятность того, что при следующем запуске или
         * остановке колесо лопнет.
         * При каждом запуске и остановке увеличивается на 1.
         */
        private int wear = 0;

        /**
         * Создать объект колесо на заданной позиции в машине (переднее правое,
         * переднее левое и т.д.)
         * @param position 
         */
        private Wheel(String position) {
            this.position = position;
            //Самоназвание при выводе статуса
            me = position + " колесо: \"";
            //Сообщение о том, что колесо лопнуло
            burstExcMsg = position + " колесо лопнуло.";
        }

        /** Раскрутить */
        @Override
        public void start() {
            if (spinning) return; //Уже крутится
            if (burst) //Если лопнуло
                throw new IllegalStateException(burstExcMsg);
            //Самоназвание
            System.out.print(me);
            //Износ при запуске
            wearOut();
            if (burst) //Если лопнуло
                throw new IllegalStateException(burstExcMsg);  
            //Признак того, что крутится
            spinning = true;                      
            System.out.println("Скрип-скрип-скрип...\"");
        }

        /** Остановить */
        @Override
        public void stop() {
            if (!spinning) return; //Не крутится
            //Самоназвание
            System.out.print(me);
            //Износ при остановке
            wearOut();
            if (!burst) //Если ещё не лопнуло
                System.out.println("СКРИП!\"");
            //Признак того, что не крутится
            spinning = false;
        }

        /** Износ при запуске и остановке */
        private void wearOut() {
            if (burst) return; //Уже лопнуло
            //Увеличить степень износа на 1
            wear = Math.min(wear + 1, 100);
            //Попробовать лопнуть
            if (wear > Math.random() * 100) { 
                //Лопнуло
                burst = true;
                System.out.println("БА-БАХ!!!\"");
            }
        }
    }    
}
