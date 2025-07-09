package selenide_tests.manager;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

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
     * Класс содержит методы для подачи заявлений об изменении заявителя\владельцеца\адреса
     * по заявке и по патенту
     */
    public void selectTypeOfChange(String typeOfChange) {
        if (typeOfChange.equals("succession")) {
            $("input[value='succession']").click();
        }
    }


}
