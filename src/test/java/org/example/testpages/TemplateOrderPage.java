package org.example.testpages;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.elements.Input;
import org.example.enums.ProductCategory;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;
import static org.assertj.core.api.Fail.fail;
import static org.example.ProductName.*;

public class TemplateOrderPage {

    private final String BOOT_DISK_SIZE = "20";
    private final String MOUNT_POINT_SIZE = "20";
    private final Input nameInput = Input.byXpath("//span[text()='Имя']//following::input[@type='text'][1]");
    private final SelenideElement aRecordCheckbox = $x("//input[@type='checkbox']");
    private final SelenideElement netSegmentOpenSelect = $x("(//span[text()='Сетевой сегмент']//following::span)[1]");
    private final SelenideElement netSegmentSelectValue = $x("//div[text()='DEV_DEV (Обезличенные данные)']");
    private final SelenideElement dataCenterOpenSelect = $x("//span[text()='Дата-центр']//following::div[1]");
    private final SelenideElement dataCenterSelectValue = $x("//div[@class='ant-select-item-option-content' and text()='cod-a']");
    private final SelenideElement platformOpenSelect = $x("//span[text()='Платформа']//following::div[1]");
    private final SelenideElement platformSelectValue = $x("(//div[text()='%s']");
    private final SelenideElement imageOSOpenSelect = $x("//span[text()='Образ ОС']");
    private final SelenideElement imageOSSelectValue = $x("//div[@class='ant-select-item-option-content' and text()='alma-template']");
    private final SelenideElement cpuQuantityButton = $x("(//div[contains(@class, 'radioTag__tag__sG1YFL') and contains(text(), '2 CPU') ])");
    private final SelenideElement ramQuantityButton = $x("(//div[contains(@class, 'radioTag__tag__sG1YFL') and text()= '2 GB'])");
    private final SelenideElement addMountPointButton = $x("//span[text()='Добавить']");
    private final SelenideElement mountPointInput = $x("//span[text()='/app/']//following::input[1]");
    private final Input inputValueDiskOrMountPoint = Input.byXpath("//input[@role='spinbutton']");
    private final SelenideElement sshKeyOpenSelect = $x("//*[text()='SSH-ключ']");
    private final SelenideElement sshKeySelectValue = $x("//div[text()='key-demo (key-demo количество: 1)']");
    private final SelenideElement makeOrderButton = $x("//span[text()='Заказать']");
    private final SelenideElement productCategory = $x("//h4//following::span[2]");
    private final SelenideElement expectedOrder = $x("//h4//following::span[1]");
    private final SelenideElement implementationSchemeButton = $x("//span[text()='Схема выполнения']");
    private final SelenideElement goToResourcesButton = $x("//span[text()='Перейти к ресурсам']");
    private final SelenideElement orderConfigurationInfo = $x("//span[@class='ant-typography']"); //пример - starvault, 2 CPU, 2 GB RAM, 30 GB


    public void setPlatform(String platform) {
        platformOpenSelect.shouldBe(visible).click();
        $x("//div[text()='%s']".formatted(platform)).scrollIntoView(true).shouldBe(visible).click();
    }

    public void setImageOC(String image) {
        imageOSOpenSelect.shouldBe(visible).click();
        $x("//div[@class='ant-select-item-option-content' and text()='%s']".formatted(image)).scrollIntoView(true).shouldBe(visible).click();
    }

    //public void setMountPoint() {
    //  addMountPointButton.shouldBe(visible).click();
    //$x("//span[text()='/app/']//following::input[1]").shouldBe(visible).setValue("123");
    //}



    public void setShhKey(String SSH) {
        var sshKey = $x("//div[@class='ant-select-item-option-content' and contains(text(), '%s')]".formatted(SSH));
        actions().moveToElement(sshKeyOpenSelect.scrollTo().shouldBe(visible))
                .click()
                .perform();
        actions().moveToElement(sshKey.scrollTo().shouldBe(visible))
                .click()
                .perform();
    }

     public void checkOrderButtonDisabled() {

        makeOrderButton.shouldHave(Condition.cssClass("disabled"), Duration.ofSeconds(15));
        makeOrderButton.shouldBe(Condition.disabled,Duration.ofSeconds(15));

    }

    @Step("Установка чекбокса 'Создать A-запись'")
    public void setARecordCheckbox(boolean shouldBeChecked) {
        aRecordCheckbox
                .shouldBe(enabled.because("Чекбокс А-запись должен отображаться"), Duration.ofSeconds(10))
                .scrollTo();

        boolean isChecked = aRecordCheckbox.isSelected(); // Проверяем текущее состояние чекбокса

        if (isChecked != shouldBeChecked) {
            aRecordCheckbox.click(); // Кликаем, если состояние не соответствует требуемому
        }
    }

    @Step("Очистка Input:{0}")
    public void setInputClear(Input input) {
        input.input().click();
        input.input().sendKeys(Keys.chord(Keys.CONTROL, "a")); // Выделяем весь текст (для Windows)
        input.input().sendKeys(Keys.BACK_SPACE); // Удаляем выделенный текст
    }

    @Step("Установка имени заказа {0}")
    public void setNameInput(String orderName) {
        setInputClear(nameInput);
        nameInput.input().shouldBe(visible.because("Инпут должен отображаться")).setValue(orderName);

        // Используем явное ожидание для проверки наличия элемента
        var checkingNameInput = getCheckingNameInput("должно быть не длиннее, чем 32 символа",
                "должно быть не длиннее, чем 15 символов",
                "Проверьте имя");
        checkingNameInput.shouldNotBe(visible.because("Необходимо проверить введенные данные на соответствие условиям"));
    }

    // Метод для создания локатора с несколькими вариантами текста
    protected SelenideElement getCheckingNameInput(String... errorTexts) {
        // Создаем часть XPath для условий
        var xpathBuilder = new StringBuilder("//li[");
        for (int i = 0; i < errorTexts.length; i++) {
            xpathBuilder
                    .append("text()='")
                    .append(errorTexts[i])
                    .append("'");
            if (i < errorTexts.length - 1) {
                xpathBuilder.append(" or ");
            }
        }
        xpathBuilder.append("]");

        // Возвращаем элемент с созданным локатором
        return $x(xpathBuilder.toString());
    }

    public void makeOrder() {
        makeOrderButton.shouldBe(enabled).click();
    }

    @Step("Установка точки монтирования {0}")
    public void setMountPoint(String mountPointName, String size) {
        clickMountPoint();
        setValueForMountPoint(mountPointName, size);
    }

    @Step("Добавление точки монтирования {0}")
    public void clickMountPoint() {
        addMountPointButton
                .shouldBe(visible.because("Кнопка должна быть видна"))
                .click();
    }

    @Step("Ввод значения точки монтирования {0}")
    public void setValueForMountPoint(String mountPointName, String size) {
        mountPointInput.shouldBe(visible)
                .val(mountPointName);
        setInputClear(inputValueDiskOrMountPoint);
        inputValueDiskOrMountPoint.input()
                .shouldBe(visible, enabled.because("Поле должно быть доступно"))
                .sendKeys(size);
    }

    //дз - методы для проверки названия; категории продукта; setNetSegment; проверка инфы после заказа (схема графов и вернуться ко всем заказам)
    //1) проверка названия продукта
    public void checkProductName(String orderName) {
        Selenide.sleep(5000);
        var text=nameInput.input().shouldBe(visible.because("Инпут должен отображаться"),Duration.ofSeconds(15))
                .getAttribute("value");
        var actualValue= Objects.requireNonNull(text).substring(0,text.indexOf("-"));
        var expectedValue= Objects.requireNonNull(orderName).substring(0,text.indexOf("-"));
        Assertions.assertEquals(actualValue, expectedValue);
    } // переписать под любой продукт, а не только для astra

    //2) категория продукта *разбираюсь с enum
    public void checkCategory() {
        productCategory.shouldBe(visible)
                .shouldHave(value("Базовые вычисления"));
    }

    //3) setNetSegment
    public void setNetSegment() {
        netSegmentOpenSelect.shouldBe(visible).click();
        netSegmentSelectValue.shouldBe(visible).click();
    }

    //4) схема графов и вернуться ко всем заказам
    //5) сверка элементов после заказа
    public void postOrderElementsCheck(String orderName) {
        var expectedCpu = "2";
        var expectedRam = "4";
        var expectedStorage = "20";

        //метод можно дополнить чтобы он проверял соответствие cpu ram
        // storage со страницей заказа. разделить cpu ram storage на отдельные элементы. + суммировать доп диски
        var text = orderConfigurationInfo.shouldBe(visible).getText();
        var splitText = Arrays.asList(text.split(","));
        Assertions.assertAll("Проверка элементов",
                () -> Assertions.assertTrue(orderName.contains(splitText.getFirst())),
                () -> Assertions.assertTrue(splitText.get(1).contains(expectedCpu)),
                () -> Assertions.assertTrue(splitText.get(2).contains(expectedRam)),
                () -> Assertions.assertTrue(splitText.get(3).contains(expectedStorage))
        );

        goToResourcesButton.shouldBe(visible).click();
        implementationSchemeButton.shouldBe(visible);
        // как написать метод, чтобы click() делался по обеим кнопкам?
    }

    public TemplateOrderPage createSimpleProductTemplate(String orderName, String platform, String OS, Boolean createARecord) {
//        checkOrderButtonDisabled();
        checkOrderClass();
        checkProductName(orderName);
        setNameInput(orderName);
        if (createARecord) {
            setARecordCheckbox(true);
        }
        setPlatform(platform);
        setNetSegment();
        if (orderName.toLowerCase().contains("ubuntu") || orderName.toLowerCase().contains("astra")) {
            setImageOC(OS);
        }
        setMountPoint("test", MOUNT_POINT_SIZE);
        setShhKey("key-demo");
        makeOrder();
        postOrderElementsCheck(orderName);
        return this;
    }

    @Step("Проверка правильности отнесения продукта к классу продуктов")
    public void checkOrderClass() {
        var expectedOrders = expectedOrder.shouldBe(visible.because("Элемент должен быть виден")).getText();
        var orderClass = productCategory.shouldBe(visible.because("Элемент должен быть виден")).getText();

        switch (expectedOrders) {
            case ALMA, ASTRA, CENTOS, DEBIAN, RED_OS, UBUNTU, WINDOWS, WINDOWS_8, WINDOWS_10, WINDOWS_2012,
                 WINDOWS_2016, ORACLE_LINUX -> {
                if (!orderClass.equals(ProductCategory.БАЗОВЫЕ_ВЫЧИСЛЕНИЯ.getCategoryName())) {
                    fail("Ошибка: для продукта %s ожидается класс 'Базовые вычисления', но получен: %s".formatted(expectedOrders, orderClass));
                }
            }
            case APACHE_KAFKA, RABBIT_MQ -> {
                if (!orderClass.equals("Брокеры сообщений")) {
                    fail(String.format("Ошибка: для продукта %s ожидается класс 'Брокеры сообщений', но получен: %s", expectedOrders, orderClass));
                }
            }
            case CLICK_HOUSE, ELASTIC_SEARCH, MONGO_DB, MY_SQL, POSTGRE_SQL, ARENADATA_PROSPERITY, REDIS, TANTOR_BE -> {
                if (!orderClass.equals("Базы данных")) {
                    fail("Ошибка: для продукта %s ожидается класс 'Базы данных', но получен: %s".formatted(expectedOrders, orderClass));
                }
            }
            case NOVA_CLUSTER, KUBERNETES_CLUSTER -> {
                if (!orderClass.equals("Контейнеры")) {
                    fail("Ошибка: для продукта %s ожидается класс 'Контейнеры', но получен: %s".formatted(expectedOrders, orderClass));
                }
            }
            case NGINX, WILDFLY, ZVIRT, MINIO -> {
                if (!orderClass.equals("Веб-приложения")) {
                    fail("Ошибка: для продукта %s ожидается класс 'Веб-приложения', но получен: %s".formatted(expectedOrders, orderClass));
                }
            }
            case STARVAULT -> {
                if (!orderClass.equals("Управление секретами")) {
                    fail("Ошибка: для продукта %s ожидается класс 'Управление секретами', но получен: %s".formatted(expectedOrders, orderClass));
                }
            }
            default -> throw new IllegalArgumentException("Ошибка: неизвестный заказ: %s".formatted(expectedOrders));
        }
    }

    /**
     * Метод для разбиения текста на части
     *
     * @param text      исходный текст
     * @param delimiter разделитель (например, запятая, точка с запятой и т.д.)
     * @return список частей текста
     */
    public static List<String> splitText(String text, String delimiter) {
        List<String> parts = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return parts;
        }

        // Разбиваем текст по разделителю
        String[] splitParts = text.split(delimiter);
        for (String part : splitParts) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty()) {
                parts.add(trimmedPart);
            }
        }

        return parts;
    }
}


