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
    public OrganisationData (int countryNumber) {
        this.position = fakerRU.company().profession();
        this.organisationName = fakerRU.company().name();
        setUniqueID(countryNumber);
    }

    /**
     * Метод устанавливает уникальный идентификатор для юридического лица
     */
    private void setUniqueID(int countryNumber) {
        switch (countryNumber) {
            case 0:
            case 6:
                uniqueID = fakerRU.number().digits(10);
                break;
            case 1:
                uniqueID = fakerRU.number().digits(14);
                break;
            case 2:
                uniqueID = fakerRU.number().digits(8);
                break;
            case 3:
            case 5:
                uniqueID = fakerRU.number().digits(9);
                break;
            case 4:
            case 7:
                uniqueID = fakerRU.number().digits(12);
                break;
        }

    }




}
