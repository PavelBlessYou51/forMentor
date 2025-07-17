package selenium_tests.manager;

import exceptions.NextButtomException;
import model.EntityDataBase;
import model.PersonData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

import static utils.FileUtils.getAbsolutePathToFile;
import static utils.FileUtils.getListOfFiles;

/**
 * Класс-помощник. Содержит методы, связанные с подачей заявок
 */
public class SendingHelper extends HelperBase {

    SendingHelper(ApplicationManager manager) {
        super(manager);
    }



    /**
     * Метод выбирает тип заявки\дополнений\заявлений
     */
    public void selectTypeOfApplication(String typeApp) {
        if ("euroApp".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать евразийскую заявку']"), true);
        } else if ("allocatedApp".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать выделенную евразийскую заявку']"), true);
        } else if ("PCT".equals(typeApp)) {
            click(By.cssSelector("input[value='Переход заявки PCT на региональную фазу ']"), false);
        } else if ("addition".equals(typeApp)) {
            click(By.cssSelector("input[value^='Направить ответ']"), false);
        } else if ("changedApp".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать измененное заявление']"), false);
        } else if ("additionWithDate".equals(typeApp)) {
            click(By.cssSelector("input[value^='Направить доп. материалы с указанием даты']"), false);
        } else if ("additionIndWithDate".equals(typeApp)) {
            click(By.cssSelector("input[value='Подать досылку с указанием даты']"), false);
        }


    }

    /**
     * Метод заполняет раздел №1 "Общая информация" заявки на изобретение
     */
    public void fillInventionCommonInfoPart() {
        EntityDataBase entity = new EntityDataBase();
        String inventionName = entity.fakerRU.lorem().sentence();
        type(By.xpath("//textarea[contains(@id, 'invName') or contains(@id, 'nameInv')]"), inventionName, true);
    }

    /**
     * Метод добавляет приоритет в разделе №1 "Общая информация" заявки на изобретение
     */
    public void addInventionPriority(String priorityType) {
        EntityDataBase entity = new EntityDataBase();
        boolean flag = presenceOfElement(By.cssSelector("input[value='previous-international-application']")).isEnabled();
        if (!flag) {
            click(By.xpath("//input[contains(@id, 'cbPrio')]"), true);
        }
        if ("previousPCT".equals(priorityType)) {
            click(By.cssSelector("input[value='previous-international-application']"), true);
            previousPCTApplication(entity.fakerRU.number().digits(6), entity.countryCode);
        } else if ("previousEuro".equals(priorityType)) {
            click(By.cssSelector("input[value='previous-eurasian-application']"), true);
            previousEuroApplication(entity.fakerRU.number().digits(6));
        } else if ("additionalMaterials".equals(priorityType)) {
            click(By.cssSelector("input[value='additional-materials-reception']"), true);
            additionalMaterials(entity.fakerRU.number().digits(6));
        } else if ("startsOpenShowing".equals(priorityType)) {
            click(By.cssSelector("input[value='exhibition-show']"), true);
            startsOpenShowing("invention");
        }
    }


    /**
     * Метод заполняет промышленные образцы в досылке по ПО
     */
    public void fillIndustrialPrototypeInAddition(boolean allDocs) {
        List<WebElement> listOfIndustrialSamples = manager.driver.findElements(By.cssSelector("input[title='Развернуть']"));
        int countOfSamples = listOfIndustrialSamples.size();
        for (int i = 1; i <= countOfSamples; i++) {
            String locator = String.format("(//input[@title='Развернуть'])[%s]", i);
            click(By.xpath(locator), true);
            if (allDocs) {
                addAllDocuments(i - 1);
            } else {
                addNecessaryDocuments(i - 1);
            }
        }
        click(By.cssSelector("input[value='Далее']"), true);


    }

    /**
     * Метод добавляет нового заявителя и заполняет его данные
     */
    public void addNewApplicants(int count) throws NextButtomException {
        boolean hasHeader = true;
        int havePersons = 0;
        while (hasHeader) {
            while (havePersons < count) {
                click(By.cssSelector("input[value='Добавить нового заявителя']"), true);
                while (havePersons == presenceOfElements(By.xpath("//input[contains(@value, 'Удалить')]")).size()) {
                    click(By.cssSelector("input[value='Добавить нового заявителя']"), true);
                }
                if (isElementPresent(By.xpath("//span[@class='error-message']"))) {
                    deletePerson(havePersons--);
                    continue;
                }
                fillPersonData("applicant", havePersons);
                havePersons++;
            }
            int loopCount = 0;
            while (!isElementPresent(By.xpath("//input[@value='Добавить нового изобретателя' or @value='Добавить нового автора']"))) {
                if (loopCount > 4) {
                    throw new NextButtomException("Loop of next button!");
                }
                click(By.cssSelector("input[value='Далее']"), true);
                loopCount++;
            }
            hasHeader = isElementPresent(By.xpath("//td[contains(text(), 'Заявители')]"));
            if (hasHeader) {
                deletePerson(count);
                havePersons--;
            }
        }
    }

    /**
     * Метод добавляет нового изобретателя\автора и заполняет его данные
     */
    public void addNewInventors(int count) throws NextButtomException {
        boolean HasHeader = true;
        int havePersons = 0;
        while (HasHeader) {
            while (havePersons < count) {
                click(By.xpath("//input[contains(@value, 'Добавить нового')]"), true);
                while (havePersons == presenceOfElements(By.xpath("//input[contains(@value, 'Удалить')]")).size()) {
                    click(By.xpath("//input[contains(@value, 'Добавить нового')]"), true);
                }
                if (isElementPresent(By.xpath("//span[@class='error-message']"))) {
                    deletePerson(havePersons--);
                    continue;
                }
                fillPersonData("inventor", havePersons);
                havePersons++;
            }
            int loopCount = 0;
            while (!isElementPresent(By.xpath("//input[@value='Добавить нового представителя']"))) {
                if (loopCount > 4) {
                    throw new NextButtomException("Loop of next button!");
                }
                click(By.cssSelector("input[value='Далее']"), true);
                loopCount++;
            }
            HasHeader = isElementPresent(By.xpath("//td[contains(text(), 'Изобретатели') or contains(text(), 'Авторы')]"));
            if (HasHeader) {
                deletePerson(count);
                havePersons--;
            }
        }

    }

    /**
     * Метод добавляет нового представителя и заполняет его данные
     */
    public void addNewRepresentative() {
        boolean HasHeader = true;
        while (HasHeader) {
            click(By.xpath("//input[contains(@value, 'Добавить нового')]"), true);
            fillPersonData("representative", 1);
            click(By.cssSelector("input[value='Далее']"), true);
            HasHeader = isElementPresent(By.xpath("//td[contains(text(), 'Представители')]"));
            if (HasHeader) {
                deletePerson(2);
            }
        }

    }

    /**
     * Метод заполняет раздел №7 "Документы" в изобретениях и №6 в ПО
     */
    public void fillAppDocumentForm(String appType) throws NextButtomException {
        if ("invention".equals(appType)) {
            fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            uploadRandom3DFile("//td[contains(text(), 'Изображение в формате 3D(obj, step, stl, stp, u3d)')]/..//input[@type='file']");
            click(By.xpath("//input[@title='Добавить документ']"), true);
            randomOptionPicker(By.xpath("//select"));
            fileUploadWithCheck("//div[contains(@id, 'repeatOtherDocId')]//input[@type='file']", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Перевод_док_о_правопреемстве.pdf"));
        } else if ("industrial".equals(appType)) {
            click(By.xpath("//input[@title='Добавить документ']"), true);
            optionPicker(By.xpath("//select"), 1, true);
            fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[3]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/Другое%.pdf"));
            fileUpload(By.xpath("(//div[contains(@id, 'upload')]//input[@type='file'])[1]"), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/Доверенность%.pdf"), true);
            fileUpload(By.xpath("(//div[contains(@id, 'upload')]//input[@type='file'])[2]"), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/Письмо Заявителя%.pdf"), true);
        }
        int count = 0;
        while (isElementPresent(By.xpath("//td[contains(text(), 'Документы')]"))) {
            if (count > 4) {
                throw new NextButtomException("Loop of next button!");
            }
            click(By.cssSelector("input[value='Далее']"), true);
            count++;
        }
    }


    /**
     * Метод заполняет раздел №7 "Документы" в изобретениях для тестов сохранения в Madras
     */
    public void fillAppDocumentFormForMadras(boolean hasPriority) {
        int fieldNumberModifier = 0;
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание(не_сжим_17_Мб%).pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[2]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Формула_(цветной_файл)%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[3]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Чертежи_(%).pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[5]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Реферат_(много_шрифтов)%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[6]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Список_последовательностей%.txt"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[7]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Документ_о_депонировании%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[8]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Основания_уменьшения_пошлины%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[9]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Доверенность%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[10]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Передача_права_на_евр_заявку%.pdf"));
        if (hasPriority) {
            fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[11]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Права_на_приоритета%.pdf"));
            fieldNumberModifier++;
        }
        fileUploadWithCheck(String.format("(//div[contains(@id, 'upload')]//input[@type='file'])[%s]", fieldNumberModifier + 11), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Письмо_заявителя%.pdf"));
        uploadRandom3DFile("//td[contains(text(), 'Изображение в формате 3D(obj, step, stl, stp, u3d)')]/..//input[@type='file']");
        click(By.xpath("//input[@title='Добавить документ']"), true);
        randomOptionPicker(By.xpath("//select"));
        fileUploadWithCheck("//span[contains(@id, 'uploadOtherDocId')]//input[@type='file']", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Перевод_док_о_правопреемстве.pdf"));
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод заполняет раздел №1 "Документы" при подаче досылки по ИЗО для тестов сохранения в Madras
     */
    public void fillAdditionDocFormForMadras() {
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание(не_сжим_17_Мб%).pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[2]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Формула_(цветной_файл)%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[3]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Чертежи_(%).pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[5]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Реферат_(много_шрифтов)%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[6]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Список последовательностей%.xml"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[7]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Доверенность%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[8]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Передача_права_на_евр_заявку%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[9]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Ответ_заявителя_на_запрос_экспертизы%.pdf"));
        uploadRandom3DFile("//td[contains(text(), 'Изображение в формате 3D(obj, step, stl, stp, u3d)')]/..//input[@type='file']");
        click(By.xpath("//input[@title='Добавить документ']"), true);
        randomOptionPicker(By.xpath("//select"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[10]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Письмо_заявителя%.pdf"));
        click(By.cssSelector("input[value='Далее']"), true);
    }


    /**
     * Метод заполняет раздел №7 "Документы" в изобретениях и ПО
     */
    public void fillAdditionDocumentForm() {
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание(не_сжим_17_Мб%).pdf"));
        uploadRandom3DFile("//td[contains(text(), 'Изображение в формате 3D(obj, step, stl, stp, u3d)')]/..//input[@type='file']");
        click(By.xpath("//input[@title='Добавить документ']"), true);
        randomOptionPicker(By.xpath("//select"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[10]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Письмо_заявителя%.pdf"));
        click(By.cssSelector("input[value='Далее']"), true);
    }


    /**
     * Метод заполняет раздел №8 "Расчет пошлин" заявки на изобретения
     */
    public void fillTaxFormInvention() {
        chooseDiscount("1");
        click(By.xpath("//input[contains(@id, 'cbDuty001')]"), true);
        click(By.cssSelector("input[value='payment-document']"), true);
        fileUpload(By.xpath("//input[@class='rf-fu-inp']"), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Пп_об_оплате_ГП%.pdf"), true);
    }

    /**
     * Метод заполняет раздел №6 "Дополнительная информация"
     */
    public void fillAdditionalInfo(String petitionType) {
        if ("replaceDiscription".equals(petitionType)) {
            replaceDiscription();
        } else if ("substantiveExam".equals(petitionType)) {
            click(By.xpath("//input[contains(@id, 'examination')]"), true);
        } else if ("earlyPub".equals(petitionType)) {
            earlyPublication();
        } else if ("allPetitions".equals(petitionType)) {
            replaceDiscription();
            click(By.xpath("//input[contains(@id, 'examination')]"), true);
            earlyPublication();
        }
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод выбирает чекбокс "Прошу (просим) для целей установления даты подачи этой заявки заменить описание изобретения или его часть ссылкой на ранее поданную заявителем заявку"
     * в разделе №6 "Дополнительная информация" евразийской заявки
     */
    protected void replaceDiscription() {
        click(By.xpath("//input[contains(@id, 'replaceDescription')]"), true);
        type(By.xpath("(//table[contains(@id, 'replaceDescriptionPanel')]//input[@class='application-input-required'])[1]"), getRandomAppNumber(), true);
        click(By.xpath("//input[contains(@id, 'calPrioritetIntDateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
        type(By.xpath("(//table[contains(@id, 'replaceDescriptionPanel')]//input[@class='application-input-required'])[2]"), "RU", true);
    }

    /**
     * Метод выбирает чекбокс "Об ускоренной публикации сведений о евразийской заявке (ст. 15(4) Конвенции)"
     * в разделе №6 "Дополнительная информация" евразийской заявки
     */
    protected void earlyPublication() {
        click(By.xpath("//input[contains(@id, 'earlyPublicationCheckBoxId')]"), true);
        type(By.xpath("//table[contains(@id, 'earlyPublicationId')]//input[@class='application-input-required']"), String.valueOf(getRandomInt(6)), true);
    }

    /**
     * Метод подписывает и подает заявки
     */
    public void signInApplication() {
        click(By.xpath("//input[contains(@value, 'Подписать и подать ')]"), true);
        click(By.xpath("//input[@value='Подписать и подать']"), true);
        waitingFor("//span[@class='error-message']", 45);
    }

    /**
     * Метод заполняет данные физического лица(Заявитель, Изобретатель, Представитель)
     */
    public void fillPersonData(String personType, int count) {
        PersonData person = new PersonData();
        optionPicker(By.xpath(String.format("(//select[@class='application-input'])[%s]", count + 1)), 1, true);
        type(By.xpath(String.format("//input[contains(@id, '%s:appeal')]", count)), person.callTo, true);
        type(By.xpath(String.format("//textarea[contains(@id, '%s:name')]", count)), person.surname, true);
        type(By.xpath(String.format("//input[contains(@id, '%s:firstName')]", count)), person.name, true);
        type(By.xpath(String.format("//input[contains(@id, '%s:middleName')]", count)), person.patronymic, true);
        type(By.xpath(String.format("//input[contains(@id, '%s:email')]", count)), person.email, true);
        type(By.xpath(String.format("//input[contains(@id, '%s:country')]", count)), person.countryCode, true);
        if ("applicant".equals(personType) || "representative".equals(personType)) {
            type(By.xpath(String.format("//input[contains(@id, '%s:idTown')]", count)), person.postCode, true);
            type(By.xpath(String.format("//input[contains(@id, '%s:phone')]", count)), person.phoneNumber, true);
        }
        type(By.xpath(String.format("//textarea[contains(@id, '%s:address')]", count)), person.address, true);
    }

    /**
     * Метод удаляет добавленного заявителя\автора\изобретателя
     */
    protected void deletePerson(int personNumber) {
        String locator = String.format("(//table[@id='tablePersonId'])[%s]//input[contains(@value, 'Удалить ')]", personNumber);
        click(By.xpath(locator), true);
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
     * Метод заполняет данные приоритета по предыдущей евразийской заявке
     */
    protected void previousEuroApplication(String prevApp) {
        click(By.xpath("//input[contains(@name, 'calPrioritetEurDateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
        type(By.xpath("//span[contains(@id, 'priorityOptionsEur')]//input[@class='application-input-required']"), prevApp, true);
    }

    /**
     * Метод заполняет данные приоритета по дате подачи доп материалов
     */
    protected void additionalMaterials(String prevApp) {
        click(By.xpath("//input[contains(@name, 'calPrioritetAddDateInputDate')]"), true);
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
        type(By.xpath("//span[contains(@id, 'priorityOptionsAdd')]//input[@class='application-input-required']"), prevApp, true);
    }

    /**
     * Метод заполняет данные приоритета по дате первого открытого показа
     */
    protected void startsOpenShowing(String appType) {
        if (appType.equals("invention")) {
            click(By.xpath("(//input[contains(@name, 'calPrioritetAddDateInputDate')])[2]"), true);
        } else {
            click(By.xpath("//input[contains(@name, 'calPrioritetExhDateInputDate')]"), true);
        }
        click(By.xpath("//div[contains(text(), 'Today')]"), true);
    }

    /**
     * Метод заполняет данные приоритета по предыдущей заявке
     */
    protected void chooseDiscount(String discountNum) {
        String locator = String.format("//div[@class='application-text']/input[position()=%s]", discountNum);
        WebElement discountElement = presenceOfElement(By.xpath(locator));
        discountElement.click();

    }

    /**
     * Метод заполняет раздел №1 "Общая информация" заявки на ПО
     */
    public void fillIndustrialCommonInfoPart() {
        type(By.className("application-input"), "N" + getRandomInt(999), true);
        optionPicker(By.className("application-select-text"), getRandomInt(32), true);
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод заполняет раздел №7 "Промышленные образцы" заявки на ПО, добавляя образцы
     */
    public void fillIndustrialPrototypeWithAdditionalSamples(int countOfSamples, boolean allDocs) {
        for (int l = 0; l < countOfSamples; l++) {
            if (l != 0) {
                click(By.cssSelector("input[value='Добавить новый промышленный образец']"), true);
            }
            fillIndustrialPrototype(l, allDocs);
        }
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод заполняет раздел №7 "Промышленные образцы" заявки на ПО c 4 образцами и разными приоритетами
     */
    public void fillIndustrialPrototypeWithAllPriorities(int countSamples) {
        EntityDataBase entity = new EntityDataBase();
        for (int i = 0; i < countSamples; i++) {
            if (i != 0) {
                click(By.cssSelector("input[value='Добавить новый промышленный образец']"), true);
            }
            click(By.xpath(String.format("(//input[contains(@id, 'cbPrio')])[%s]", i + 1)), true);
            String prioritiesListLocator = String.format("(//select[contains(@id, 'rbPrio')])[%s]", i + 1);
            if (i == 0) {
                previousPCTApplication(entity.fakerRU.number().digits(6), entity.countryCode);
            } else if (i == 1) {
                optionPicker(By.xpath(prioritiesListLocator), 1, true);
                additionalMaterials(entity.fakerRU.number().digits(6));
            } else if (i == 2) {
                optionPicker(By.xpath(prioritiesListLocator), 2, true);
                previousEuroApplication(entity.fakerRU.number().digits(6));
            } else if (i == 3) {
                optionPicker(By.xpath(prioritiesListLocator), 3, true);
                startsOpenShowing("industrial");
            }
            fillIndustrialPrototype(i, false);
        }
        click(By.cssSelector("input[value='Далее']"), true);
    }

    /**
     * Метод заполняет раздел №7 "Промышленные образцы" заявки на ПО
     *
     * @param locatorNumber int - отсчет с 0
     */
    public void fillIndustrialPrototype(int locatorNumber, boolean allDocs) {
        EntityDataBase entity = new EntityDataBase();
        String prototypeName = entity.fakerRU.lorem().sentence();
        String productIndication = entity.fakerRU.lorem().sentence();
        type(By.xpath(String.format("//textarea[contains(@id, 'repeatDesign:%s:design:dgnNameInv')]", locatorNumber)), prototypeName, true);
        type(By.xpath(String.format("//textarea[contains(@id, 'repeatDesign:%s:design:dgnNameProduct')]", locatorNumber)), productIndication, true);
        randomOptionPicker(By.xpath(String.format("//select[contains(@id, 'repeatDesign:%s:design:selectLocSubCls')]", locatorNumber)));
        if (allDocs) {
            addAllDocuments(locatorNumber);
        } else {
            addNecessaryDocuments(locatorNumber);
        }
    }

    /**
     * Метод загружает документы в разделе №7 "Промышленные образцы" заявки на ПО
     */
    protected void addAllDocuments(int locatorNumber) {
        File[] listOfFile = getListOfFiles(getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/prototype_picture"));
        for (int i = 1; i <= 7; i++) {
            String selectLocator = String.format("(//select[contains(@id, 'repeatDesign:%s:design')][count(option)=8][not(contains(@id, 'selectLocSubCls'))])[%s]", locatorNumber, i);
            optionPicker(By.xpath(selectLocator), i, true);
        }
        for (int k = 1; k <= 7; k++) {
            String uploadLocator = String.format("(//div[contains(@id, 'upload')][contains(@id, 'repeatDesign:%s:design')]//input[@type='file'])[%s]", locatorNumber, k);
            fileUploadWithCheck(uploadLocator, listOfFile[k - 1].getAbsolutePath());
        }
        uploadRandom3DFile("(//input[@type='file'])[8]");
        fileUpload(By.xpath(String.format("//table[contains(@id, 'designFileLoad')]//tr[position()=11]//div[contains(@id, 'upload')][contains(@id, 'repeatDesign:%s:design')]//input[@type='file']", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/чертеж_общего_вида_изделия%.jpg"), true);
        fileUpload(By.xpath(String.format("//table[contains(@id, 'designFileLoad')]//tr[position()=12]//div[contains(@id, 'upload')][contains(@id, 'repeatDesign:%s:design')]//input[@type='file']", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/конфекционная_карта%.pdf"), true);
        fileUpload(By.xpath(String.format("//table[contains(@id, 'designFileLoad')]//tr[position()=13]//div[contains(@id, 'upload')][contains(@id, 'repeatDesign:%s:design')]//input[@type='file']", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/описание_пром_образца%.pdf"), true);
        fileUpload(By.xpath(String.format("//table[contains(@id, 'designFileLoad')]//tr[position()=14]//div[contains(@id, 'upload')][contains(@id, 'repeatDesign:%s:design')]//input[@type='file']", locatorNumber)), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/Права_на_приоритета%.pdf"), true);
    }

    /**
     * Метод загружает только обязательные документы в разделе №7 "Промышленные образцы" заявки на ПО
     */
    protected void addNecessaryDocuments(int locatorNumber) {
        String selectLocator = String.format("//select[contains(@id, 'repeatDesign:%s:design')][count(option)=8][not(contains(@id, 'selectLocSubCls'))]", locatorNumber);
        optionPicker(By.xpath(selectLocator), getRandomInt(6) + 1, true);
        String uploadLocator = String.format("//div[contains(@id, 'upload')][contains(@id, 'repeatDesign:%s:design')]//input[@type='file']", locatorNumber);
        File[] listOfFile = getListOfFiles(getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/prototype_picture"));
        fileUploadWithCheck(uploadLocator, listOfFile[getRandomInt(7)].getAbsolutePath());
    }


    /**
     * Метод заполняет раздел №8 "Расчет пошлин" заявки на ПО
     */
    public void fillTaxFormIndustrial() {
        click(By.xpath("(//input[contains(@id, 'cbDuty')])[1]"), true);
        click(By.cssSelector("input[value='payment-document']"), true);
        fileUpload(By.xpath("//span[contains(@id, 'uploadGroup')]//input"), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Пп_об_оплате_ГП%.pdf"), true);
    }

    /**
     * Метод загружает все возможные документы в разделе №1 "Документы" в досылке по ПО
     */
    public void addAllDocsInIndustrialAddition() {
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/Доверенность%.pdf"));
        fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[2]", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/Письмо Заявителя%.pdf"));
        click(By.cssSelector("input[title='Добавить документ']"), true);
        randomOptionPicker(By.cssSelector("select"));
        fileUploadWithCheck("//span[contains(@id, 'uploadOtherDocId')]//input[@type='file']", getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_industrial/Письмо Заявителя%.pdf"));
    }


    /**
     * Метод заполняет раздел №1 "Общая информация" PCT заявки
     */
    public void fillPCTCommonInfoPart(String PCTNumber) throws NextButtomException {
        EntityDataBase entity = new EntityDataBase();
        String inventionName = entity.fakerRU.lorem().sentence();
        type(By.xpath("//input[contains(@id, 'numInv')]"), "N" + getRandomInt(999), false);
        type(By.xpath("//textarea[contains(@id, 'nameInv')]"), inventionName, false);
        type(By.xpath("//input[contains(@id, 'pctNumber')]"), PCTNumber, true);
        int count = 0;
        while (isElementPresent(By.xpath("//td[contains(text(), 'Общая информация')]"))) {
            if (count > 4) {
                throw new NextButtomException("Loop of next button!");
            }
            click(By.cssSelector("input[value='Далее']"), true);
            count++;
        }
    }

    /**
     * Метод заполняет раздел №6 "Дополнительная информация" PCT заявки
     */
    public void fillPCTAdditionalInfo() {
        click(By.xpath("//td[contains(text(), 'поданного')]/input[1]"), true);
        click(By.xpath("//td[contains(text(), 'поданной')]/input[1]"), true);
        click(By.xpath("//td[contains(text(), 'поданных')]/input[1]"), true);
        click(By.cssSelector("input[value='Далее']"), false);
    }

    /**
     * Метод заполняет раздел №7 "Документы" PCT заявки
     */
    public void fillPCTDocumentForm() {
        fileUpload(By.xpath("(//div[contains(@id, 'upload')]//input[@type='file'])[1]"), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"), true);
        uploadRandom3DFile("//td[contains(text(), 'Изображение в формате 3D(obj, step, stl, stp, u3d)')]/..//input[@type='file']");
        click(By.xpath("//input[contains(@id, 'addOtherDocId')]"), true);
        randomOptionPicker(By.xpath("//select"));
        fileUpload(By.xpath("(//div[contains(@id, 'upload')]//input[@type='file'])[20]"), getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Перевод_док_о_правопреемстве.pdf"), true);
        while (isElementPresent(By.xpath("//td[contains(text(), 'Документы')]"))) {
            click(By.cssSelector("input[value='Далее']"), true);
        }
    }

    /**
     * Метод загружает случайный 3D файл
     */
    protected void uploadRandom3DFile(String locator) {
        File[] listOf3DFile = getListOfFiles(getAbsolutePathToFile("src/test/resources/file_to_upload/3D_models"));
        String pathTo3DFile = listOf3DFile[getRandomInt(listOf3DFile.length - 1)].getPath();
        fileUpload(By.xpath(locator), getAbsolutePathToFile(pathTo3DFile), true);
    }

    /**
     * Метод заполняет номер заявки для досылки
     */
    public void typeAppNumberForAddition(String appNumber) {
        type(By.xpath("(//input[contains(@name, 'inputBox')])[last()]"), appNumber, false);
        click(By.xpath("//input[contains(@id, 'buttonAddSendId')]"), true);
    }

    /**
     * Метод заполняет номер заявки при подаче выделенной заявки
     */
    public void typeAppNumberForAllocatedApp(String appNumber) {
        type(By.xpath("//input[@type='text' and not(@tabindex)]"), appNumber, false);
        click(By.xpath("//input[contains(@id, 'buttonDivSendId')]"), true);
    }

    /**
     * Метод заполняет номер заявки для подачи измененного заявления
     */
    public void typeAppNumberForChangedApp(String appNumber) {
        type(By.xpath("(//input[contains(@name, 'inputBox')])[last()]"), appNumber, false);
        click(By.xpath("//input[contains(@id, 'buttonChangeSendId')]"), true);
    }

    /**
     * Метод заполняет номер заявки и дату для подачи доп. материалов с указанием даты
     */
    public void typeAppNumberForAdditionWithDate(String appNumber) {
        type(By.xpath("(//input[contains(@name, 'inputBox')])[last()]"), appNumber, false);
        click(By.xpath("//input[contains(@id, 'buttonAddSendIdWithSubmitDate')]"), true);
    }

    /**
     * Метод удаляет один случайный промышленный образец
     */
    public void deleteRandomSample() {
        List<WebElement> sampleOpeners = presenceOfElements(By.xpath("//input[@title='Развернуть']"));
        sampleOpeners.get(getRandomInt(sampleOpeners.size())).click();
        click(By.cssSelector("input[value='Удалить промышленный образец']"), true);
    }
}
