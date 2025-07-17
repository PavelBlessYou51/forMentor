package selenium_tests.tests;

import org.junit.jupiter.api.DisplayName;
import selenium_tests.manager.ApplicationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

/**
 * Базовый тестовый класс для запуска иницализации и завершения работы браузера
 */
public class TestSeleniumBase {
    protected static ApplicationManager app;

    /**
     * Инициализатор. Выполняется перед запуском каждого теста
     */
    @BeforeEach
    @DisplayName("Инициализатор")
    public void init(TestInfo testInfo) {
        if (app == null) {
            app = new ApplicationManager();
        }
        if (testInfo.getTags().contains("SkipInit")) {
            return;
        }
        app.init();
    }


    /**
     * Финализатор. Выполняется после каждого теста
     */
//    @AfterEach
//    DisplayName("Финализатор")
//    public void quit() {
//        app.quit();
//    }

}
