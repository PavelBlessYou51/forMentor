package manager;

import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;


public class CertificateHelper extends HelperBase {

    public CertificateHelper(ApplicationManager manager) {
        super(manager);
    }

    public String certificateDeactivation() {
        selectSphereOfApplication("profile");
        selectCertificateAction("deactivate");
        deactivate("123456");
        String confirmation = getTextFromElement(By.xpath("//div[contains(text(), 'У вас нет действительного сертификата.')]")).strip();
        return confirmation;
    }


    public String certificateActivation() {
        selectSphereOfApplication("profile");
        selectCertificateAction("activate");
        fillCirtificateForm("Prokoshev", "Pavel", "Vladimirovich", "prokoshevpavel@mail.ru", "123456");
        String activationConfirm = getActivationConfirm();
        return activationConfirm;
    }


    protected void selectCertificateAction(String action) {
        if ("deactivate".equals(action)) {
            click(By.xpath("//span[contains(text(), 'Отзыв сертификата')]"), true);
        } else if ("activate".equals(action)) {
            click(By.xpath("(//span[contains(text(), 'Выпуск сертификата')])[1]"), true);
        }

    }

    protected void deactivate(String pinCode) {
        type(By.xpath("//input[contains(@id, 'pin-input')]"), pinCode, false);
        click(By.cssSelector("input[value='Отозвать сертификат']"), false);
    }

    protected void fillCirtificateForm(String lastName, String firstName, String patronymic, String email, String pinCode) {
        type(By.xpath("(//input[@class='application-input'])[1]"), lastName, true);
        type(By.xpath("(//input[@class='application-input'])[2]"), firstName, true);
        type(By.xpath("(//input[@class='application-input'])[3]"), patronymic, true);
        type(By.xpath("(//input[@class='application-input'])[4]"), email, true);
        type(By.xpath("(//input[@class='application-input'])[5]"), pinCode, true);
        type(By.xpath("(//input[@class='application-input'])[6]"), pinCode, true);
        click(By.xpath("//input[contains(@id, 'issue-certificate')]"), true);
    }

    protected String getActivationConfirm() {
        String rawConfirm = getTextFromElement(By.xpath("//div[contains(text(), 'У вас есть сертификат')]"));
        String confirm = rawConfirm.split(", ")[0].strip();
        return confirm;
    }


}
