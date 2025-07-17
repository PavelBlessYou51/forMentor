package selenide_tests.manager;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import utils.FileUtils;

import java.io.File;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Класс содержит базовые методы для работы со страницей
 */
public class HelperBase {

    /**
     * Метод для получения текста, содержащегося в веб-элементе
     */
    public String getTextFromElement(By locator) {
        String elemText = $(locator).shouldBe(Condition.exist, Duration.ofSeconds(20)).getText();
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

    /**
     * Метод выбирает раздел внутри ИЗО или ПО
     */
    public void selectAction() {
        $(By.xpath("//a[contains(text(), 'Полученные досылки')]")).click();
    }

    /**
     * Метод нажимает кнопку "Далее" при заполнении формы заявления на изменения
     */
    public void pressNextButton() {
        sleep(500);
        String curTitle = $(By.xpath("//td[@class='application-header']")).getText();
        String nextTitle = $(By.xpath("//td[@class='application-header']")).getText();
        while (curTitle.equals(nextTitle)) {
            $("input[value='Далее']").shouldBe(Condition.visible, Condition.clickable, Condition.exist).click();
            nextTitle = $(By.xpath("//td[@class='application-header']")).getText();
        }

    }

    /**
     * Метод нажимает кнопку "Далее" при заполнении формы заявления на изменения
     */
    public void pressContinueButton() {
        $("input[value='Продолжить']").shouldBe(Condition.visible, Condition.clickable, Condition.exist).click();
    }

    /**
     * Метод загружает файл на портал с проверкой
     */
    public void uploadFileWithCheck(String locator, String filePath) {
        String absolutePath = FileUtils.getAbsolutePathToFile(filePath);
        File file = new File(absolutePath);
        $(By.xpath(locator)).uploadFile(file);
        $(By.xpath(locator + "/ancestor::tr[position()=1]//input[@title='Cохранить файл на диск']")).shouldBe(Condition.exist, Duration.ofSeconds(10));
    }

    /**
     * Метод загружает ПП об оплате гос пошлины
     */
    public void uploadPaymentOrder() {
        $("input[value='payment-document']").click();
        uploadFileWithCheck("//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Пп_об_оплате_ГП%.pdf");
    }

    /**
     * Метод подает и подписывет документ
     */
    public void signAndSendDocument() {
        $("input[value='Подписать и подать досылку']").click();
        $("input[value='Подписать и подать']").click();
    }

    /**
     * Метод осуществляет поиск заявки или досылки по номеру
     */
    public void findAppByNumber(String appNumber) {
        $(By.xpath("//span[contains(text(), '№ Евразийской заявки')]/following-sibling::input")).shouldBe(Condition.exist).setValue(appNumber);
        $("input[value='Найти']").shouldBe(Condition.clickable).click();
    }

    /**
     * Метод открывает найденную заявку или досылку, кликая по номеру
     */
    public void openFoundAppByNumber(String appNumber) {
        String locator = String.format("//a[contains(text(), '%s')]", appNumber);
        $(By.xpath(locator)).shouldBe(Condition.visible).click();
    }


}


