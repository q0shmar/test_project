package org.example;

import io.qameta.allure.Step;
import org.example.enums.Role;
import org.example.testpages.NewOrderPage;
import org.example.testpages.OrdersPage;
import org.example.testpages.PortalLoginPage;
import org.junit.jupiter.api.BeforeEach;

import static org.apache.commons.lang3.RandomStringUtils.secure;
import static org.example.ProductName.*;

public abstract class AbstractOrder {
    protected final OrdersPage ordersPage = new OrdersPage();
    protected final NewOrderPage newOrderPage = new NewOrderPage();

    protected abstract String getProductName();

    protected abstract String getPlatformName();

    protected abstract String getExpectedOS();

    protected abstract String getExpectedImageOS();

    @BeforeEach()
    @Step("Авторизация на портале")
    public void beforeEach() {
        new PortalLoginPage().signInWithProjectDef(Role.PRODUCT_CATALOG_ADMIN);
    }

    protected void openOrderTemplate(String productName) {
        ordersPage
                .clickAddResource();
        newOrderPage.selectProduct(productName);
    }

    public static String generateOrderName(String productName, String platformName, String imageOS) {
        return switch (productName) {
            case ALMA, ASTRA, CENTOS, DEBIAN, UBUNTU -> generateLinuxName(productName, platformName);
            default -> throw new IllegalStateException("Unexpected value: " + productName);
        };
    }

    ;

    private static String generateLinuxName(String productName, String platformName) {
        return String.format("%s-%s-atui%s",
                productName.toLowerCase().replace(" ", "_"),
                platformName.toLowerCase(),
                secure().nextNumeric(4));
    }

}
