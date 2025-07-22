package selenide_tests.manager;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginHelper extends HelperBase{

    /**
     * Метод осуществляет авторизацию
     */
    @Step("Вход в ЛК")
    public void login(String login, String password) {
        $(By.xpath("//a[contains(text(), 'Войти')]")).click();
        $(By.id("j_username")).setValue(login);
        $(By.id("j_password")).setValue(password);
        $(By.cssSelector("input[value='Вход']")).click();
    }

    /**
     * Метод выхода из учетной записи
     */
    @Step("Выход из ЛК")
    public void logout() {
        $(By.cssSelector("a[class='logged-in-welcome logged-out']")).click();
    }

}
