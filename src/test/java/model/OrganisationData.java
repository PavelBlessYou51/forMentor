package model;
/**
 * Класс для представления юридического лица
 */
public class OrganisationData extends EntityDataBase {
    public String position;
    public String organisationName;
    public String uniqueID;

    /**
     * Конструктор класса
     */
    public OrganisationData () {
        this.position = fakerRU.company().profession();
        this.organisationName = fakerRU.company().name();
        setUniqueID(countryCode);
    }

    /**
     * Метод устанавливает уникальный идентификатор для юридического лица
     */
    private void setUniqueID(String country) {
        switch (country) {
            case "AZ":
            case "RU":
                uniqueID = fakerRU.number().digits(10);
                break;
            case "KG":
                uniqueID = fakerRU.number().digits(14);
                break;
            case "AM":
                uniqueID = fakerRU.number().digits(8);
                break;
            case "BY":
            case "TJ":
                uniqueID = fakerRU.number().digits(9);
                break;
            case "KZ":
            case "TM":
                uniqueID = fakerRU.number().digits(12);
                break;
        }

    }




}
