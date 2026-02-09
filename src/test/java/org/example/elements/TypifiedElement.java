package org.example.elements;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.Instant;

import static com.codeborne.selenide.Selenide.*;

public interface TypifiedElement {
    String scrollCenter = "{block: 'center'}";
    String postfix = "[{}]";

    /**
     * Получить ближайший по DOM элемент с xpathSearchElement к элементу xpathNearElement
     * *[contains(text(), '₽/сут.')]    button[.='Действия']
     *
     * @param xpathSearchElement xpath искомого element
     * @param xpathNearElement   xpath близкого к искомому element элемента
     * @return искомый element
     */

    static SelenideElement getNearElement(String xpathSearchElement, String xpathNearElement) {
        return getNearElement(xpathSearchElement, xpathNearElement, -1);
    }

    static SelenideElement getNearElement(String xpathSearchElement, String xpathNearElement, int index) {
        return $x(String.format("(//%s/ancestor-or-self::*[count(.//%s) = 1]//%s)[%s]",
                xpathNearElement, xpathSearchElement,
                xpathSearchElement, TypifiedElement.getIndex(index)));
    }

    /**
     * Получить ближайший по расстоянию элемент с xpathSearchElement к элементу xpathNearElement
     *
     * @param xpathSearchElement xpath искомого element
     * @param xpathNearElement   xpath близкого к искомому element элемента
     * @return искомый element
     */
    static SelenideElement findNearestElement(String xpathSearchElement, String xpathNearElement) {
        var elementE1 = $x(xpathNearElement).shouldBe(Condition.exist);
        var elementsE2 = $$x(xpathSearchElement).shouldBe(CollectionCondition.sizeNotEqual(0));
        if (elementsE2.isEmpty()) {
            throw new NoSuchElementException(String.format("No elements matching %s found", xpathSearchElement));
        }
        WebElement nearestElement = null;
        double minDistance = Double.MAX_VALUE;
        for (WebElement element : elementsE2) {
            double distance = calculateDistance(element, elementE1);
            if (distance < minDistance) {
                minDistance = distance;
                nearestElement = element;
            }
        }
        if (nearestElement == null)
            throw new NoSuchElementException("No nearest element found");
        return (SelenideElement) nearestElement;
    }

    static double calculateDistance(WebElement element1, WebElement element2) {
        var x1 = element1.getLocation().getX();
        var y1 = element1.getLocation().getY();
        var x2 = element2.getLocation().getX();
        var y2 = element2.getLocation().getY();
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    static String getIndex(int index) {
        return (index == -1) ? "last()" : String.valueOf(index);
    }

    @SneakyThrows
    //Для решения редкой проблемы динамически обновляемого контента (когда между Condition и getText() может обновиться элемент)
    static String findNotEmptyText(SelenideElement element, Duration duration) {
        var start = Instant.now();
        element.shouldBe(Condition.visible);
        while (duration.compareTo(Duration.between(start, Instant.now())) > 0) {
            final String s = element.getText();
            if (!s.isEmpty()) return s;
            sleep(300);
        }
        throw new TimeoutException("Return empty string, duration: %s".formatted(duration));
    }

    static void open(String url) {
        Selenide.open(url);
    }
}
