package model;
/**
 * Класс для представления юридического лица
 */
public class OrganisationData extends EntityDataBase {
    public String position;
    public String organisationName;

    /**
     * Конструктор класса
     */
    public OrganisationData () {
        this.position = fakerRU.company().profession();
        this.organisationName = fakerRU.company().name();
    }
}
