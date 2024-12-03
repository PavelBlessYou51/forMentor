package manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


/**
 * Класс содержит методы для работы с браузером, а также методы для получения классов-помощников
 */
public class ApplicationManager {

    protected WebDriver driver;
    private LoginHelper session;
    private RegistrationHelper registrator;
    private SubmitHelper submitter;
    private SaveAppHelper sender;

    /**
     * Инициализатор браузера. Масштабирует окно браузера на максимальный размер экрана
     */
    public void init() {
        driver = new ChromeDriver();
        driver.get("https://portal8.eapo.org/olf");
        driver.manage().window().maximize();
    }

    /**
     * Закрывает браузер
     */
    public void quit() {
        driver.quit();
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом авторизации
     */
    public LoginHelper session() {
        if (session == null) {
            session = new LoginHelper(this);
        }
        return session;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом регистрации
     */
    public RegistrationHelper registrator() {
        registrator = new RegistrationHelper(this);
        return registrator;
    }


    /**
     * Возвращает класс-помощник для взаимодействия с функционалом подачи заявок
     */
    public SubmitHelper submitter() {
        submitter = new SubmitHelper(this);
        return submitter;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом сохранения заявок и досылок в Soprano
     */
    public SaveAppHelper sender() {
        sender = new SaveAppHelper(this);
        return sender;
    }



}

