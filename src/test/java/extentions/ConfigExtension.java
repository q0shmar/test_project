package extentions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;


import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static extentions.TestsExecutionListener.initDriver;

@Log4j2
public class ConfigExtension implements AfterEachCallback, BeforeEachCallback, BeforeAllCallback, AfterAllCallback, InvocationInterceptor {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        // Настройка логирования
        var logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);

        // Создание ChromeOptions и установка логирования
        Configuration.browserCapabilities.setCapability("goog:loggingPrefs", logs);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        closeWebDriver();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        initDriver();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        //ReportUtils.addEnvironment("environment=" + System.getProperty("env"));
    }
}
