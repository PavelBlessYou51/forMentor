package manager;

import model.OrganisationData;
import model.PatentAgent;
import model.PersonData;
import org.openqa.selenium.By;

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
        click(By.xpath("//a[contains(text(), 'Регистрация')]"));
    }

    /**
     * Метод заполняется форму регистрации для физического лица
     */
    public void fillRegistrationFormForPerson() throws InterruptedException {
        goToRegisterPage();
        PersonData person = new PersonData();
        optionPicker(By.id("form:j_idt258:j_idt262"), getRandomInt(3));
        optionPicker(By.name("form:j_idt204"), getRandomInt(8));
        type(By.name("form:j_idt224"), person.surname);
        type(By.name("form:j_idt228"), person.name);
        type(By.name("form:j_idt232"), person.patronymic);
        type(By.id("form:email"), person.email);
        type(By.id("form:emailToConfirm"), person.email);
        type(By.name("form:j_idt244"), person.passport);
        type(By.id("form:appeal"), person.callTo);
        type(By.name("form:j_idt272"), person.postCode);
        type(By.name("form:j_idt276"), person.address);
        type(By.name("form:j_idt280"), person.phoneNumber);
        click(By.id("form:btnSelfRegistrationUser"));
    }

    /**
     * Метод заполняется форму регистрации для юридического лица
     */
    public void fillRegistrationFormForOrganisation() throws InterruptedException {
        goToRegisterPage();
        OrganisationData oraganisation = new OrganisationData();
        optionPicker(By.id("form:radioPanelId"), 2);
        optionPicker(By.id("form:j_idt258:j_idt262"), getRandomInt(3));
        optionPicker(By.name("form:j_idt204"), getRandomInt(8));
        type(By.name("form:j_idt224"), oraganisation.surname);
        type(By.name("form:j_idt228"), oraganisation.name);
        type(By.name("form:j_idt232"), oraganisation.patronymic);
        type(By.id("form:email"), oraganisation.email);
        type(By.id("form:emailToConfirm"), oraganisation.email);
        type(By.name("form:j_idt249"), oraganisation.position);
        type(By.name("form:j_idt254"), oraganisation.organisationName);
        type(By.id("form:appeal"), oraganisation.callTo);
        type(By.name("form:j_idt272"), oraganisation.postCode);
        type(By.name("form:j_idt276"), oraganisation.address);
        type(By.name("form:j_idt280"), oraganisation.phoneNumber);
        click(By.id("form:btnSelfRegistrationUser"));
    }

    /**
     * Метод заполняется форму регистрации переданного типа поверенного
     */
    public void fillRegistrationFormForPatentAgent(String agentType) {
        goToRegisterPage();
        String regNumber;
        String email;
        PatentAgent patentAgent = new PatentAgent(agentType);
        optionPicker(By.id("form:radioPanelId"), 1);
        if ("industrial".equals(agentType)) {
            click(By.id("form:certificationTypeRadio:1"));
            regNumber = patentAgent.agentRegNumberIndustrialdesign[getRandomInt(3)];
        } else {
            regNumber = patentAgent.agentRegNumberInvention[getRandomInt(3)];
        }
        click(By.name("form:j_idt218"));
        type(By.name("form:j_idt218"), regNumber);
        click(By.id("form:btnCheckUser"));
        email = getElementAttrValue(By.id("form:email"), "value");
        type(By.id("form:emailToConfirm"), email);
        click(By.id("form:btnSelfRegistrationUser"));
    }

    /**
     * Метод получающий сообщение об отправке запроса на регистрацию
     */
    public String getRegistrationRequestMessageConfirm() throws InterruptedException {
        String massege = getTextFromElement(By.cssSelector("li span[class='error-message']")).split("\\.")[0];
        return massege;
    }
}
