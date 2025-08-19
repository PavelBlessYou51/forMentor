package selenide_tests.manager;

import io.qameta.allure.Step;
import jdbc.JdbcHelper;
import model.EntityDataBase;
import model.PatentAgent;
import model.PersonData;
import org.openqa.selenium.By;


import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;

public class ForwardHelper extends HelperBase {

    /**
     * Метод вводит номер патентного ведомства при пересылке заявки
     */
    @Step("Ввод номера патентного ведомства при пересылке заявки")
    public void typeDepartmentNumber() {
        $(By.xpath("//span[contains(text(), 'Пересылка ЕА заявки из нацведомства')]/ancestor::div[contains(@id, 'input-box_container')]//input[contains(@id, 'inputBox')]")).setValue("RU");
        $(By.xpath("//span[contains(text(), 'Пересылка ЕА заявки из нацведомства')]/ancestor::div[contains(@id, 'input-box_container')]//input[@value='Переслать']")).click();
    }

    /**
     * Метод заполняет раздел №1 "Общая информация" заявки на ИЗО - название изобретение
     */
    @Step("Ввод названия изобретения")
    public void fillNameOfInvention() {
        EntityDataBase entity = new EntityDataBase();
        String inventionName = entity.fakerRU.lorem().sentence();
        $("textarea").setValue(inventionName);
    }

    /**
     * Метод проверяет, что адрес для переписки соответствует адресу ПП
     */
    @Step("Сверка адреса для переписки")
    public boolean checkAddressForCorrespondents(PatentAgent patentAgent) {
        String fullName = getTextFromElement(By.xpath("(//div[contains(@id, 'currentAddress')]/div)[1]")).split(": ")[1].toLowerCase().strip();
        String address = getTextFromElement(By.xpath("(//div[contains(@id, 'currentAddress')]/div)[3]")).split(": ")[1].toLowerCase().strip();
        String agentFullName = String.join(" ", Arrays.asList(patentAgent.surname, patentAgent.name, patentAgent.patronymic)).toLowerCase().strip();
        return fullName.equals(agentFullName) && address.equals(patentAgent.address.toLowerCase().strip().replace("\n", " "));
        // заменить ожиданием
    }


    /**
     * Метод добавляет нового представителя в форме подачи заявления
     */
    @Step("Добавление нового представителя")
    public EntityDataBase addNewRepresentative(boolean isAgent) {
        if (isAgent) {
            JdbcHelper jdbcHelper = new JdbcHelper();
            PatentAgent agent = jdbcHelper.getAgentData();
            selectRepresentativeType(true);
            selectPatentAgentByNumber(agent.regNumber);
            return agent;
        } else {
            selectRepresentativeType(false);
            $(By.xpath("//input[contains(@value, 'Добавить нового')]")).click();
            PersonData person = new PersonData();
            $(By.xpath("//input[contains(@id, '1:firstName')]")).setValue(person.name);
            $(By.xpath("//textarea[contains(@id, '1:name')]")).setValue(person.surname);
            $(By.xpath("//input[contains(@id, '1:middleName')]")).setValue(person.patronymic);
            $(By.xpath("//input[contains(@id, '1:email')]")).setValue(person.email);
            $(By.xpath("//input[contains(@id, '1:country')]")).setValue(person.countryCode);
            $(By.xpath("//input[contains(@id, '1:phone')]")).setValue(person.phoneNumber);
            $(By.xpath("//textarea[contains(@id, '1:address')]")).setValue(person.address);
            $(By.xpath("//input[contains(@id, '1:idTown')]")).setValue(person.postCode);
            return person;
        }
    }

    /**
     * Метод загружает все возможные документы
     */
    @Step("Загрузка документов")
    public void uploadAllDocs() {
        uploadFileWithCheck("//td[contains(text(), 'Описание изобретения:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf");
        uploadFileWithCheck("//td[contains(text(), 'Формула изобретения:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Формула_(цветной_файл)%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Чертеж(и) и иные материалы:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Чертежи_(%).pdf");
        uploadRandom3DFile("//td[contains(text(), 'Изображение в формате 3D(obj, step, stl, stp, u3d)')]/following-sibling::td//input[@type='file']");
        uploadFileWithCheck("//td[contains(text(), 'Реферат:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Реферат_(много_шрифтов)%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Списки последовательностей нуклеотидов и/или аминокислот:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Список_последовательностей%.txt");
        uploadFileWithCheck("//td[contains(text(), 'Документ о депонировании микроорганизма:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Документ_о_депонировании%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Документ, подтверждающий наличие оснований для уменьшения размера пошлины:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Основания_уменьшения_пошлины%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Доверенность, удостоверяющая полномочия представителя:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Доверенность%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Документ, относящийся к передаче права на евразийскую заявку:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Передача_права_на_евр_заявку%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Письмо заявителя (в т.ч. сопроводительное):')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Письмо_заявителя%.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Уведомление национального ведомства:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Уведомление_нац%ведомства.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Заявление поданное в национальное ведомство:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Заявление%подан%нац_ведомство.pdf");
        uploadFileWithCheck("//td[contains(text(), 'Заверенная копия заявки:')]/following-sibling::td//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Заверенная%копия%заявки.pdf");
        $(By.xpath("//input[contains(@id, 'addOtherDocId')]")).click();
        $("select").selectOptionByValue("OTHER_DOC");
        uploadFileWithCheck("//div[contains(@id, 'uploadOtherDocId')]//input[@type='file']", "src/test/resources/file_to_upload/doc_for_madras_invention/Другое%.pdf");
    }

    /**
     * Метод выбирает ЕПП
     */
    @Step("Выбор ЕПП")
    public void selectCommonFee() {
        $(By.xpath("//input[contains(@id, 'cbDuty001')]")).click();
    }
}


