package selenide_tests.manager;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Класс содержит методы для направления запроса гос органа или нац ведомства
 */
public class RequestHelper extends HelperBase {

    /**
     * Метод вводит номер заявки по которой будет направлять запрос гос органа\ведомтсва
     */
    @Step("Ввод номера заявки для подачи заявления об изменении")
    public void typeAppNumberForRequest(String appNumber) {
        $(By.xpath("//span[contains(text(), 'Запрос нацведомства / Гос. органа')]/ancestor::div[contains(@id, 'input-box_container')]//input[contains(@id, 'inputBox')]")).setValue(appNumber);
        $(By.xpath("//span[contains(text(), 'Запрос нацведомства / Гос. органа')]/ancestor::div[contains(@id, 'input-box_container')]//input[@value='Подать']")).click();
    }

    /**
     * Метод загружает запрос
     */
    @Step("Загрузка запроса")
    public void uploadRequest() {
        uploadFileWithCheck("//td[contains(text(), 'Запрос нац. пат. ведомства / Государственного органа: ')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_changed_apps/Документ_о_правопреемстве%.pdf");
    }

    /**
     * Метод загружает приложения к запросу
     */
    @Step("Загрузка приложений к запросу")
    public void uploadAttachments() {
        $(By.xpath("//input[contains(@name, 'addOtherDocId')]")).click();
        $(By.xpath("//select")).selectOptionByValue("OTHER_DOC");
        uploadFileWithCheck("//div[contains(@id, 'uploaddocId')]//input[@type='file']", "src/test/resources/file_to_upload/doc_changed_apps/Документ_о_правопреемстве%.pdf");
    }

    /**
     * Метод сохраняет досылку с запросом
     */
    @Step("Сохранение досылки с запросом")
    public void saveRequest () {
        $(By.xpath("//input[contains(@id, 'btnSave')]")).click();
    }

}
