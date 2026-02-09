package org.example.testpages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$x;

public class NewOrderPage {

    @Step("Выбрать продукт: '{0}'")
    public NewOrderPage selectProduct(String product) {
        SelenideElement productElement = $x("//*[text()='%s']".formatted(product));

        // Явное ожидание появления и видимости
        productElement.should(appear, Duration.ofSeconds(15));
        productElement.shouldBe(visible);
        productElement.shouldBe(enabled);
        productElement.scrollTo();
        productElement.click();

        return this;
    }
}
