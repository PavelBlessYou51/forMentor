package tests.industrial_sample;

import exceptions.NextButtomException;
import manager.JdbcHelper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tests.TestBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SendAndSaveNewIndAppsTests extends TestBase {

    /**
     * Тест заявки на ПО без приоритета по 1 ПО с загрузкой всех возможных файлов,
     * в т.ч. 3D и доп. документами
     */
    @Test
    @Order(1)
    public void submitIndustrialEuroApplicationTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("industrial");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillIndustrialCommonInfoPart();
        app.sender().addNewApplicants(3);
        app.sender().addNewInventors(3);
        app.sender().addNewRepresentative();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillAppDocumentForm("industrial");
        app.sender().fillIndustrialPrototypeWithAdditionalSamples(1);
        app.sender().fillTaxFormIndustrial();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/industrialAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("industrial");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(7, sopranoRecords); // проверка формирования записей в Soprano
    }

    /**
     * 3 ПО с приоритетами (предшествующей + доп. мат. + открытый показ) и несколькими заявителями и авторами
     */
    @Test
    @Order(2)
    public void submitIndustrialEuroApplicationWithPrioritiesTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("industrial");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillIndustrialCommonInfoPart();
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        app.sender().addNewRepresentative();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillIndustrialPrototypeWithAllPriorities(4);
        app.sender().fillTaxFormIndustrial();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/industrialAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("industrial");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
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
            Thread.sleep(90000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> appNumbers = app.sender().applicationNumbersReader("src/test/resources/list_of_app/industrialAppNumbers.txt");
        ArrayList<Integer> actualCount = new ArrayList<>();
        for (String number : appNumbers) {
            int count = app.jdbc().checkDocsInMadras(number);
            actualCount.add(count);
        }
        Collections.sort(actualCount);
        ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(13, 30));
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Метод закрывает соединение с БД
     */
    @AfterAll
    public static void tearDown() {
        JdbcHelper jdbc = app.jdbc();
        jdbc.closePortalConnection();

    }


    /**
     * Метод удаляет файл с заявками
     */
    @AfterAll
    public static void docsCleaner() {
        app.session().fileDeleter("src/test/resources/list_of_app");
    }

}
