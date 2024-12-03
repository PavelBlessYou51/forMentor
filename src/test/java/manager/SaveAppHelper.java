package manager;

import org.openqa.selenium.By;

import java.util.List;

public class SaveAppHelper extends HelperBase {


    public SaveAppHelper(ApplicationManager manager) {
        super(manager);
    }


    public boolean saveInventionAppsToSoprano(String appType) {
        selectSphereOfApplication(appType);
        click(By.xpath("//a[contains(text(), 'Полученные заявки')]"), false);
        boolean result = saveAppsFromList(appType);
        return result;
    }

    protected boolean saveAppsFromList(String appType) {
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
            findApp(application);
            openApp();
            saveApp();
            String confirmation = getTextFromElement(By.className("error-message"));
            if (!String.format("Заявка %s успешно сохранена в Soprano.", application).equals(confirmation)) {
                result = false;
                break;
            }
        }
        return result;

    }


    protected void findApp(String application) {
        type(By.xpath("//span[contains(text(), '№ Евразийской заявки')]/ancestor::div/input"), application, false);
        click(By.cssSelector("input[value='Найти']"), true);
    }

    protected void openApp() {
        click(By.className("command-link-column"), true);
    }

    protected void saveApp() {
        click(By.xpath("//span[contains(text(), 'РАСЧЕТ ПОШЛИН')]"), true);
        click(By.xpath("//input[contains(@id, 'btnSave')]"), true);
    }
}
