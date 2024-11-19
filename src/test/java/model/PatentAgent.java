package model;

/**
 * Класс для представления патентного поверенного
 */
public class PatentAgent {
    public String agentType;
    public String [] agentRegNumberInvention = {"589", "587", "586"};
    public String [] agentRegNumberIndustrialdesign = {"111", "112", "113"};

    /**
     * Конструктор класса
     */
    public PatentAgent(String agentType) {
        this.agentType = agentType;
    }
}
