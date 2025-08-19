package selenide_tests.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import fixture.ConfigProvider;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import selenide_tests.manager.ApplicationManager;

import static com.codeborne.selenide.FileDownloadMode.PROXY;

public class TestSelenideBase {
    protected static ApplicationManager app;

    @BeforeEach
    @Step("Инициализация браузера")
    public void setUp(TestInfo testInfo) {
        if (app == null) {
            app = new ApplicationManager();
        }
        if (testInfo.getTags().contains("SkipInit")) {
            return;
        }
        Configuration.browser = "chrome";
        Configuration.pageLoadTimeout = 10000;
        Configuration.proxyEnabled = true;
        Configuration.fileDownload = PROXY;
        Selenide.open(ConfigProvider.getBaseUrl());
        WebDriverRunner.getWebDriver().manage().window().maximize();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
        );
    }
    // если не сработает сканивание файла, то удалить proxyEnabled, fileDownload = PROXY; поменять на FOLDER

}
