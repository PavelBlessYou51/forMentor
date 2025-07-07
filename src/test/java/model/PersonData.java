package model;

import java.util.concurrent.TimeUnit;

/**
 * Класс для представления физического лица
 */
public class PersonData extends EntityDataBase {

    public String passport;

    /**
     * Конструктор класса
     */
    public PersonData () {
        this.passport = fakerRU.number().digits(4) + " " + fakerRU.number().digits(6) + " " + fakerRU.commerce().department() + fakerRU.date().past(3650, TimeUnit.DAYS);
    }

}
