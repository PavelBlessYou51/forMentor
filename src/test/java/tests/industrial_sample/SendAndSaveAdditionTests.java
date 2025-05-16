package tests.industrial_sample;

import exceptions.NextButtomException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import tests.TestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SendAndSaveAdditionTests extends TestBase {

    /**
     * Тест подачи досылки с 1 ПО с загрузкой всех документов
     */
    @Test
    @Order(1)
    public void submitIndustrialEuroApplicationTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("industrial");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillIndustrialCommonInfoPart();
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        app.sender().addNewRepresentative();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillAppDocumentForm("industrial");
        app.sender().fillIndustrialPrototypeWithAdditionalSamples(1, true);
        app.sender().fillTaxFormIndustrial();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("industrial");
        app.saver().saveDocToSoprano("заявки", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("industrial");
        app.sender().selectTypeOfApplication("addition");
        app.sender().typeAppNumberForAddition(appNumber);
        





    }
}
