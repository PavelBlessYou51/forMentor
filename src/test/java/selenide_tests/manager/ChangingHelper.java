package selenide_tests.manager;

import com.codeborne.selenide.Condition;
import model.EntityDataBase;
import model.OrganisationData;
import model.PersonData;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

/**
 * Класс содержит методы для подачи заявлений об изменении заявителя\владельца\адреса
 * по заявке и по патенту
 */
public class ChangingHelper extends HelperBase {

    /**
     * Метод выбирает функционал подачи заявления об изменении
     */
    public void selectChangeApplication() {
        $("input[value='Передача права / Изменение имени или наименования / Изменение адреса']").click();
    }

    /**
     * Метод вводит номер заявки по которой будет подаваться заявление об изменении
     */
    public void typeAppNumberForChange(String appNumber) {
        $(By.xpath("//span[contains(text(), 'Передача права / Изменение имени или наименования / Изменение адреса')]/ancestor::div[contains(@id, 'input-box_container')]//input[contains(@id, 'inputBox')]")).setValue(appNumber);
        $(By.xpath("//span[contains(text(), 'Передача права / Изменение имени или наименования / Изменение адреса')]/ancestor::div[contains(@id, 'input-box_container')]//input[@value='Подать']")).click();
    }

    /**
     * Метод выбирает подать заявление об изменении
     */
    public void selectTypeOfChange(String typeOfChange) {
        if (typeOfChange.equals("succession")) {
            $("input[value='succession']").click();
        }
    }

    /**
     * Метод проверяет куда вносятся изменения: в заявку или патент
     */
    public boolean checkTypeOfApp(boolean isApp) {
        String header = getTextFromElement(By.xpath("//td[contains(text(), 'Передача права/Изменение имени или наименования')]"));
        String regex;
        if (isApp) {
            regex = ".+заявка.+\\d{9}";
        } else {
            regex = ".+заявка.+патент.+";
        }
        return header.matches(regex);

    }

    /**
     * Метод удаляет старого заявителя\владельца в форме подачи заявления
     */
    public void deleteOldOwners() {
        int ownCount = $$("input[value='Удалить заявителя']").size();
        for (int i = 0; i < ownCount; i++) {
            $("input[value='Удалить заявителя']").shouldBe(Condition.visible, Condition.clickable).click();
            sleep(600);
        }
    }

    /**
     * Метод удаляет нового заявителя\владельца в форме подачи заявления
     * ownerType: person, company, government физ. лицо\юр. лицо\гос. орг.
     */
    public void addNewOwner(boolean isPerson) {
        $("input[value='Добавить нового заявителя']").click();
        EntityDataBase newOwner;
        if (isPerson) {
            newOwner = new PersonData();
            $(By.xpath("//input[contains(@id, 'firstName')]")).setValue(newOwner.name);
            $(By.xpath("//textarea[contains(@id, 'name')]")).setValue(newOwner.surname);
            $(By.xpath("//input[contains(@id, 'middleName')]")).setValue(newOwner.patronymic);
        } else {
            newOwner = new OrganisationData();
            $(By.xpath("//div[not(@class)]/select")).selectOptionByValue("juridical-person");
            $(By.xpath("//textarea[contains(@id, 'name')]")).setValue(((OrganisationData) newOwner).organisationName);
        }
        $(By.xpath("//input[contains(@id, 'email')]")).setValue(newOwner.email);
        $(By.xpath("//input[contains(@id, 'country')]")).setValue(newOwner.countryCode);
        $(By.xpath("//input[contains(@id, 'phone')]")).setValue(newOwner.phoneNumber);
        $(By.xpath("//input[contains(@id, 'idTown')]")).setValue(newOwner.postCode);
        $(By.xpath("//textarea[contains(@id, 'address')]")).setValue(newOwner.address);

    }

    /**
     * Метод загружает документы к заявлению о смене владельца
     */
    public void uploadDocsForSuccession() {
        uploadFileWithCheck("//td[contains(text(), 'Документ, подтверждающий право на универсальное правопреемство')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_changed_apps/Документ_о_правопреемстве%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Перевод документа, подтверждающего право на универсальное правопреемство')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_changed_apps/Перевод_док_о_правопреемстве.pdf");
    }

    /**
     * Метод получает подтверждение отправки заявления на фронте
     */
    public String getSendingConfirm() {
        return getTextFromElement(By.cssSelector("span[class='error-message']"));
    }

    /**
     * Метод получает подтверждение охранения заявления в Soprano
     */
    public String getSavingConfirm() {
        return getTextFromElement(By.cssSelector("span[class='error-message']"));
    }

    /**
     * Метод сохраняет заявление об изменении
     */
    public void saveAppToSoprano() {
        $(By.xpath("//td[contains(@id, ':inactive')]/span[contains(text(), 'РАСЧЕТ ПОШЛИН')]")).should(Condition.exist).click();
        $(By.xpath("//input[contains(@id, 'btnSave')]")).shouldBe(Condition.visible, Condition.clickable).click();
    }


}
