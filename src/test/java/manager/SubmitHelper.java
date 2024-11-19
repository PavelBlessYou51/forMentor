package manager;

import model.EntityDataBase;
import model.PersonData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;

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
        click(By.cssSelector("input[value='Далее']"));
        click(By.cssSelector("input[value='Далее']"));
        fillEditionalInfo(false);
        fillDocumentForm("invention");
        fillTaxFormInvention();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        return sendingConfirmation;
    }

    /**
     * Метод подает заявку на изобретение с ходатайством
     */
    public String sendApplicationWithPetition() throws InterruptedException {
        selectSphereOfApplication("invention");
        selectTypeOfApplication("inv_EuroApp");
        fillInventionCommonInfoPart("withoutPreority");
        addNewApplicant();
        addNewInventor();
        click(By.cssSelector("input[value='Далее']"));
        click(By.cssSelector("input[value='Далее']"));
        fillEditionalInfo(true);
        fillDocumentForm("invention");
        fillTaxFormInvention();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        return sendingConfirmation;
    }

    /**
     * Метод подает заявку на ПО
     */
    public String sendIndustrialApplication() throws InterruptedException {
        selectSphereOfApplication("industrial");
        selectTypeOfApplication("ind_usualApp");
        fillIndustrialCommonInfoPart();
        click(By.cssSelector("input[value='Далее']"));
        addNewApplicant();
        addNewInventor();
        click(By.cssSelector("input[value='Далее']"));
        click(By.cssSelector("input[value='Далее']"));
        fillDocumentForm("industrial");
        fillIndustrialPrototype();
        fillTaxFormIndustrial();
        signInApplication();
        String sendingConfirmation = getTextFromElement(By.cssSelector("span[class='error-message']"));
        return sendingConfirmation;
    }

    /**
     * Метод выбирает вид заявки
     */
    protected void selectSphereOfApplication(String typeSphere) {
        if ("invention".equals(typeSphere)) {
            click(By.xpath("//span[contains(text(), 'Изобретения')]"));
        } else if ("industrial".equals(typeSphere)) {
            click(By.xpath("//span[contains(text(), 'Промышленные')]"));
        }

    }

    /**
     * Метод выбирает тип заявки
     */
    protected void selectTypeOfApplication(String typeApp) {
        if ("inv_EuroApp".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать евразийскую заявку']"));
        } else if ("ind_usualApp".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать заявку']"));
        }

    }

    /**
     * Метод заполняет раздел №1 "Общая информация" заявки на изобретение
     */
    protected void fillInventionCommonInfoPart(String priorityType) {
        EntityDataBase entity = new EntityDataBase();
        String inventionName = entity.fakerRU.lorem().sentence();
        type(By.id("form:invName"), inventionName);
        if (!"withoutPreority".equals(priorityType)) {
            click(By.id("form:cbPrio"));
            if ("previousPCT".equals(priorityType)) {
                previousPCTApplication(entity.fakerRU.number().digits(6), entity.countryCode);
            } else if ("previousEuro".equals(priorityType)) {
                previousEuroApplication(entity.fakerRU.number().digits(6));
            } else if ("additionalMaterials".equals(priorityType)) {
                additionalMaterials(entity.fakerRU.number().digits(6));
            } else if ("startsOpenShowing".equals(priorityType)) {
                startsOpenShowing(entity.fakerRU.number().digits(6));
            }
        }
        click(By.id("form:j_idt110:nextButtonId"));

    }

    /**
     * Метод добавляет нового заявителя и заполняет его данные
     */
    protected void addNewApplicant() throws InterruptedException {
        boolean hasHeader = true;
        while (hasHeader) {
            click(By.cssSelector("input[value='Добавить нового заявителя']"));
            fillPersonData("applicant");
            click(By.cssSelector("input[value='Далее']"));
            click(By.cssSelector("input[value='Далее']"));
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
            click(By.xpath("//input[contains(@value, 'Добавить нового')]"));
            fillPersonData("inventor");
            click(By.cssSelector("input[value='Далее']"));
            click(By.cssSelector("input[value='Далее']"));
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
            fileUpload(By.cssSelector("span[id='form:j_idt811:j_idt813:uploadGroup'] input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/oписание.pdf"));
            fileUpload(By.cssSelector("span[id='form:j_idt811:j_idt835:uploadGroup'] input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/формула.pdf"));
            fileUpload(By.cssSelector("span[id='form:j_idt811:j_idt901:uploadGroup'] input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/реферат.pdf"));
            fileUpload(By.cssSelector("span[id='form:j_idt811:j_idt1062:uploadGroup'] input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/заявление.pdf"));
        } else if ("industrial".equals(appType)) {
            fileUpload(By.xpath("//div[contains(@id, 'upload16')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/доверенность.pdf"));
            fileUpload(By.xpath("//div[contains(@id, 'upload18')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/письмо_заявителя.pdf"));
        }
        click(By.cssSelector("input[value='Далее']"));
    }

    /**
     * Метод заполняет раздел №8 "Расчет пошлин" заявки на изобретения
     */
    protected void fillTaxFormInvention() {
        chooseDiscount("1");
        click(By.id("form:j_idt1194:j_idt1227:j_idt1229:cbDuty001"));
        click(By.id("form:j_idt1194:j_idt1512:j_idt1517:1"));
        fileUpload(By.className("rf-fu-inp"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invention/пп_об_оплате_гп.pdf"));

    }

    /**
     * Метод заполняет раздел №6 "Дополнительная информация"
     */
    protected void fillEditionalInfo(boolean withPetition) {
        if (withPetition) {
            click(By.xpath("(//div[@class='application-text']//input)[2]"));
        }
        click(By.cssSelector("input[value='Далее']"));
    }

    /**
     * Метод подписывает и подает заявки
     */
    public void signInApplication() {
        click(By.xpath("//input[@value='Подписать и подать заявку']"));
        click(By.xpath("//input[@value='Подписать и подать']"));
    }

    /**
     * Метод заполняет данные физического лица
     */
    protected void fillPersonData(String personType) {
        PersonData person = new PersonData();
        optionPicker(By.cssSelector("select[class='application-input']"), 1);
        if ("applicant".equals(personType)) {
            type(By.xpath("//input[contains(@id, 'idTown')]"), person.postCode);
            type(By.xpath("//input[contains(@id, 'phone')]"), person.phoneNumber);
        }
        type(By.xpath("//input[contains(@id, 'appeal')]"), person.callTo);
        type(By.xpath("//input[contains(@id, 'country')]"), person.countryCode);
        type(By.xpath("//textarea[contains(@id, 'address')]"), person.address);
        type(By.xpath("//textarea[contains(@id, 'name')]"), person.surname);
        type(By.xpath("//input[contains(@id, 'firstName')]"), person.name);
        type(By.xpath("//input[contains(@id, 'middleName')]"), person.patronymic);
        type(By.xpath("//input[contains(@id, 'email')]"), person.email);
    }

    /**
     * Метод удаляет добавленного заявителя\автора\изобретателя
     */
    protected void deletePerson() {
        click(By.xpath("//input[contains(@value, 'Удалить ')]"));
    }

    /**
     * Метод заполняет данные приоритета по предыдущей заявке при подаче заявки на изобретение
     */
    protected void previousPCTApplication(String prevApp, String countryCode) {
        type(By.name("form:priorityOptionsIntTable:0:j_idt60"), prevApp);
        click(By.id("form:priorityOptionsIntTable:0:calPrioritetIntDateInputDate"));
        click(By.xpath("//div[contains(text(), 'Today')]"));
        type(By.name("form:priorityOptionsIntTable:0:j_idt66"), countryCode);
    }

    /**
     * Метод заполняет данные приоритета по предыдущей евразийской заявке при подаче заявки на изобретение
     */
    protected void previousEuroApplication(String prevApp) {
        click(By.id("form:rbPrio:1"));
        click(By.id("form:j_idt79:0:calPrioritetEurDateInputDate"));
        click(By.xpath("//div[contains(text(), 'Today')]"));
        type(By.name("form:j_idt79:0:j_idt85"), prevApp);
    }

    /**
     * Метод заполняет данные приоритета по дате подачи доп материалов при подаче заявки на изобретение
     */
    protected void additionalMaterials(String prevApp) {
        click(By.id("form:rbPrio:2"));
        click(By.id("form:j_idt90:0:calPrioritetAddDateInputDate"));
        click(By.xpath("//div[contains(text(), 'Today')]"));
        type(By.name("form:j_idt90:0:j_idt96"), prevApp);
    }

    /**
     * Метод заполняет данные приоритета по дате первого открытого показа при подаче заявки на изобретение
     */
    protected void startsOpenShowing(String prevApp) {
        click(By.id("form:rbPrio:3"));
        click(By.id("form:j_idt101:0:calPrioritetAddDateInputDate"));
        click(By.xpath("//div[contains(text(), 'Today')]"));
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
        type(By.className("application-input"), "N" + getRandomInt(999));
        optionPicker(By.className("application-select-text"), getRandomInt(19));
    }

    /**
     * Метод заполняет раздел №7 "Промышленные образцы" заявки на ПО
     */
    protected void fillIndustrialPrototype() {
        EntityDataBase entity = new EntityDataBase();
        String prototypeName = entity.fakerRU.lorem().sentence();
        String productIndication = entity.fakerRU.lorem().sentence();
        type(By.xpath("//textarea[contains(@id, 'dgnNameInv')]"), prototypeName);
        type(By.xpath("//textarea[contains(@id, 'dgnNameProduct')]"), productIndication);
        randomOptionPicker(By.xpath("//select[contains(@id, 'selectLocSubCls')]"));
        File[] listOfFile = getListOfFiles(getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/prototype_picture"));
        for (int i = 1; i <= 7; i++) {
            String selectLocator = String.format("(//select[count(option)=8])[%s]", i);
            optionPicker(By.xpath(selectLocator), i);
        }
        for (int i = 1; i <= 7; i++) {
            String uploadLocator = String.format("(//div[contains(@id, 'upload%s')]//input)[1]", i);
            fileUpload(By.xpath(uploadLocator), listOfFile[i - 1].getAbsolutePath());
        }
        fileUpload(By.xpath("//div[contains(@id, 'upload8')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/3d_model.STEP"));
        fileUpload(By.xpath("//div[contains(@id, 'upload9')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/чертеж_общего_вид_изд.jpg"));
        fileUpload(By.xpath("//div[contains(@id, 'upload10')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/конфекционная_карта.pdf"));
        fileUpload(By.xpath("//div[contains(@id, 'upload11')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_industrial/описание_пром_образца.pdf"));
        click(By.cssSelector("input[value='Далее']"));
    }

    /**
     * Метод заполняет раздел №8 "Расчет пошлин" заявки на ПО
     */
    protected void fillTaxFormIndustrial() {
        click(By.xpath("(//input[contains(@id, 'cbDuty')])[1]"));
        click(By.cssSelector("input[value='payment-document']"));
        fileUpload(By.xpath("//span[contains(@id, 'uploadGroup')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/docs_for_invetion/пп_об_оплате_гп.pdf"));
    }


}


