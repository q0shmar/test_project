package org.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

public class CloudlinkTest {
    private final String baseUrl = "https://portal.ift05.a2cloud.dev.box/";
    private final SelenideElement username = $x("//input[@id='username']");
    private final SelenideElement password = $x("//input[@id='password']");
    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement errorText = $x("//span[text()='Неправильное имя пользователя или пароль']");


    public void openBasePage() {
        Selenide.open(baseUrl);

    }

    public void setLogoPass() {
        username.setValue("m.vasilev");
        password.setValue("123");

    }

    public void submitButtonClick() {
        submitButton.click();
        errorText.shouldBe(Condition.visible.because("Элемент должен быть виден"), Duration.ofSeconds(10));
        Selenide.sleep(10000);
    }

    @Test
    public void test() {
        openBasePage();
        setLogoPass();
        submitButtonClick();
    }

    @Test
    void name() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
