package selenide_tests.manager;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Класс содержит базовые методы для работы со страницей
 */
public class HelperBase {

    /**
     * Метод для получения текста, содержащегося в веб-элементе
     */
    public String getTextFromElement(By locator) {
        String elemText = $(locator).getText();
        return elemText;
    }

    /**
     * Метод выбирает раздел аккаунта
     */
    public void selectSectionOfAccount(String typeSection) {
        if ("invention".equals(typeSection)) {
            $(By.xpath("//span[contains(text(), 'Изобретения')]")).click();
        } else if ("industrial".equals(typeSection)) {
            $(By.xpath("//span[contains(text(), 'Промышленные')]")).click();
        } else if ("profile".equals(typeSection)) {
            $(By.xpath("//span[contains(text(), 'Профиль')]")).click();
        }

    }
}
