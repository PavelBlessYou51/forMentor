package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс содержить тестовые классы с методами подачи и сохранения заявок в Soprano
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class SubmitAndSaveAppsTests extends TestBase {

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
            String sendingConfirm = app.submitter().sendIndustrialApplication(countOfSamples);
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
            String sendingConfirm = app.submitter().sendIndustrialApplicationWithPriority(priorityType);
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
            String sendingConfirm = app.submitter().sendInventionApplication(priorityType);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

        /**
         * Тест заявки на изобретение с ходатайством
         */
        @Test
        public void SubmitInventionApplicationWithPetitionTest() throws InterruptedException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.submitter().sendInventionApplicationWithPetition();
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

        /**
         * Тест PCT заявки
         */
        @Test
        public void SubmitInventionPCTApplicationTest() throws InterruptedException {
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            String sendingConfirm = app.submitter().sendInventionPCTApplication();
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }


    }

    @Nested
    @Order(3)
    public class SaveAppTests {

        @ParameterizedTest
        @ValueSource(strings = {"invention", "industrial"})
        public void saveInventionApp(String appType) {
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            boolean result = app.sender().saveInventionAppsToSoprano(appType);
            assertTrue(result);
        }

        /**
         * Метод удаляет списки файлы со списками заявок после выполнения всех тестов во вложенном классе
         */
        @AfterAll
        public static void cleaner() {
            app.session().fileDeleter("src/test/resources/list_of_app");

        }


    }
}
