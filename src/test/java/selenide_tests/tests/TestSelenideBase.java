package selenide_tests.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import fixture.ConfigProvider;
import org.junit.jupiter.api.BeforeEach;
import selenide_tests.manager.ApplicationManager;

public class TestSelenideBase {
    protected static ApplicationManager app;

    @BeforeEach
    public void setUp() {
        if (app == null) {
            app = new ApplicationManager();
        }
        Configuration.browser = "chrome";
        Configuration.browserSize = "2560x1080";
        Configuration.pageLoadTimeout = 10000;
        Selenide.open(ConfigProvider.getBaseUrl());
    }

}
