package manager;

import model.OrganisationData;
import model.PatentAgent;
import model.PersonData;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

/**
 * Класс-помощник. Содержит методы, связанные с регистрацией
 */
public class RegistrationHelper extends HelperBase {

    /**
     * Конструктор класса. Явно вызывает конструктор родителя
     */
    public RegistrationHelper(ApplicationManager manager) {
        super(manager);
    }

    /**
     * Метод для перехода на страницу с формой регистрации
     */
    public void goToRegisterPage() {
        click(By.xpath("//a[contains(text(), 'Регистрация')]"), true);
    }

    /**
     * Метод заполняется форму регистрации для физического лица
     */
    public void fillRegistrationFormForPerson() throws InterruptedException {
        goToRegisterPage();
        PersonData person = new PersonData();
        optionPicker(By.xpath("//select[contains(@id, 'regRadioPanelId')]"), 0, false);
        optionPicker(By.xpath("//span[contains(@id, 'gridCountryMenu')]/div/select"), getRandomInt(8), true);
        type(By.xpath("//input[contains(@id, 'input-lastname')]"), person.surname, true);
        type(By.xpath("//input[contains(@id, 'input-firstname')]"), person.name, false);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[3]"), person.patronymic, true);
        type(By.cssSelector("input[id='form:email']"), person.email, true);
        type(By.id("form:emailToConfirm"), person.email, true);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[6]"), person.passport, false);
        type(By.id("form:appeal"), person.callTo, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[2]/input"), person.postCode, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[4]/input"), person.address, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[6]/input"), person.phoneNumber, false);
        click(By.id("form:btnSelfRegistrationUser"), false);

    }

    /**
     * Метод заполняется форму регистрации для юридического лица
     */
    public void fillRegistrationFormForOrganisation() throws InterruptedException {
        goToRegisterPage();
        int randomCountryNumber = getRandomInt(8);
        OrganisationData oraganisation = new OrganisationData(randomCountryNumber);
        optionPicker(By.xpath("//select[contains(@id, 'RadioPanelId')]"), 2, false);
        optionPicker(By.xpath("//span[contains(@id, 'CountryMenu')]//select"), randomCountryNumber, true);
        type(By.xpath("//input[contains(@id, 'input-lastname')]"), oraganisation.surname, false);
        type(By.xpath("//input[contains(@id, 'input-firstname')]"), oraganisation.name, false);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[3]"), oraganisation.patronymic, true);
        type(By.id("form:email"), oraganisation.email, true);
        type(By.id("form:emailToConfirm"), oraganisation.email, true);
        type(By.xpath("//div[@class='registration-content-input-post']/input"), oraganisation.position, false);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[6]"), oraganisation.organisationName, false);
        optionPicker(By.xpath("(//select[@class='reg-input-field-required'])[3] "), getRandomInt(3), true);
        type(By.id("form:appeal"), oraganisation.callTo, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[2]/input"), oraganisation.postCode, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[4]/input"), oraganisation.address, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[6]/input"), oraganisation.phoneNumber, false);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[7]"), oraganisation.uniqueID, false);
        click(By.id("form:btnSelfRegistrationUser"), false);
    }

    /**
     * Метод заполняется форму регистрации переданного типа поверенного
     */
    public void fillRegistrationFormForPatentAgent(String agentType) {
        goToRegisterPage();
        String regNumber;
        String email;
        PatentAgent patentAgent = new PatentAgent(agentType);
        optionPicker(By.xpath("//select[contains(@id, 'RadioPanel')]"), 1, false);
        if ("industrial".equals(agentType)) {
            click(By.id("form:certificationTypeRadio:1"), false);
            regNumber = patentAgent.agentRegNumberIndustrialdesign[getRandomInt(3)];
        } else {
            regNumber = patentAgent.agentRegNumberInvention[getRandomInt(3)];
        }
        click(By.cssSelector("input[class='reg-input-field-required']"), false);
        type(By.cssSelector("input[class='reg-input-field-required']"), regNumber, true);
        click(By.id("form:btnCheckUser"), false);
        email = getElementAttrValue(By.id("form:email"), "value");
        type(By.id("form:emailToConfirm"), email, false);
        click(By.id("form:btnSelfRegistrationUser"), false);
    }

    /**
     * Метод получающий сообщение об отправке запроса на регистрацию
     */
    public String getRegistrationRequestMessageConfirm() throws InterruptedException {
        String massege = getTextFromElement(By.cssSelector("li span[class='error-message']")).split("\\.")[0];
        return massege;
    }
}
