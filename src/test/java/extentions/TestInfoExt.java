package extentions;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import selenium_tests.tests.TestSeleniumBase;

import static utils.SeleniumScreenshotUtils.takeScreenshotToAllureReport;


public class TestInfoExt implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    public static String testMethodName;
    public static String testName;


    @Override
    public void beforeTestExecution(ExtensionContext context) {
        testMethodName = context.getTestMethod().get().getName();
        testName = context.getDisplayName();
    }


    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            WebDriver driver = TestSeleniumBase.app.driver;
            if (driver != null) {
                takeScreenshotToAllureReport(driver);
            }
        }
    }
}

