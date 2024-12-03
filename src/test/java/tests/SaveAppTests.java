package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveAppTests extends TestBase {

    @ParameterizedTest
    @ValueSource(strings = {"invention", "industrial"})
    public void saveInventionApp(String appType) {
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        boolean result = app.sender().saveInventionAppsToSoprano(appType);
        assertTrue(result);
    }


}
