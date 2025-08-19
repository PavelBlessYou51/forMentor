package model;

import java.util.Arrays;

/**
 * Класс для представления патентного поверенного
 */
public class PatentAgent extends EntityDataBase{
    public String agentType;
    public String [] agentRegNumberInvention = {"589", "587", "586"};
    public String [] agentRegNumberIndustrialdesign = {"111", "112", "113"};
    public int regNumber;


    /**
     * Конструктор класса для ПП при регистрации
     */
    public PatentAgent(String agentType) {
        this.agentType = agentType;
    }

    /**
     * Конструктор класса для ПП при добавлении в заявку
     */
    public PatentAgent(String firstName, String patronymic, String lastName, String address, String email, String postCode, String phoneNumber, int regNumber) {
        super(firstName, patronymic, lastName, address, email, postCode, phoneNumber);
        this.regNumber = regNumber;
    }
}
