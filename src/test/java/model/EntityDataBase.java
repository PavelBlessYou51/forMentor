package model;

import com.github.javafaker.Faker;


import java.util.Locale;

/**
 * Базовый класс для генерации случайных данных, используемых для заполнения полей
 */
public class EntityDataBase {
    public Faker fakerRU = new Faker(new Locale("ru"));
    public Faker fakerEnUS = new Faker(new Locale("en-US"));

    public String surname;
    public String name;
    public String patronymic;
    public String email;
    public String callTo;
    public String postCode;
    public String address;
    public String phoneNumber;
    public String countryCode;

    /**
     * Конструктор класса
     */
    public EntityDataBase() {
        setCountryCode();
        setPostCode();
        this.surname = fakerRU.name().lastName();
        this.name = fakerRU.name().firstName();
        this.patronymic = "Фейкерович";
        this.email = fakerEnUS.internet().emailAddress();
        this.callTo = "Заявителю";
        this.address = "Россия " + fakerRU.address().cityName() + " " + fakerRU.address().streetAddress();
        this.phoneNumber = "+" + fakerRU.number().digits(10);
    }

    private void setCountryCode() {
        String[] listOfCountryCode = {"AM", "AZ", "BY", "KG", "KZ", "RU", "TJ", "TM"};
        countryCode = listOfCountryCode[fakerRU.number().numberBetween(0, 7)];
    }

    private void setPostCode() {
        if ("AM".equals(countryCode)) {
            postCode = fakerRU.number().digits(4);
        } else if ("AZ".equals(countryCode)) {
            postCode = "AZ" + fakerRU.number().digits(4);
        } else {
            postCode = fakerRU.number().digits(6);
        }
    }


}

