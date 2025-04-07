package manager;

import org.openqa.selenium.By;

import java.util.List;

/**
 * Класс-помощник. Содержит методы для сохранения заявок в Soprano
 */
public class SaveAppAndAdditionHelper extends HelperBase {


    public SaveAppAndAdditionHelper(ApplicationManager manager) {
        super(manager);
    }

    /**
     * Метод запускает процесс сохранения заявок и досылок в Soprano и возвращает true, если все заявки успешно сохранены
     */
    public void saveDocToSoprano(String docType, String appNumber) {
        click(By.xpath(String.format("//a[contains(text(), 'Полученные %s')]", docType)), false);
        findDoc(appNumber);
        openDoc();
        saveDoc();
    }

    /**
     * Метод запускает процесс сохранения заявок и досылок в Soprano и возвращает true, если все заявки успешно сохранены
     */
    public boolean saveDocsToSoprano(String appType, String docType) {
        selectSectionOfAccount(appType);
        click(By.xpath(String.format("//a[contains(text(), 'Полученные %s')]", docType)), false);
        boolean result = saveDocsFromList(appType);
        return result;
    }

    /**
     * Метод извлекает номера заявок/досылок по заявкам из файла и поочередно сохраняет в Soprano
     */
    protected boolean saveDocsFromList(String appType) {
        List<String> listOfApps;
        if ("invention".equals(appType)) {
            listOfApps = applicationNumbersReader("src/test/resources/list_of_app/inventionAppNumbers.txt");
        } else if ("industrial".equals(appType)) {
            listOfApps = applicationNumbersReader("src/test/resources/list_of_app/industrialAppNumbers.txt");
        } else {
            throw new IllegalArgumentException();
        }
        boolean result = true;
        for (String application : listOfApps) {
            findDoc(application);
            openDoc();
            saveDoc();
            String confirmation = getTextFromElement(By.className("error-message"));
            if (!(String.format("Заявка %s успешно сохранена в Soprano.", application).equals(confirmation) || String.format("Досылка для заявки %s сохранена в Soprano.", application).equals(confirmation))) {
                result = false;
                System.out.printf("Заявка %s не сохранена!", application);
                break;
            }
        }
        return result;

    }

    /**
     * Метод находит заявку/досылку по заявке по номеру
     */
    protected void findDoc(String application) {
        type(By.xpath("//span[contains(text(), '№ Евразийской заявки')]/ancestor::div/input"), application, false);
        click(By.cssSelector("input[value='Найти']"), true);
    }

    /**
     * Метод открывает найденную заявку/досылку по заявке
     */
    protected void openDoc() {
        click(By.className("command-link-column"), true);
    }

    /**
     * Метод сохраняет заявку/досылку по заявке
     */
    protected void saveDoc() {
        click(By.xpath("//span[contains(text(), 'РАСЧЕТ ПОШЛИН')]"), true);
        click(By.xpath("//input[contains(@id, 'btnSave')]"), true);
    }


}
