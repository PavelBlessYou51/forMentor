package manager;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
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
                if (hasDelay) {
                    TimeUnit.MILLISECONDS.sleep(400);
                }
                WebElement element = presenceOfElement(locator);
                element.sendKeys(text);
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
    protected void click(By locator, boolean hasDelay) {
        for (var i = 1; i <= 10; i++) {
            try {
                if (hasDelay) {
                    TimeUnit.MILLISECONDS.sleep(400);
                }
                WebElement element = presenceOfElement(locator);
                element.click();
                return;
            } catch (StaleElementReferenceException exception) {
                System.out.println("Try to click, but get StaleElementReferenceException");
            } catch (ElementNotInteractableException exception) {
                System.out.println("Try to click, but get ElementNotInteractableException");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            catch (TimeoutException exception) {
//                System.out.println("Try to click, but get TimeoutException");
//            }
        }


    }

    /**
     * Метод для получения текста, содержащегося в веб-элементе
     */
    protected String getTextFromElement(By locator) {
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
                Select option = new Select(manager.driver.findElement(locator));
                if (hasDelay) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                option.selectByIndex(index);
                return;
            } catch (StaleElementReferenceException exception) {
                System.out.println("Try to pick the option, but get StaleElementReferenceException");

            }
        }
    }

    /**
     * Метод для выбора случайного элемента выпадающего списка типа select
     */
    protected void randomOptionPicker(By locator) {
        Select option = new Select(manager.driver.findElement(locator));
        List<WebElement> listOfOptions = option.getOptions();
        option.selectByIndex(getRandomInt(listOfOptions.size() - 1));
    }

    /**
     * Метод явного ожидания присутствия веб-элемента на странице
     */
    protected WebElement presenceOfElement(By locator) {
        WebElement element = new WebDriverWait(manager.driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(locator));
        return element;
    }

    /**
     * Метод явного ожидания кликабельности веб-элемента на странице
     */
    protected WebElement elementIsClickable(By locator) {
        WebElement element = new WebDriverWait(manager.driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
        return element;
    }

    /**
     * Метод возвращает случайное число от 0 до переданного значения
     */
    protected int getRandomInt(int ceil) {
        Random rand = new Random();
        return rand.nextInt(ceil);
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
                        TimeUnit.MILLISECONDS.sleep(400);
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
     * Метод перемещает курсор к документу
     */
    protected void moveToTheElement(WebElement element) {
        Actions action = new Actions(manager.driver);
        action.moveToElement(element).perform();
    }

    /**
     * Метод возвращает абсолютный путь к файлу по относительному пути
     */
    protected String getAbsolutePathToFile(String file) {
        String absolutePath = Paths.get(file).toAbsolutePath().toString();
        return absolutePath;
    }

    /**
     * Метод проверяет наличие элемента в DOM
     */
    protected boolean isElementPresent(By locator) {
        boolean result = manager.driver.findElements(locator).isEmpty();
        return !result;
    }

    /**
     * Метод проверяет список файлов в папке
     */
    protected File[] getListOfFiles(String directoryPath) {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }

    /**
     * Метод имитирует нажатие клавиш на клавиатуре
     */
    protected void keyBoardTypes(Keys keyType) {
        Actions action = new Actions(manager.driver);
        action.sendKeys(keyType).perform();
    }


    /**
     * Метод записывает номер заявки в указанный файл
     */
    protected void applicationNumbersWriter(String path) {
        String absPath = getAbsolutePathToFile(path);
        fileCreator(absPath);
        String appNumber = extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        try {
            FileWriter fw = new FileWriter(path, true);
            fw.write(appNumber + "\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Метод извлекает номер заявки
     */
    protected String extractAppNumber(By locator) {
        String rawElementContent = getTextFromElement(locator);
        String appNumber = rawElementContent.split(" ")[2];
        return appNumber;
    }

    /**
     * Метод проверяет наличие файла по указанному пути и создает его, если файл отсутствует
     */
    protected void fileCreator(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Метод удаляет все файлы из указанной директории
     */
    public void fileDeleter (String path) {
        File[] listOfFile = getListOfFiles(getAbsolutePathToFile(path));
        for (File file : listOfFile) {
            file.delete();
        }
    }

    /**
     * Метод номера заявок из указанного файла
     */
    protected List<String> applicationNumbersReader(String path) {
        try {
            List<String> numbers = Files.readAllLines(Paths.get(path).toAbsolutePath());
            return numbers;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Метод выбирает вид заявки
     */
    protected void selectSphereOfApplication(String typeSphere) {
        if ("invention".equals(typeSphere)) {
            click(By.xpath("//span[contains(text(), 'Изобретения')]"), true);
        } else if ("industrial".equals(typeSphere)) {
            click(By.xpath("//span[contains(text(), 'Промышленные')]"), true);
        } else if ("profile".equals(typeSphere)) {
            click(By.xpath("//span[contains(text(), 'Профиль')]"), true);
        }

    }

}





