package tests.industrial_sample;

import exceptions.NextButtomException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tests.TestBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SendAndSaveAdditionTests extends TestBase {

    /**
     * Тест подачи досылки с 1 ПО с загрузкой всех документов
     */
    @Test
    @Order(1)
    public void submitIndEuroAdditionWithOneSampleTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("industrial");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillIndustrialCommonInfoPart();
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        app.sender().addNewRepresentative();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillAppDocumentForm("industrial");
        app.sender().fillIndustrialPrototypeWithAdditionalSamples(1, false);
        app.sender().fillTaxFormIndustrial();
        app.sender().signInApplication();
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
        app.sender().addAllDocsInIndustrialAddition();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillIndustrialPrototypeInAddition(true);
        app.sender().fillTaxFormIndustrial();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation);
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/industrialAdditionNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("industrial");
        app.saver().saveDocToSoprano("досылки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
        assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
    }

    /**
     * Тест подачи досылки с 3 ПО только обязательные документы
     */
    @Test
    @Order(2)
    public void submitIndEuroAdditionWithThreeSamplesTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("industrial");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillIndustrialCommonInfoPart();
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        app.sender().addNewRepresentative();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillIndustrialPrototypeWithAdditionalSamples(3, false);
        app.sender().fillTaxFormIndustrial();
        app.sender().signInApplication();
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
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillIndustrialPrototypeInAddition(false);
        app.sender().fillTaxFormIndustrial();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation);
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/industrialAdditionNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("industrial");
        app.saver().saveDocToSoprano("досылки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
        assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
    }


    /**
     * Тест для проверки сохранения документов в Madras
     * по проведенным тестам
     */
    @Test
    @Order(3)
    @Tag("SkipInit")
    public void checkSaveDocsToMadrasTest() {
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> appNumbers = app.sender().applicationNumbersReader("src/test/resources/list_of_app/industrialAdditionNumbers.txt");
        ArrayList<Integer> actualCount = new ArrayList<>();
        for (String number : appNumbers) {
            int count = app.jdbc().checkDocsInMadras(number);
            actualCount.add(count);
        }
        Collections.sort(actualCount);
        ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(21, 39));
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Метод удаляет файл с заявками
     */
    @AfterAll
    public static void docsCleaner() {
        app.session().fileDeleter("src/test/resources/list_of_app");
    }
}
