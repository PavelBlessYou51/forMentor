package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс содержить тестовые классы с методами подачи и сохранения заявок в Soprano
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class SendAndSaveAppAndAdditionTests extends TestBase {

    @Nested
    @Order(1)
    public class SubmitApplicationIndustrialTests {

        /**
         * Параметризированный тест подачи заявок на ПО (1, 3, 5 образцов)
         */
        @ParameterizedTest
        @ValueSource(ints = {1, 3, 5})
        public void SubmitIndustrialApplicationTest(int countOfSamples) throws InterruptedException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendIndustrialApplication(countOfSamples);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

        /**
         * Параметризированный тест подачи заявок на ПО.
         * priorityType: тип приоритета
         */
        @ParameterizedTest
        @ValueSource(strings = {"previousPCT", "previousEuro", "additionalMaterials", "startsOpenShowing"})
        public void SubmitIndustrialApplicationWithPriorityTest(String priorityType) throws InterruptedException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendIndustrialApplicationWithPriority(priorityType);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

    }

    @Nested
    @Order(2)
    public class SubmitApplicationInventionTests {

        /**
         * Параметризированный тест подачи заявок на изобретения.
         * priorityType: тип приоритета или его отсутствие
         */
        @ParameterizedTest
        @ValueSource(strings = {"withoutPreority", "previousPCT", "previousEuro", "additionalMaterials", "startsOpenShowing"})
        public void SubmitInventionApplicationTest(String priorityType) throws InterruptedException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendInventionApplication(priorityType);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

        /**
         * Тест заявки на изобретение с ходатайством
         */
        @Test
        public void SubmitInventionApplicationWithPetitionTest() throws InterruptedException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendInventionApplicationWithPetition();
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

        /**
         * Тест PCT заявки
         */
        @Test
        public void SubmitInventionPCTApplicationTest() throws InterruptedException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendInventionPCTApplication();
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }


    }

    @Nested
    @Order(3)
    public class SaveAppTests {

        /**
         * Параметризированный тест сохранения заявок в Soprano
         */
        @ParameterizedTest
        @ValueSource(strings = {"invention", "industrial"})
        public void saveInventionApp(String appType) {
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            boolean result = app.saver().saveDocsToSoprano(appType, "заявки");
            assertTrue(result);
        }

    }

    @Nested
    @Order(4)
    public class SubmitIndustrialAdditionTests {


        /**
         * Фабричная функция для предоставления номеров заявок в тест подачи досылок
         */
        static List<String> InventionAppNumbersProvider() throws IOException {
            List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/inventionAppNumbers.txt").toAbsolutePath());
            return listOfApps;
        }

        /**
         * Параметризированный тест подачи досылок по изобретениям
         */
        @ParameterizedTest
        @MethodSource("InventionAppNumbersProvider")
        public void SubmitInventionAdditionalTest(String appNumber) {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendAdditionForInventionApp(appNumber);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }


    }

    @Nested
    @Order(5)
    public class SaveAdditionTests {

        /**
         * Параметризированный тест сохранения досылок
         */
        @ParameterizedTest
        @ValueSource(strings = {"invention", "industrial"})
        public void saveInventionApp(String appType) {
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            boolean result = app.saver().saveDocsToSoprano(appType, "досылки");
            assertTrue(result);
        }


        /**
         * Метод удаляет списки файлы со списками заявок после выполнения тестов сохранения заявок и досылок
         */
        @AfterAll
        public static void docsCleaner() {
            app.session().fileDeleter("src/test/resources/list_of_app");

        }


    }

}
