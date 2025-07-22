package selenium_tests.manager;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Класс-помощник. Содержит методы, связанные с авторизацией
 */
public class LoginHelper extends HelperBase {

    public LoginHelper(ApplicationManager manager) {
        super(manager);
    }

    /**
     * Метод осуществляет авторизацию
     */
    @Step("Вход в ЛК")
    public void login(String login, String password) {
        click(By.xpath("//a[contains(text(), 'Войти')]"), false);
        type(By.id("j_username"), login, false);
        type(By.id("j_password"), password, false);
        click(By.cssSelector("input[value='Вход']"), false);
    }

    /**
     * Метод выхода из учетной записи
     */
    @Step("Выход из ЛК")
    public void logout() {
        click(By.cssSelector("a[class='logged-in-welcome logged-out']"), false);
    }

    /**
     * Метод для проверки авторизации под учетной записью
     */
    @Step("Получения подтверждения входа в ЛК")
    public String[] getConfirmLoginMessage() {
        String[] messageText = new String[2];
        String welcomeText = getTextFromElement(By.cssSelector("span[id*='userRoleText']")).split(", ")[0];
        String exitText = getTextFromElement(By.cssSelector("a[class='logged-in-welcome logged-out']"));
        messageText[0] = welcomeText;
        messageText[1] = exitText;
        return messageText;
    }

    /**
     * Метод для проверки нахождения на стартовой странице
     */
    @Step("Получения подтверждения выхода из ЛК")
    public String getConfirmLogoutMessage() {
        String messageText = getTextFromElement(By.xpath("//a[contains(text(), 'Войти')]"));
        return messageText;
    }




}
