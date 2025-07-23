package utils;

import extentions.TestInfoExt;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SeleniumScreenshotUtils {

    public static void takeScreenshotToAllureReport(WebDriver driver) {
        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        String name = TestInfoExt.testMethodName + "_" + TestInfoExt.testName  + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyyТHH-mm")) + ".png";
        Allure.attachment(name, new ByteArrayInputStream(screenshotBytes));
    }
}
