package extentions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.Reader;
import org.junit.platform.launcher.TestExecutionListener;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class TestsExecutionListener implements TestExecutionListener {
    private static final String BASE_URL = Reader.getProperty("base.url");

    @SneakyThrows
    public static void initDriver() {
        Configuration.baseUrl = BASE_URL;
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadTimeout = 90000;
        Configuration.timeout = 50000;
        Configuration.browser = "chrome";

        if (isRemote()) {
            configureRemoteDriver();
        } else {
            configureLocalDriver();
        }
    }

    @SneakyThrows
    private static void configureRemoteDriver() {
        log.info("Настройка WebDriver для Selebrow");

        System.setProperty("selenide.screenshot", "false");

        boolean isGitLabRun = System.getenv("GITLAB_CI") != null;

        if (isGitLabRun) {
            log.info("Запуск в режиме GitLab CI");
            Configuration.remote = Reader.getProperty("webdriver.remote.url");
        } else {
            log.info("Запуск в режиме обычного удаленного сервера");
            Configuration.remote = Reader.getProperty("webdriver.remote.url.server");
        }

        log.info("URL WebDriver: {}", Configuration.remote);

        var options = new ChromeOptions();
        options.setPlatformName("Linux");

        var browserVersion = System.getProperty("browser.version", "");

        // Согласно документации Selebrow: если версия пустая или "latest" - не указываем
        if (browserVersion != null && !browserVersion.trim().isEmpty() && !"latest".equals(browserVersion)) {
            options.setBrowserVersion(browserVersion);
            log.info("Используется версия Chrome: {}", browserVersion);
        } else {
            log.info("Using default Chrome version (Selebrow will auto-select)");
            // Не устанавливаем browserVersion - Selebrow выберет дефолтную
        }

        Configuration.screenshots = false;
        Configuration.savePageSource = false;
        Configuration.reportsFolder = "target/allure-results";

        // Конфигурация Selebrow согласно документации
        var selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", false);
        selenoidOptions.put("enableLog", true);
        selenoidOptions.put("sessionTimeout", "10m");
        selenoidOptions.put("screenResolution", "1920x1080x24");
        selenoidOptions.put("env", List.of("TZ=UTC"));
        selenoidOptions.put("enableWebsocket", false);

        if (browserVersion != null && !browserVersion.trim().isEmpty() && !"latest".equals(browserVersion)) {
            selenoidOptions.put("version", browserVersion);
        }

        selenoidOptions.put("name", "Cloudlink UI Tests");
        selenoidOptions.put("labels", Map.of(
                "project", "cloudlink",
                "testType", "regression"
        ));

        options.setCapability("selenoid:options", selenoidOptions);

        // Опции Chrome
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--window-size=1920,1080",
                "--remote-allow-origins=*",
                "--remote-debugging-port=0",
                "--disable-dev-tools"
        );

        Configuration.browserCapabilities = options;

        log.info("Настройка Selebrow выполнена. Mode: {}", isGitLabRun ? "GitLab CI" : "Remote Server");
    }


    @SneakyThrows
    private static void configureLocalDriver() {
        log.info("UI Tests запущены локально");
        WebDriverManager.chromedriver()
                .driverVersion(Reader.getProperty("webdriver.version"))
                .setup();

        var options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions",
                "--no-proxy-server");
        Configuration.browserCapabilities = options;
        Configuration.fileDownload = FileDownloadMode.FOLDER;
    }

    public static Boolean isRemote() {
        return Boolean.parseBoolean(System.getProperty("remote", "false"));
    }
}

