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
        optionPicker(By.id("form:j_idt258:j_idt262"), getRandomInt(3), false);
        optionPicker(By.name("form:j_idt204"), getRandomInt(8), true);
        type(By.name("form:j_idt224"), person.surname, true);
        type(By.name("form:j_idt228"), person.name, false);
        type(By.name("form:j_idt232"), person.patronymic, false);
        type(By.cssSelector("input[id='form:email']"), person.email, true);
        type(By.id("form:emailToConfirm"), person.email, true);
        type(By.name("form:j_idt244"), person.passport, false);
        type(By.id("form:appeal"), person.callTo, false);
        type(By.name("form:j_idt272"), person.postCode, false);
        type(By.name("form:j_idt276"), person.address, false);
        type(By.name("form:j_idt280"), person.phoneNumber, false);
        click(By.id("form:btnSelfRegistrationUser"), false);

    }

    /**
     * Метод заполняется форму регистрации для юридического лица
     */
    public void fillRegistrationFormForOrganisation() throws InterruptedException {
        goToRegisterPage();
        OrganisationData oraganisation = new OrganisationData();
        optionPicker(By.xpath("//select[contains(@id, 'RadioPanelId')]"), 2, false);
        optionPicker(By.xpath("//span[contains(@id, 'CountryMenu')]//select"), getRandomInt(8), true);
        type(By.name("form:j_idt224"), oraganisation.surname, false);
        type(By.name("form:j_idt228"), oraganisation.name, false);
        type(By.name("form:j_idt232"), oraganisation.patronymic, true);
        type(By.id("form:email"), oraganisation.email, true);
        type(By.id("form:emailToConfirm"), oraganisation.email, true);
        type(By.name("form:j_idt249"), oraganisation.position, false);
        type(By.name("form:j_idt254"), oraganisation.organisationName, false);
        optionPicker(By.id("form:j_idt258:j_idt262"), getRandomInt(3), true);
        type(By.id("form:appeal"), oraganisation.callTo, false);
        type(By.name("form:j_idt272"), oraganisation.postCode, false);
        type(By.name("form:j_idt276"), oraganisation.address, false);
        type(By.name("form:j_idt280"), oraganisation.phoneNumber, false);
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
        click(By.name("form:j_idt218"), false);
        type(By.name("form:j_idt218"), regNumber, false);
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
