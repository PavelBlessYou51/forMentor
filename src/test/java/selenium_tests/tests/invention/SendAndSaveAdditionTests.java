package selenium_tests.tests.invention;

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

/**
 * Класс с тестами подачи дополнительных материалов
 */
@Epic("Подача дополнительных материалов")
@DisplayName("Класс с тестами подачи дополнительных материалов")
public class SendAndSaveAdditionTests extends TestSeleniumBase {

    /**
     * Класс с тестами подачи измененных заявлений на ИЗО
     */
    @Nested
    @Feature("Подача измененных заявлений на ИЗО")
    @DisplayName("Вложенный класс с тестами подачи измененных заявлений по ИЗО")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendChangedApplicationTests {

        @Test
        @Order(1)
        @DisplayName("Тест подачи измененной евразийской заявки со всеми документами")
        @Story("Подача измененной евразийской заявки со всеми документами")
        @Description("Тест подачи измененной евразийской заявки со всеми документами. Подается новая первоначальная заявка. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitChangedApplicationTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillInventionCommonInfoPart();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("changedApp");
            app.sender().typeAppNumberForChangedApp(appNumber);
            app.sender().fillInventionCommonInfoPart();
            for (int i = 0; i < 6; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillAppDocumentFormForMadras(false);
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(2)
        @DisplayName("Тест подачи измененной PCT заявки")
        @Story("Подача измененной PCT заявки со всеми документами")
        @Description("Тест подачи измененной PCT заявки. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitChangedPCTApplicationTest() throws NextButtomException {
            String PCTNumber = app.jdbc().getPCTData();
            app.jdbc().deletePCTRecord(PCTNumber);
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("PCT");
            app.sender().fillPCTCommonInfoPart(PCTNumber);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            app.sender().addNewRepresentative();
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("changedApp");
            app.sender().typeAppNumberForChangedApp(appNumber);
            app.sender().fillInventionCommonInfoPart();
            for (int i = 0; i < 6; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillPCTDocumentForm();
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(3)
        @DisplayName("Тест подачи измененной выделенной заявки")
        @Story("Подача измененной выделенной заявки со всеми документами")
        @Description("Тест подачи измененной выделенной заявки. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitChangedAllocatedApplicationTest() throws NextButtomException {
            String appNumber = app.jdbc().getInventionApp();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("allocatedApp");
            app.sender().typeAppNumberForAllocatedApp(appNumber);
            app.sender().fillInventionCommonInfoPart();
            for (int i = 0; i < 6; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("changedApp");
            app.sender().typeAppNumberForChangedApp(appNumber);
            app.sender().fillInventionCommonInfoPart();
            for (int i = 0; i < 6; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillAppDocumentForm("invention");
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano

        }

        @Test
        @Order(4)
        @Tag("SkipInit")
        @DisplayName("Тест проверки сохранения документов в Madras")
        @Story("Проверка сохранения статики в Madras")
        @Description("Тест для проверки сохранения документов в Madras по проведенным тестам")
        public void checkSaveDocsToMadrasTest() {
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> appNumbers = applicationNumbersReader("src/test/resources/list_of_app/inventionAdditionNumbers.txt");
            ArrayList<Integer> actualCount = new ArrayList<>();
            for (String number : appNumbers) {
                int count = app.jdbc().checkDocsInMadras(number);
                actualCount.add(count);
            }
            Collections.sort(actualCount);
            ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(15, 16, 25));
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
     * Класс с тестами подачи досылок на ИЗО
     */
    @Nested
    @Feature("Подача досылок по ИЗО")
    @DisplayName("Вложенный класс с тестами подачи досылок по ИЗО")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendAdditionTests {

        @Test
        @Order(1)
        @DisplayName("Тест подачи досылки PCT заявке")
        @Story("Досылка к PCT заявке")
        @Description("Тест подачи досылки PCT заявке. Подается новая первоначальная заявка. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitAdditionPCTApplicationTest() throws NextButtomException {
            String PCTNumber = app.jdbc().getPCTData();
            app.jdbc().deletePCTRecord(PCTNumber);
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("PCT");
            app.sender().fillPCTCommonInfoPart(PCTNumber);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            app.sender().addNewRepresentative();
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("addition");
            app.sender().typeAppNumberForAddition(appNumber);
            app.sender().fillAdditionDocumentForm();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("досылки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(2)
        @DisplayName("Тест подачи досылки со всеми документами по евразийской заявке")
        @Story("Досылка со всеми документами по евразийской заявке")
        @Description("Тест подачи досылки со всеми документами по евразийской заявке. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitAdditionApplicationTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillInventionCommonInfoPart();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("addition");
            app.sender().typeAppNumberForAddition(appNumber);
            app.sender().fillAdditionDocFormForMadras();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("досылки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(3)
        @DisplayName("Тест подачи досылки выделенной евразийской заявке")
        @Story("Досылка к выделенной евразийской заявке")
        @Description("Тест подачи досылки к выделенной евразийской заявке. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitAdditionAllocatedApplicationTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillInventionCommonInfoPart();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillAppDocumentForm("invention");
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("allocatedApp");
            app.sender().typeAppNumberForAllocatedApp(appNumber);
            app.sender().fillInventionCommonInfoPart();
            for (int i = 0; i < 6; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("addition");
            app.sender().typeAppNumberForAddition(appNumber);
            app.sender().fillAdditionDocumentForm();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("досылки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(4)
        @Tag("SkipInit")
        @DisplayName("Тест проверки сохранения документов в Madras")
        @Story("Проверка сохранения статики в Madras")
        @Description("Тест для проверки сохранения документов в Madras по проведенным тестам")
        public void checkSaveDocsToMadrasTest() {
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> appNumbers = applicationNumbersReader("src/test/resources/list_of_app/inventionAdditionNumbers.txt");
            ArrayList<Integer> actualCount = new ArrayList<>();
            for (String number : appNumbers) {
                int count = app.jdbc().checkDocsInMadras(number);
                actualCount.add(count);
            }
            Collections.sort(actualCount);
            ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(14, 15, 22));
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
     * Класс с тестами подачи досылок на ИЗО с указанием даты
     */
    @Nested
    @Feature("Подача досылок на ИЗО с указанием даты")
    @DisplayName("Вложенный класс с тестами подачи досулок по ИЗО с указание даты")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendAdditionWithDateTests {

        @Test
        @Order(1)
        @DisplayName("Тест подачи досылки к евразийской заявке с указанием даты")
        @Story("Досылка к евразийской заявке с указанием даты")
        @Description("Тест подачи досылки к евразийской заявке с указанием даты. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
        public void submitAdditionWithDateTest() throws NextButtomException {
            app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillInventionCommonInfoPart();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.sender().click(By.xpath("//span[contains(text(), 'Подача заявок')]"), true);
            app.sender().selectTypeOfApplication("additionWithDate");
            app.sender().typeAppNumberForAdditionWithDate(appNumber);
            app.sender().fillAdditionDocumentForm();
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.sender().click(By.cssSelector("input[value='Продолжить']"), true);
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
        @Order(2)
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
            List<String> appNumbers = applicationNumbersReader("src/test/resources/list_of_app/inventionAdditionNumbers.txt");
            ArrayList<Integer> actualCount = new ArrayList<>();
            for (String number : appNumbers) {
                int count = app.jdbc().checkDocsInMadras(number);
                actualCount.add(count);
            }
            Collections.sort(actualCount);
            ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(15));
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
