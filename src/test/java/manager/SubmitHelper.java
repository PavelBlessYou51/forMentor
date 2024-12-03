package manager;

import model.EntityDataBase;
import model.PersonData;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.io.File;

/**
 * Класс-помощник. Содержит методы, связанные с подачей заявок
 */
public class SubmitHelper extends HelperBase {

    SubmitHelper(ApplicationManager manager) {
        super(manager);
    }

    /**
     * Метод подает заявки на изобретения (с приоритетом и без)
     */
    public String sendInventionApplication(String priorityType) throws InterruptedException {
        selectSphereOfApplication("invention");
        selectTypeOfApplication("inv_EuroApp");
        fillInventionCommonInfoPart(priorityType);
        addNewApplicant();
        addNewInventor();
        click(By.cssSelector("input[value='Далее']"), true);
        click(By.cssSelector("input[value='Далее']"), true);
        fillEditionalInfo(false);
        fillDocumentForm("invention");
        fillTaxFormInvention();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt");
        return sendingConfirmation;
    }

    /**
     * Метод подает заявку на изобретение с ходатайством
     */
    public String sendInventionApplicationWithPetition() throws InterruptedException {
        selectSphereOfApplication("invention");
        selectTypeOfApplication("inv_EuroApp");
        fillInventionCommonInfoPart("withoutPreority");
        addNewApplicant();
        addNewInventor();
        click(By.cssSelector("input[value='Далее']"), true);
        click(By.cssSelector("input[value='Далее']"), true);
        fillEditionalInfo(true);
        fillDocumentForm("invention");
        fillTaxFormInvention();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt");
        return sendingConfirmation;
    }

    /**
     * Метод подает заявку на ПО
     */
    public String sendIndustrialApplication(int countOfSamples) throws InterruptedException {
        selectSphereOfApplication("industrial");
        selectTypeOfApplication("ind_usualApp");
        fillIndustrialCommonInfoPart();
        addNewApplicant();
        addNewInventor();
        click(By.cssSelector("input[value='Далее']"), true);
        click(By.cssSelector("input[value='Далее']"), true);
        fillDocumentForm("industrial");
        fillIndustrialPrototypeWithEdditionalSamples(countOfSamples);
        fillTaxFormIndustrial();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        applicationNumbersWriter("src/test/resources/list_of_app/industrialAppNumbers.txt");
        return sendingConfirmation;
    }

    /**
     * Метод подает заявку на ПО c указанным приоритетом
     */
    public String sendIndustrialApplicationWithPriority(String priorityType) throws InterruptedException {
        selectSphereOfApplication("industrial");
        selectTypeOfApplication("ind_usualApp");
        fillIndustrialCommonInfoPart();
//        click(By.cssSelector("input[value='Далее']"), true);
        addNewApplicant();
        addNewInventor();
        click(By.cssSelector("input[value='Далее']"), true);
        click(By.cssSelector("input[value='Далее']"), true);
        fillDocumentForm("industrial");
        fillIndustrialPrototypeWithPriority(priorityType);
        fillTaxFormIndustrial();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        applicationNumbersWriter("src/test/resources/list_of_app/industrialAppNumbers.txt");
        return sendingConfirmation;
    }

    /**
     * Метод подает PCT заявку
     */
    public String sendInventionPCTApplication() throws InterruptedException {
        selectSphereOfApplication("invention");
        selectTypeOfApplication("PCT");
        fillPCTCommonInfoPart();
        addNewApplicant();
        addNewInventor();
        click(By.cssSelector("input[value='Далее']"), false);
        click(By.cssSelector("input[value='Далее']"), false);
        fillPCTEditionalInfo();
        fillPCTDocumentForm();
        fillTaxFormInvention();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt");
        return sendingConfirmation;
    }


    /**
     * Метод выбирает тип заявки
     */
    protected void selectTypeOfApplication(String typeApp) {
        if ("inv_EuroApp".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать евразийскую заявку']"), true);
        } else if ("ind_usualApp".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать заявку']"), true);
        } else if ("PCT".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать заявку EAPO-PCT']"), false);
        }

    }

    /**
     * Метод заполняет раздел №1 "Общая информация" заявки на изобретение
     */
    protected void fillInventionCommonInfoPart(String priorityType) {
        EntityDataBase entity = new EntityDataBase();
        String inventionName = entity.fakerRU.lorem().sentence();
        type(By.xpath("//textarea[contains(@id, 'invName')]"), inventionName, true);
        if (!"withoutPreority".equals(priorityType)) {
            click(By.xpath("//input[contains(@id, 'cbPrio')]"), true);
            if ("previousPCT".equals(priorityType)) {
                previousPCTApplication(entity.fakerRU.number().digits(6), entity.countryCode);
            } else if ("previousEuro".equals(priorityType)) {
                click(By.id("form:rbPrio:1"), true);
                previousEuroApplication(entity.fakerRU.number().digits(6));
            } else if ("additionalMaterials".equals(priorityType)) {
                click(By.id("form:rbPrio:2"), true);
                additionalMaterials(entity.fakerRU.number().digits(6));
            } else if ("startsOpenShowing".equals(priorityType)) {
                click(By.id("form:rbPrio:3"), true);
                startsOpenShowing(entity.fakerRU.number().digits(6));
            }
        }
        click(By.id("form:j_idt110:nextButtonId"), true);

    }

    /**
     * Метод добавляет нового заявителя и заполняет его данные
     */
    protected void addNewApplicant() throws InterruptedException {
        boolean hasHeader = true;
        while (hasHeader) {
            click(By.cssSelector("input[value='Добавить нового заявителя']"), true);
            fillPersonData("applicant");
            keyBoardTypes(Keys.RETURN);
            click(By.cssSelector("input[value='Далее']"), true);
            hasHeader = isElementPresent(By.xpath("//td[contains(text(), 'Заявители')]"));
            if (hasHeader) {
                deletePerson();
            }
        }
    }

    /**
     * Метод добавляет нового изобретателя\автора и заполняет его данные
     */
    protected void addNewInventor() throws InterruptedException {
        boolean HasHeader = true;
        while (HasHeader) {
            click(By.xpath("//input[contains(@value, 'Добавить нового')]"), true);
            fillPersonData("inventor");
            keyBoardTypes(Keys.RETURN);
            click(By.cssSelector("input[value='Далее']"), true);
            HasHeader = isElementPresent(By.xpath("//td[contains(text(), 'Документы')]"));
            if (HasHeader) {
                deletePerson();
            }
        }

    }

    /**
     * Метод заполняет раздел №7 в изобретениях и №6 в ПО "Документы"
     */
    protected void fillDocumentForm(String appType) {
        if ("invention".equals(appType)) {
            fileUpload(By.xpath("(//div[contains(@id, 'upload1')]//input)[1]"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/описание.pdf"), true);
            fileUpload(By.xpath("//div[contains(@id, 'upload2')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/формула.pdf"), true);
            fileUpload(By.xpath("//div[contains(@id, 'upload4')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/реферат.pdf"), true);
            fileUpload(By.xpath("//div[contains(@id, 'upload11')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/заявление.pdf"), true);
            uploadRandom3DFile();
        } else if ("industrial".equals(appType)) {
            fileUpload(By.xpath("//div[contains(@id, 'upload16')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/доверенность.pdf"), true);
            fileUpload(By.xpath("//div[contains(@id, 'upload18')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/письмо_заявителя.pdf"), true);
        }
        click(By.cssSelector("input[value='Далее']"), true);
    }



    /**
     * Метод заполняет раздел №8 "Расчет пошлин" заявки на изобретения
     */
    protected void fillTaxFormInvention() {
        chooseDiscount("1");
        click(By.xpath("//input[contains(@id, 'cbDuty001')]"), true);
        click(By.cssSelector("input[value='payment-document']"), true);
        fileUpload(By.className("rf-fu-inp"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/пп_об_оплате_гп.pdf"), true);

    }

    /**
     * Метод заполняет раздел №6 "Дополнительная информация"
     */
    protected void fillEditionalInfo(boolean withPetition) {
        if (withPetition) {
            click(By.xpath("(//div[@class='application-text']//input)[2]"), true);
        }
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод подписывает и подает заявки
     */
    public void signInApplication() {
        click(By.xpath("//input[@value='Подписать и подать заявку']"), true);
        click(By.xpath("//input[@value='Подписать и подать']"), true);
    }

    /**
     * Метод заполняет данные физического лица
     */
    protected void fillPersonData(String personType) {
        PersonData person = new PersonData();
        optionPicker(By.cssSelector("select[class='application-input']"), 1, true);
        type(By.xpath("//input[contains(@id, 'country')]"), person.countryCode, true);
        if ("applicant".equals(personType)) {
            type(By.xpath("//input[contains(@id, 'idTown')]"), person.postCode, true);
            type(By.xpath("//input[contains(@id, 'phone')]"), person.phoneNumber, true);
        }
        type(By.xpath("//input[contains(@id, 'appeal')]"), person.callTo, true);
        type(By.xpath("//textarea[contains(@id, 'address')]"), person.address, true);
        type(By.xpath("//textarea[contains(@id, 'name')]"), person.surname, true);
        type(By.xpath("//input[contains(@id, 'firstName')]"), person.name, true);
        type(By.xpath("//input[contains(@id, 'middleName')]"), person.patronymic, true);
        type(By.xpath("//input[contains(@id, 'email')]"), person.email, true);

    }

    /**
     * Метод удаляет добавленного заявителя\автора\изобретателя
     */
    protected void deletePerson() {
        click(By.xpath("//input[contains(@value, 'Удалить ')]"), true);
    }

    /**
     * Метод заполняет данные приоритета по предыдущей заявке при подаче заявки на изобретение
     */
    protected void previousPCTApplication(String prevApp, String countryCode) {
        type(By.xpath("(//input[contains(@name, 'priorityOptionsIntTable')][@class='application-input-required'])[1]"), prevApp, true);
        click(By.xpath("//input[contains(@name, 'calPrioritetIntDateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
        type(By.xpath("(//input[contains(@name, 'priorityOptionsIntTable')][@class='application-input-required'])[2]"), countryCode, true);
    }

    /**
     * Метод заполняет данные приоритета по предыдущей евразийской заявке при подаче заявки на изобретение
     */
    protected void previousEuroApplication(String prevApp) {
        click(By.xpath("//input[contains(@name, 'DateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
        type(By.xpath("//input[@class='application-input-required']"), prevApp, true);
    }

    /**
     * Метод заполняет данные приоритета по дате подачи доп материалов при подаче заявки на изобретение
     */
    protected void additionalMaterials(String prevApp) {
        click(By.xpath("//input[contains(@name, 'DateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
        type(By.xpath("//input[@class='application-input-required']"), prevApp, true);
    }

    /**
     * Метод заполняет данные приоритета по дате первого открытого показа при подаче заявки на изобретение
     */
    protected void startsOpenShowing(String prevApp) {
        click(By.xpath("//input[contains(@name, 'DateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
    }

    /**
     * Метод заполняет данные приоритета по предыдущей заявке при подаче заявки на изобретение
     */
    protected void chooseDiscount(String discountNum) {
        String locator = String.format("//div[@class='application-text']/input[position()=%s]", discountNum);
        WebElement discountElement = presenceOfElement(By.xpath(locator));
        discountElement.click();

    }

    /**
     * Метод заполняет раздел №1 "Общая информация" заявки на ПО
     */
    protected void fillIndustrialCommonInfoPart() {
        type(By.className("application-input"), "N" + getRandomInt(999), true);
        optionPicker(By.className("application-select-text"), getRandomInt(19), true);
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод заполняет раздел №7 "Промышленные образцы" заявки на ПО, добавляя образцы
     */
    protected void fillIndustrialPrototypeWithEdditionalSamples(int countOfSamples) {
        for (int l = 0; l < countOfSamples; l++) {
            if (l >= 1) {
                click(By.cssSelector("input[value='Добавить новый промышленный образец']"), true);
            }
            fillIndustrialPrototype(l);
        }
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод заполняет раздел №7 "Промышленные образцы" заявки на ПО c приоритетом
     */
    public void fillIndustrialPrototypeWithPriority(String priorityType) {
        EntityDataBase entity = new EntityDataBase();
        click(By.xpath("//input[contains(@id, 'cbPrio')]"), true);
        if ("previousPCT".equals(priorityType)) {
            previousPCTApplication(entity.fakerRU.number().digits(6), entity.countryCode);
        } else if ("previousEuro".equals(priorityType)) {
            optionPicker(By.xpath("//select[contains(@id, 'rbPrio')]"), 1, true);
            previousEuroApplication(entity.fakerRU.number().digits(6));
        } else if ("additionalMaterials".equals(priorityType)) {
            optionPicker(By.xpath("//select[contains(@id, 'rbPrio')]"), 2, true);
            additionalMaterials(entity.fakerRU.number().digits(6));
        } else if ("startsOpenShowing".equals(priorityType)) {
            optionPicker(By.xpath("//select[contains(@id, 'rbPrio')]"), 3, true);
            startsOpenShowing(entity.fakerRU.number().digits(6));
        }
        fillIndustrialPrototype(0);
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод заполняет раздел №7 "Промышленные образцы" заявки на ПО
     */
    protected void fillIndustrialPrototype(int locatorNumber) {
        EntityDataBase entity = new EntityDataBase();
        String prototypeName = entity.fakerRU.lorem().sentence();
        String productIndication = entity.fakerRU.lorem().sentence();
        type(By.xpath(String.format("//textarea[contains(@id, 'repeatDesign:%s:design:dgnNameInv')]", locatorNumber)), prototypeName, true);
        type(By.xpath(String.format("//textarea[contains(@id, 'repeatDesign:%s:design:dgnNameProduct')]", locatorNumber)), productIndication, true);
        randomOptionPicker(By.xpath(String.format("//select[contains(@id, 'repeatDesign:%s:design:selectLocSubCls')]", locatorNumber)));
        File[] listOfFile = getListOfFiles(getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/prototype_picture"));
        for (int i = 1; i <= 7; i++) {
            String selectLocator = String.format("(//select[contains(@id, 'repeatDesign:%s:design')][count(option)=8][not(contains(@id, 'selectLocSubCls'))])[%s]", locatorNumber, i);
            optionPicker(By.xpath(selectLocator), i, true);
        }
        for (int k = 1; k <= 7; k++) {
            String uploadLocator = String.format("(//div[contains(@id, 'upload%s')][contains(@id, 'repeatDesign:%s:design')]//input)[1]", k, locatorNumber);
            fileUpload(By.xpath(uploadLocator), listOfFile[k - 1].getAbsolutePath(), true);
        }
        fileUpload(By.xpath(String.format("//div[contains(@id, 'upload8')][contains(@id, 'repeatDesign:%s:design')]//input", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/3d_model.STEP"), true);
        fileUpload(By.xpath(String.format("//div[contains(@id, 'upload9')][contains(@id, 'repeatDesign:%s:design')]//input", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/чертеж_общего_вид_изд.jpg"), true);
        fileUpload(By.xpath(String.format("//div[contains(@id, 'upload10')][contains(@id, 'repeatDesign:%s:design')]//input", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/конфекционная_карта.pdf"), true);
        fileUpload(By.xpath(String.format("//div[contains(@id, 'upload11')][contains(@id, 'repeatDesign:%s:design')]//input", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/описание_пром_образца.pdf"), true);
    }

    /**
     * Метод заполняет раздел №8 "Расчет пошлин" заявки на ПО
     */
    protected void fillTaxFormIndustrial() {
        click(By.xpath("(//input[contains(@id, 'cbDuty')])[1]"), true);
        click(By.cssSelector("input[value='payment-document']"), true);
        fileUpload(By.xpath("//span[contains(@id, 'uploadGroup')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/пп_об_оплате_гп.pdf"), true);
    }

    /**
     * Метод заполняет раздел №1 "Общая информация" PCT заявки
     */
    protected void fillPCTCommonInfoPart() {
        EntityDataBase entity = new EntityDataBase();
        String inventionName = entity.fakerRU.lorem().sentence();
        type(By.xpath("//input[contains(@id, 'numInv')]"), "N" + getRandomInt(999), false);
        type(By.xpath("//textarea[contains(@id, 'nameInv')]"), inventionName, false);
        type(By.xpath("//input[contains(@id, 'pctNumber')]"), entity.countryCode + "2024/" + entity.fakerRU.number().digits(6), true);
        keyBoardTypes(Keys.RETURN);
        click(By.xpath("//input[contains(@id, 'PctDateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
        click(By.cssSelector("input[value='Далее']"), false);

    }

    /**
     * Метод заполняет раздел №6 "Дополнительная информация" PCT заявки
     */
    protected void fillPCTEditionalInfo() {
        click(By.xpath("//td[contains(text(), 'поданного')]/input[1]"), true);
        click(By.xpath("//td[contains(text(), 'поданной')]/input[1]"), true);
        click(By.xpath("//td[contains(text(), 'поданных')]/input[1]"), true);
        click(By.cssSelector("input[value='Далее']"), false);
    }

    /**
     * Метод заполняет раздел №7 "Документы" PCT заявки
     */
    protected void fillPCTDocumentForm() {
        fileUpload(By.xpath(String.format("(//div[contains(@id, 'upload1')]//input)[1]")), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_PCT/Описание изобретения.pdf"), true);
        fileUpload(By.xpath(String.format("(//div[contains(@id, 'upload3')]//input)[1]")), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_PCT/Формула изборетения.pdf"), true);
        fileUpload(By.xpath(String.format("(//div[contains(@id, 'upload6')]//input)[1]")), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_PCT/Чертежи.pdf"), true);
        fileUpload(By.xpath(String.format("(//div[contains(@id, 'upload19')]//input)[1]")), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_PCT/Верстак 3D-модель.STEP"), true);
        fileUpload(By.xpath(String.format("(//div[contains(@id, 'upload12')]//input)[1]")), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_PCT/Реферат.pdf"), true);

        click(By.cssSelector("input[value='Далее']"), false);

    }

    protected void uploadRandom3DFile() {
        File[] listOf3DFile = getListOfFiles(getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/3D_formats"));
        String pathTo3DFile = listOf3DFile[getRandomInt(listOf3DFile.length - 1)].getPath();
        fileUpload(By.xpath("//div[contains(@id, 'upload14')]//input"), getAbsolutePathToFile(pathTo3DFile), true);
    }

}
