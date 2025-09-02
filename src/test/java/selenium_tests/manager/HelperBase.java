package selenium_tests.manager;

import com.github.javafaker.Faker;
import exceptions.TooManyLoopsException;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Базовый класс-родитель для всех классов-помощников. Содержит общие методы для взаимодействия с элементами страниц.
 */
public class HelperBase {
    protected final ApplicationManager manager;

    /**
     * Конструктор класса. Создает переменную, содержащую ApplicationManager
     */
    public HelperBase(ApplicationManager manager) {
        this.manager = manager;
    }

    /**
     * Метод для заполнения полей символами.
     * Метод адаптирован под обновление страницы при заполнении полей различных форм.
     */
    protected void type(By locator, String text, boolean hasDelay) {
        for (var i = 1; i <= 3; i++) {
            try {
                WebElement element = presenceOfElement(locator);
                element.clear();
                element.sendKeys(text);
                if (hasDelay) {
                    TimeUnit.MILLISECONDS.sleep(400);
                }
                return;
            } catch (StaleElementReferenceException exception) {
                System.out.println("Try to type, but get StaleElementReferenceException");
            } catch (ElementNotInteractableException exception) {
                System.out.println("Try to type, but get ElementNotInteractableException");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Метод осуществляет ЛКМ по веб-элементу.
     * Метод адаптирован под обновление страницы при заполнении полей различных форм.
     */
    public void click(By locator, boolean hasDelay) {
        for (var i = 1; i <= 10; i++) {
            try {
                WebElement element = presenceOfElement(locator);
                element.click();
                if (hasDelay) {
                    TimeUnit.MILLISECONDS.sleep(400);
                }
                return;
            } catch (StaleElementReferenceException exception) {
                System.out.println("Try to click, but get StaleElementReferenceException");
            } catch (ElementNotInteractableException exception) {
                System.out.println("Try to click, but get ElementNotInteractableException");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

    /**
     * Метод для получения текста, содержащегося в веб-элементе
     */
    public String getTextFromElement(By locator) {
        String elemText = presenceOfElement(locator).getText();
        return elemText;
    }


    /**
     * Метод для получения значения атрибута веб-элемента
     */
    protected String getElementAttrValue(By locator, String attrName) {
        String text = presenceOfElement(locator).getAttribute(attrName);
        return text;
    }


    /**
     * Метод для выбора элементов выпадающего списка типа select по индексу
     */
    protected void optionPicker(By locator, int index, boolean hasDelay) {
        for (var i = 1; i <= 10; i++) {
            try {
                Select option = new Select(presenceOfElement(locator));
                option.selectByIndex(index);
                if (hasDelay) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                option.selectByIndex(index); // Костыль. Не удалять!
                return;
            } catch (StaleElementReferenceException exception) {
                System.out.println("Try to pick the option, but get StaleElementReferenceException");
            } catch (NoSuchElementException exception) {
                System.out.println("Try to pick the option, but get NoSuchElementException");
            }
        }
    }

    /**
     * Метод для выбора случайного элемента выпадающего списка типа select
     */
    protected void randomOptionPicker(By locator) {
        Select option = new Select(presenceOfElement(locator));
        List<WebElement> listOfOptions = option.getOptions();
        int index = getRandomInt(listOfOptions.size() - 1);
        if (index == 0) {
            index++;
        }
        option.selectByIndex(index);
    }

    /**
     * Метод явного ожидания присутствия веб-элемента на странице
     */
    protected WebElement presenceOfElement(By locator) {
        WebElement element = new WebDriverWait(manager.driver, Duration.ofSeconds(15)).until(ExpectedConditions.presenceOfElementLocated(locator));
        return element;
    }

    /**
     * Метод явного ожидания присутствия списка элементов на странице
     */
    protected List<WebElement> presenceOfElements(By locator) {
        List<WebElement> elements = new WebDriverWait(manager.driver, Duration.ofSeconds(15)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return elements;
    }

    /**
     * Метод возвращает случайное число от [0, ceil)
     *
     * @param ceil int - верхняя граница
     */
    protected int getRandomInt(int ceil) {
        Random rand = new Random();
        return rand.nextInt(ceil);
    }

    /**
     * Метод формирует случайный номер заявки
     */
    protected String getRandomAppNumber() {
        Faker fakerRU = new Faker(new Locale("ru"));
        return fakerRU.number().digits(9);
    }

    /**
     * Метод загружает документ
     */
    protected void fileUpload(By locator, String path, boolean hasDelay) {
        WebElement element = presenceOfElement(locator);
        for (int i = 0; i < 5; i++) {
            try {
                element.sendKeys(path);
                if (hasDelay) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return;
            } catch (StaleElementReferenceException exception) {
                System.out.println("Try to upload, but get StaleElementReferenceException");

            }
        }
    }

    /**
     * Метод загружает документ на портале и ждет окончания загрузки
     */
    public void fileUploadWithCheck(String locator, String path) {
        WebElement element = presenceOfElement(By.xpath(locator));
        element.sendKeys(path);
        waitingFor(locator + "/ancestor::tr[position()=1]//input[@title='Cохранить файл на диск']", 45);

    }

    protected void waitingFor(String locator, int secToWait) {
        WebDriverWait wait = new WebDriverWait(manager.driver, Duration.ofSeconds(secToWait));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }


    /**
     * Метод проверяет наличие элемента в DOM
     */
    protected boolean isElementPresent(By locator) {
        boolean result = manager.driver.findElements(locator).isEmpty();
        return !result;
    }


    /**
     * Метод извлекает номер заявки
     */
    public String extractAppNumber(By locator) {
        String rawElementContent = getTextFromElement(locator);
        String appNumber = rawElementContent.split(" ")[2];
        return appNumber;
    }


    /**
     * Метод выбирает раздел аккаунта
     */
    @Step("Выбор секции аккаунта")
    public void selectSectionOfAccount(String typeSection) {
        if ("invention".equals(typeSection)) {
            click(By.xpath("//span[contains(text(), 'Изобретения')]"), true);
        } else if ("industrial".equals(typeSection)) {
            click(By.xpath("//span[contains(text(), 'Промышленные')]"), true);
        } else if ("profile".equals(typeSection)) {
            click(By.xpath("//span[contains(text(), 'Профиль')]"), true);
        }
    }

    /**
     * Метод добавляет 2 и последующую форму для заявителя\автора\изобретателя\представителя
     */
    public void addNewNextEntityForm() throws TooManyLoopsException {
        int currentCount = manager.driver.findElements(By.id("tablePersonId")).size();
        int newCount = -1;
        int loopCount = 0;
        while (newCount <= currentCount) {
            if (loopCount > 4) {
                throw new TooManyLoopsException("When add new entity form, loop count exceeds 3");
            }
            click(By.xpath("//input[contains(@value, 'Добавить нового')]"), true);
            newCount = presenceOfElements(By.id("tablePersonId")).size();
            loopCount++;
        }
    }

    /**
     * Метод нажимает кнопку "Далее". Можно использовать вместо обычного нажатия и для случаев, когда не нажимается с первого раза
     */
    public void pressNextButton() throws TooManyLoopsException {
        String currentHeader = getTextFromElement(By.xpath("//td[not(@style='display : none') and contains(@class, '-active')]/span[@class='rf-tab-lbl']"));
        String newHeader = getTextFromElement(By.xpath("//td[not(@style='display : none') and contains(@class, '-active')]/span[@class='rf-tab-lbl']"));
        int loopCount = 0;
        while (currentHeader.equals(newHeader)) {
            if (loopCount > 4) {
                throw new TooManyLoopsException("When press next button, loop count exceeds 3");
            }
            click(By.cssSelector("input[value='Далее']"), true);
            newHeader = getTextFromElement(By.xpath("//td[not(@style='display : none') and contains(@class, '-active')]/span[@class='rf-tab-lbl']"));
            loopCount++;
        }
    }




}





