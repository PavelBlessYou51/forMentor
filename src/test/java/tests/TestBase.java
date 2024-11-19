package tests;

import manager.ApplicationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Базовый тестовый класс для запуска иницализации и завершения работы браузера
 */
public class TestBase {
    protected static ApplicationManager app;

    /**
     * Инициализатор. Выполняется перед запуском каждого теста
     */
    @BeforeEach
    public void init() {
        if (app == null) {
            app = new ApplicationManager();
        }
        app.init();
    }

    /**
     * Финализатор. Выполняется после каждого теста
     */
//    @AfterEach
//    public void quit() {
//        app.quit();
//    }

}
