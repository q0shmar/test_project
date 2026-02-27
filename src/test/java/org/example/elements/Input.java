package org.example.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$x;

public record Input(SelenideElement input) implements TypifiedElement {
    public static final char[] SPECIAL_CHARACTERS = "!@#$%^&*()_+{}[]|\\:;\"<>,.?/".toCharArray();

    public static Input byLabel(String label) {
        return byLabel(label, 1);
    }


    //    @Step("Получение Input по label:'{0}' с индексом: '{1}'")
    public static Input byLabel(String label, int index) {
        var xpath = String.format("(//label[starts-with(., '%s')]/parent::*//input)[%d]", label, index);
        return new Input($x(xpath));
    }


    public static Input byXpath(String xPath) {
        return new Input($x(xPath));
    }

    public static Input byValue(String value) {
        return new Input($x("//input[@value='%s']".formatted(value)));
    }

    public static Input byType(String type, int index) {
        return new Input($x("(//input[@type='%s'])[%d]".formatted(type, index)));
    }

    //    @Step("Получение Input по role:'{0}'")
    public static Input byRole(String role) {
        return new Input($x("//input[@role='%s']".formatted(role)));
    }

    public static Input byRole(String role, int index) {
        return new Input($x("(//input[@role='%s'])[%d]".formatted(role, index)));
    }

    public static Input byLabelV2(String label) {
        return new Input($x("//label[starts-with(.,'%s')]/following::input[1]".formatted(label)));
    }

    public static Input byPlaceholder(String placeholder) {
        return new Input($x("//input[@placeholder='%s']".formatted(placeholder)));
    }

    public static Input byName(String name) {
        return new Input($x("//input[@name='%s']".formatted(name)));
    }

    public static Input byFieldName(String name) {
        return new Input($x("//span[text()='%s']/../input".formatted(name)));
    }

    public String getValue() {
        input.shouldBe(Condition.visible);
        return input.getValue();
    }

    public void setValue(Object value) {
        input.shouldBe(Condition.visible).shouldBe(Condition.enabled);
        clear();
        input.setValue(String.valueOf(value));
    }

    public void clear() {
        input.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }

    public static void cleanInput(SelenideElement element) {
        element.sendKeys(Keys.chord("%sa%s".formatted(Keys.CONTROL, Keys.BACK_SPACE)));
        if (element.is(Condition.not(Condition.empty))) {
            element.clear();
        }
        if (element.is(Condition.not(Condition.empty)) && element.getValue() != null) {
            for (int i = 0; i < element.getValue().length(); ++i) {
                element.sendKeys(Keys.BACK_SPACE);
            }
        }
        if (element.is(Condition.not(Condition.empty))) {
            Assertions.fail("Поле не очищено");
        }
    }
}
