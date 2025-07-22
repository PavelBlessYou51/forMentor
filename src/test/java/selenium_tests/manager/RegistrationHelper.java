package selenium_tests.manager;

import io.qameta.allure.Step;
import model.OrganisationData;
import model.PatentAgent;
import model.PersonData;
import org.openqa.selenium.By;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс-помощник. Содержит методы, связанные с регистрацией
 */
public class RegistrationHelper extends HelperBase {

    public static final Map<String, Integer> COUNTRY_CODES;

    static {
        HashMap<String, Integer> tempMap = new HashMap<>();
        tempMap.put("AZ", 0);
        tempMap.put("KG", 1);
        tempMap.put("AM", 2);
        tempMap.put("BY", 3);
        tempMap.put("KZ", 4);
        tempMap.put("TJ", 5);
        tempMap.put("RU", 6);
        tempMap.put("TM", 7);
        COUNTRY_CODES = Collections.unmodifiableMap(tempMap);
    }

    /**
     * Конструктор класса. Явно вызывает конструктор родителя
     */
    public RegistrationHelper(ApplicationManager manager) {
        super(manager);
    }

    /**
     * Метод для перехода на страницу с формой регистрации
     */
    @Step("Переход на страницу регистрации")
    public void goToRegisterPage() {
        click(By.xpath("//a[contains(text(), 'Регистрация')]"), true);
    }

    /**
     * Метод заполняется форму регистрации для физического лица
     */
    @Step("Заполнение формы регистрации физ лица")
    public String fillRegistrationFormForPerson() {
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
        randomOptionPicker(By.xpath("(//select)[3]"));
        type(By.id("form:appeal"), person.callTo, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[2]/input"), person.postCode, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[4]/input"), person.address, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[6]/input"), person.phoneNumber, false);
        click(By.id("form:btnSelfRegistrationUser"), false);
        return person.surname;
    }

    /**
     * Метод заполняется форму регистрации для юридического лица
     */
    @Step("Заполнение формы регистрации юр лица")
    public String fillRegistrationFormForOrganisation() {
        goToRegisterPage();
        OrganisationData organisationData = new OrganisationData();
        optionPicker(By.xpath("//select[contains(@id, 'RadioPanelId')]"), 2, false);
        optionPicker(By.xpath("//span[contains(@id, 'CountryMenu')]//select"), COUNTRY_CODES.get(organisationData.countryCode), true);
        type(By.xpath("//input[contains(@id, 'input-lastname')]"), organisationData.surname, false);
        type(By.xpath("//input[contains(@id, 'input-firstname')]"), organisationData.name, false);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[3]"), organisationData.patronymic, true);
        type(By.id("form:email"), organisationData.email, true);
        type(By.id("form:emailToConfirm"), organisationData.email, true);
        type(By.xpath("//div[@class='registration-content-input-post']/input"), organisationData.position, false);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[6]"), organisationData.organisationName, false);
        //optionPicker(By.xpath("(//select[@class='reg-input-field-required'])[3] "), getRandomInt(3), true);
        randomOptionPicker(By.xpath("(//select)[3]"));
        type(By.id("form:appeal"), organisationData.callTo, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[2]/input"), organisationData.postCode, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[4]/input"), organisationData.address, false);
        type(By.xpath("//span[contains(@id, 'contactsGridId')]/div[6]/input"), organisationData.phoneNumber, false);
        type(By.xpath("(//div[@class='registration-content-input']/input[@class='reg-input-field-required'])[7]"), organisationData.uniqueID, false);
        click(By.id("form:btnSelfRegistrationUser"), false);
        return organisationData.surname;
    }

    /**
     * Метод заполняется форму регистрации переданного типа поверенного
     *
     */
    @Step("Заполнение формы регистрации патентного поверенного")
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
    @Step("Получения текста подтверждения регистрации")
    public String getRegistrationRequestMessageConfirm() {
        String massege = getTextFromElement(By.cssSelector("li span[class='error-message']")).split("\\.")[0];
        return massege;
    }
}


