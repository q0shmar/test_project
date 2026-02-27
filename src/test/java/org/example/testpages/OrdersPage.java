package org.example.testpages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

public class OrdersPage {
    private final SelenideElement ordersButton = $x("(//li[@data-menu-id='rc-menu-uuid-76373-1-orders'])");
    private final SelenideElement csvButton = $x("(//button[@class='ant-btn ant-btn-text ant-btn-color-default ant-btn-variant-text csv__R_iBFl'])");
    private final SelenideElement filterButton = $x("(//button[@aria-describedby='rc_unique_551'])");
    private final SelenideElement addResourceButton = $x("(//button[@class='ant-btn ant-btn-primary ant-btn-color-primary ant-btn-variant-solid addButton__mYsjB_'])");
    private final SelenideElement orderProducts = $x("//span[text()='Добавить ресурс']//parent::button");

    @Step("Переход на страницу заказа продуктов")
    public OrdersPage clickAddResource() {
        orderProducts.shouldBe(Condition.visible, Duration.ofSeconds(15)).scrollTo()
                .click();
        return new OrdersPage();
    }
}
