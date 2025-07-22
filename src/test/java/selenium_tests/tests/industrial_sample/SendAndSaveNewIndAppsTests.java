package selenium_tests.tests.industrial_sample;

import exceptions.NextButtomException;
import fixture.ConfigProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jdbc.JdbcHelper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import selenium_tests.tests.TestSeleniumBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.FileUtils.*;

@Epic("Отправка новых заявок")
@DisplayName("Класс с тестами подачи заявок на ПО")
class SendAndSaveNewIndAppsTests extends TestSeleniumBase {

    @Nested
    @Feature("Отправка обычных заявок на ПО")
    @DisplayName("Вложенный класс с тестами подачи обычных заявок на ПО")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class SendAndSaveUsualAppsTests {

        @Test
        @Order(1)
        @DisplayName("Тест заявки на ПО без приоритета по 1 ПО с загрузкой всех возможных файлов")
        @Story("Отправка заявки без приоритета по 1 ПО с загрузкой всех возможных документов")
        @Description("Тест заявки на ПО без приоритета по 1 ПО с загрузкой всех возможных файлов, в т.ч. 3D и доп. документами. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitIndustrialEuroApplicationTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillIndustrialCommonInfoPart();
            app.sender().addNewApplicants(3);
            app.sender().addNewInventors(3);
            app.sender().addNewRepresentative();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillAppDocumentForm("industrial");
            app.sender().fillIndustrialPrototypeWithAdditionalSamples(1, true);
            app.sender().fillTaxFormIndustrial();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            applicationNumbersWriter("src/test/resources/list_of_app/industrialAppNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.saver().saveDocToSoprano("заявки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(7, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(2)
        @DisplayName("Тест заявки на ПО без приоритета по 3 ПО с приоритетами (предшествующей + доп. мат. + открытый показ) и несколькими заявителями и авторами")
        @Story("Отправка заявки с 3 приоритетами и несколькими заявителями/авторами")
        @Description("Тест заявки на ПО с 3 ПО с приоритетами (предшествующей + доп. мат. + открытый показ) и несколькими заявителями и авторами. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitIndustrialEuroApplicationWithPrioritiesTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
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
            applicationNumbersWriter("src/test/resources/list_of_app/industrialAppNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.saver().saveDocToSoprano("заявки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(3)
        @Tag("SkipInit")
        @DisplayName("Тест проверки сохранения документов в Madras")
        @Story("Проверка сохранения статики в Madras")
        @Description("Тест для проверки сохранения документов в Madras по проведенным тестам")
        public void checkSaveDocsToMadrasTest() {
            try {
                Thread.sleep(90000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> appNumbers = applicationNumbersReader("src/test/resources/list_of_app/industrialAppNumbers.txt");
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
         * Метод удаляет файл с заявками
         */
        @AfterAll
        public static void docsCleaner() {
            fileDeleter("src/test/resources/list_of_app");
        }


    }

    @Nested
    @Feature("Отправка выделенных заявок на ПО")
    @DisplayName("Вложенный класс с тестами подачи выделенных заявок на ПО")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class SendAndSaveAllocatedAppsTests {

        @Test
        @Order(1)
        @DisplayName("Тест подачи выделенной заявки с 1 ПО")
        @Story("Отправка выделенной заявки с 1 ПО")
        @Description("Тест подачи выделенной заявки с 1 ПО. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitIndAllocatedAppTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillIndustrialCommonInfoPart();
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            app.sender().addNewRepresentative();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillIndustrialPrototype(0, false);
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormIndustrial();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.saver().saveDocToSoprano("заявки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.sender().selectTypeOfApplication("allocatedApp");
            app.sender().typeAppNumberForAllocatedApp(appNumber);
            String errorMessage = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Подача выделенной евразийской заявки невозможна, так как в первоначальной заявке только один промышленный образец", errorMessage); // проверка блока подачи заявки т.к. 1 ПО
        }

        @Test
        @Order(2)
        @DisplayName("Тест подачи выделенной заявки с 3 ПО")
        @Story("Отправка выделенной заявки с 3 ПО")
        @Description("Тест подачи выделенной заявки с 3 ПО. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitIndAllocatedAppWithThreeSampleTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
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
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.saver().saveDocToSoprano("заявки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.sender().selectTypeOfApplication("allocatedApp");
            app.sender().typeAppNumberForAllocatedApp(appNumber);
            app.sender().click(By.xpath("//td[not(@style='display : none')]/span[contains(text(), 'ПРОМЫШЛЕННЫЕ ОБРАЗЦЫ')]"), true);
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormIndustrial();
            app.sender().signInApplication();
            sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            applicationNumbersWriter("src/test/resources/list_of_app/industrialAppNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("industrial");
            app.saver().saveDocToSoprano("заявки", appNumber);
            savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(3)
        @Tag("SkipInit")
        @DisplayName("Тест проверки сохранения документов в Madras")
        @Story("Проверка сохранения статики в Madras")
        @Description("Тест для проверки сохранения документов в Madras по проведенным тестам")
        public void checkSaveDocsToMadrasTest() {
            try {
                Thread.sleep(90000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> appNumbers = applicationNumbersReader("src/test/resources/list_of_app/industrialAppNumbers.txt");
            ArrayList<Integer> actualCount = new ArrayList<>();
            for (String number : appNumbers) {
                int count = app.jdbc().checkDocsInMadras(number);
                actualCount.add(count);
            }
            Collections.sort(actualCount);
            ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(11));
            assertEquals(expectedCount, actualCount);
        }

        /**
         * Метод удаляет файл с заявками
         */
        @AfterAll
        public static void docsCleaner() {
            fileDeleter("src/test/resources/list_of_app");
        }

    }

    /**
     * Метод закрывает соединение с БД
     */
    @AfterAll
    public static void tearDown() {
        JdbcHelper jdbc = app.jdbc();
        jdbc.closePortalConnection();

    }


}
