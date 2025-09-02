package selenide_tests.manager;

import com.codeborne.selenide.Condition;
import exceptions.TooManyLoopsException;
import io.qameta.allure.Step;
import model.EntityDataBase;
import model.OrganisationData;
import model.PersonData;
import org.openqa.selenium.By;
import utils.FileUtils;

import java.io.File;
import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static utils.FileUtils.getAbsolutePathToFile;
import static utils.FileUtils.getListOfFiles;

/**
 * Класс содержит базовые методы для работы со страницей
 */
public class HelperBase {

    /**
     * Метод для получения текста, содержащегося в веб-элементе
     */
    public String getTextFromElement(By locator) {
        return $(locator).shouldBe(Condition.exist, Duration.ofSeconds(20)).getText();
    }

    /**
     * Метод выбирает раздел аккаунта
     */
    @Step("Выбор раздела аккаунта")
    public void selectSectionOfAccount(String typeSection) {
        if ("invention".equals(typeSection)) {
            $(By.xpath("//span[contains(text(), 'Изобретения')]")).click();
        } else if ("industrial".equals(typeSection)) {
            $(By.xpath("//span[contains(text(), 'Промышленные')]")).click();
        } else if ("profile".equals(typeSection)) {
            $(By.xpath("//span[contains(text(), 'Профиль')]")).click();
        }
    }

    /**
     * Метод выбирает функционал подачи заявления об изменении
     */
    @Step("Выбор 'Передача права / Изменение имени или наименования / Изменение адреса'")
    public void selectApplicationType(String appType) {
        if (appType.equals("change")) {
            $("input[value='Передача права / Изменение имени или наименования / Изменение адреса']").click();
        } else if (appType.equals("forward")) {
            $("input[value='Пересылка ЕА заявки из нацведомства']").click();
        }

    }

    /**
     * Метод выбирает раздел внутри ИЗО или ПО
     */
    @Step("Выбор секции в выбранном разделе")
    public void selectAction() {
        $(By.xpath("//a[contains(text(), 'Полученные досылки')]")).click();
    }

    /**
     * Метод нажимает кнопку "Далее" при заполнении формы заявления на изменения
     */
    public void pressNextButton() throws TooManyLoopsException {
        sleep(500);
        String currentHeader = getTextFromElement(By.xpath("//td[not(@style='display : none') and contains(@class, '-active')]/span[@class='rf-tab-lbl']"));
        String newHeader = getTextFromElement(By.xpath("//td[not(@style='display : none') and contains(@class, '-active')]/span[@class='rf-tab-lbl']"));
        int loopCount = 0;
        while (currentHeader.equals(newHeader)) {
            if (loopCount > 4) {
                throw new TooManyLoopsException("When press next button, loop count exceeds 3");
            }
            $("input[value='Далее']").shouldBe(Condition.visible, Condition.clickable, Condition.exist).click();;
            newHeader = getTextFromElement(By.xpath("//td[not(@style='display : none') and contains(@class, '-active')]/span[@class='rf-tab-lbl']"));
            loopCount++;
        }

    }

    /**
     * Метод нажимает кнопку "Далее" при заполнении формы заявления на изменения
     */
    @Step("Нажатие кнопки 'Продолжить'")
    public void pressContinueButton() {
        $("input[value='Продолжить']").shouldBe(Condition.visible, Condition.clickable, Condition.exist).click();
    }

    /**
     * Метод загружает файл на портал с проверкой
     */
    public void uploadFileWithCheck(String locator, String filePath) {
        String absolutePath = FileUtils.getAbsolutePathToFile(filePath);
        File file = new File(absolutePath);
        $(By.xpath(locator)).uploadFile(file);
        $(By.xpath(locator + "/ancestor::tr[position()=1]//input[@title='Cохранить файл на диск']")).shouldBe(Condition.exist, Duration.ofSeconds(15));
    }

    /**
     * Метод загружает случайный 3D файл
     */
    protected void uploadRandom3DFile(String locator) {
        File[] listOf3DFile = getListOfFiles(getAbsolutePathToFile("src/test/resources/file_to_upload/3D_models"));
        String pathTo3DFile = listOf3DFile[getRandomInt(listOf3DFile.length - 1)].getPath();
        uploadFileWithCheck(locator, pathTo3DFile);
    }

    /**
     * Метод загружает ПП об оплате гос пошлины
     */
    @Step("Загрузка ПП об оплате")
    public void uploadPaymentOrder() {
        $("input[value='payment-document']").click();
        uploadFileWithCheck("//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Пп_об_оплате_ГП%.pdf");
    }

    /**
     * Метод подает и подписывает документ
     */
    @Step("Отправка и подписание")
    public void signAndSendDocument() {
        $(By.xpath("//input[contains(@value, 'Подписать и подать ')]")).click();
        $("input[value='Подписать и подать']").click();
    }

    /**
     * Метод осуществляет поиск заявки или досылки по номеру
     */
    @Step("Поиск заявки по номеру")
    public void findAppByNumber(String appNumber) {
        $(By.xpath("//span[contains(text(), '№ Евразийской заявки')]/following-sibling::input")).shouldBe(Condition.exist).setValue(appNumber);
        $("input[value='Найти']").shouldBe(Condition.clickable).click();
    }

    /**
     * Метод открывает найденную заявку или досылку, кликая по номеру
     */
    @Step("Открытие заявки по номеру")
    public void openFoundAppByNumber(String appNumber) {
        String locator = String.format("//a[contains(text(), '%s')]", appNumber);
        $(By.xpath(locator)).shouldBe(Condition.visible).click();
    }


    /**
     * Метод добавляет нового заявителя\владельца в форме подачи заявления
     * ownerType: person, company, government физ. лицо\юр. лицо\гос. орг.
     */
    @Step("Добавление нового владельца/заявителя")
    public void addNewOwner(boolean isPerson) {
        $("input[value='Добавить нового заявителя']").click();
        EntityDataBase newOwner;
        if (isPerson) {
            newOwner = new PersonData();
            $(By.xpath("//input[contains(@id, 'firstName')]")).setValue(newOwner.name);
            $(By.xpath("//textarea[contains(@id, 'name')]")).setValue(newOwner.surname);
            $(By.xpath("//input[contains(@id, 'middleName')]")).setValue(newOwner.patronymic);
        } else {
            newOwner = new OrganisationData();
            $(By.xpath("//div[not(@class)]/select")).selectOptionByValue("juridical-person");
            $(By.xpath("//textarea[contains(@id, 'name')]")).setValue(((OrganisationData) newOwner).organisationName);
        }
        $(By.xpath("//input[contains(@id, 'email')]")).setValue(newOwner.email);
        $(By.xpath("//input[contains(@id, 'country')]")).setValue(newOwner.countryCode);
        $(By.xpath("//input[contains(@id, 'phone')]")).setValue(newOwner.phoneNumber);
        $(By.xpath("//input[contains(@id, 'idTown')]")).setValue(newOwner.postCode);
        $(By.xpath("//textarea[contains(@id, 'address')]")).setValue(newOwner.address);
    }

    /**
     * Метод добавляет нового автора\изобретателя в форме подачи заявления
     */
    @Step("Добавление нового автора/изобретателя")
    public void addNewInventor() {
        $(By.xpath("//input[contains(@value, 'Добавить нового')]")).click();
        PersonData person = new PersonData();
        $(By.xpath("//input[contains(@id, 'firstName')]")).setValue(person.name);
        $(By.xpath("//textarea[contains(@id, 'name')]")).setValue(person.surname);
        $(By.xpath("//input[contains(@id, 'middleName')]")).setValue(person.patronymic);
        $(By.xpath("//input[contains(@id, 'email')]")).setValue(person.email);
        $(By.xpath("//input[contains(@id, 'country')]")).setValue(person.countryCode);
        $(By.xpath("//input[contains(@id, 'phone')]")).setValue(person.phoneNumber);
        $(By.xpath("//textarea[contains(@id, 'address')]")).setValue(person.address);
    }

    /**
     * Метод выбирает тип представителя: ПП или физ лицо
     */
    public void selectRepresentativeType(boolean isAgent) {
        if (isAgent) {
            $("input[value='attorney']").click();
        } else {
            $("input[value='attorney']").click();
        }
    }

    /**
     * Метод находит и выбирает ПП по регистрационному номеру
     */
    public void selectPatentAgentByNumber(int regNumber) {
        sleep(1000); // если убрать ПП находится через поиск не будут
        $(By.xpath("//input[contains(@id, 'searchAgentText')]")).shouldBe(Condition.enabled, Condition.exist).setValue(String.valueOf(regNumber));
        $("input[value='Искать']").click();
        $(By.xpath("//input[contains(@value, 'Добавить представителя')]")).click();
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
     * Метод получает подтверждение отправки заявления на фронте
     */
    @Step("Получение подтверждения отправки")
    public String getConfirmMessage() {
        return getTextFromElement(By.cssSelector("span[class='error-message']"));
    }

    /**
     * Метод скачивает файл по локатору
     */
    @Step("Скачивание файла")
    public void downloadFile() {
        File application = $(By.xpath("//a[contains(text(), 'Заявление')]")).download(withExtension("pdf"));
    }

    /**
     * Метод получает номер заявки из сообщения об отправке
     */
    @Step("Получение номера заявки")
    public String getAppNumber() {
        String message = getTextFromElement(By.xpath("//span[contains(text(), 'Номер заявки')]"));
        return message.split(": ")[1];
    }
}


