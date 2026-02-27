package org.example;

import extentions.BaseTest;
import io.qameta.allure.Step;
import org.example.enums.Role;
import org.example.testpages.NewOrderPage;
import org.example.testpages.OrdersPage;
import org.example.testpages.PortalLoginPage;
import org.example.testpages.TemplateOrderPage;
import org.junit.jupiter.api.BeforeEach;

import static org.apache.commons.lang3.RandomStringUtils.secure;
import static org.example.ProductName.*;

public abstract class AbstractOrder extends BaseTest {
    protected final OrdersPage ordersPage = new OrdersPage();
    protected final NewOrderPage newOrderPage = new NewOrderPage();
    protected final TemplateOrderPage templateOrderPage = new TemplateOrderPage();

    protected final ThreadLocal<String> threadOrderName = ThreadLocal.withInitial(() -> null);

    protected abstract String getProductName();

    protected abstract String getPlatformName();

    protected abstract String getExpectedOS();

    protected abstract String getExpectedImageOS();

    public void createOrder(boolean createARecord){
        var orderName=generateOrderName(getProductName(),getPlatformName(),getExpectedImageOS());
        setOrderName(orderName);
        openOrderTemplate(getProductName());
        setOrderTemplate(orderName,getPlatformName(),getProductName(),getExpectedImageOS(),createARecord);
    }

    @BeforeEach()
    @Step("Авторизация на портале")
    public void beforeEach() {
        new PortalLoginPage().signInWithProjectDef(Role.PRODUCT_CATALOG_ADMIN);
    }

    protected void setOrderName(String orderName) {
        threadOrderName.set(orderName);
    }

    protected void openOrderTemplate(String productName) {
        ordersPage
                .clickAddResource();
        newOrderPage.selectProduct(productName);
    }

    public static String generateOrderName(String productName, String platformName, String imageOS) {
        return switch (productName) {
            case ALMA, ASTRA, CENTOS, DEBIAN, UBUNTU -> generateLinuxName(productName, platformName);
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(productName));
        };
    }

    ;

    private static String generateLinuxName(String productName, String platformName) {
        return String.format("%s-%s-atui%s",
                productName.toLowerCase().replace(" ", "_"),
                platformName.toLowerCase(),
                secure().nextNumeric(4));
    }
    protected void setOrderTemplate(String orderName, String platform, String productName, String imageOS, boolean createARecord) {
        // Выбор шаблона в зависимости от типа продукта
        switch (productName) {
            case ASTRA, ALMA, CENTOS, DEBIAN, RED_OS, UBUNTU, ORACLE_LINUX,LOKI ->
                    templateOrderPage.createSimpleProductTemplate(orderName, platform, imageOS, createARecord);
//            case WINDOWS_8, WINDOWS_10, WINDOWS_2012, WINDOWS_2016 ->
//                    templateOrderPage.setOrderFormForWindows(orderName, platform, createARecord);
//            case WINDOWS -> templateOrderPage.setOrderFormForWindows2019(orderName, platform, createARecord);
//            case APACHE_KAFKA, RABBIT_MQ, CLICK_HOUSE, MY_SQL, ELASTIC_SEARCH, MONGO_DB, POSTGRE_SQL, REDIS,
//                 TANTOR_BE ->
//                    templateOrderPage.setOrderFormForComplexProduct(orderName, platform, imageOS, createARecord);
//            case NGINX, WILDFLY ->
//                    templateOrderPage.setOrderFormWebProduct(orderName, platform, imageOS, createARecord);
//            case KUBERNETES_CLUSTER ->
//                    templateOrderPage.setOrderFormConteinerProduct(orderName, platform, imageOS, createARecord);
//            case ARENADATA_PROSPERITY ->
//                    templateOrderPage.setOrderFormContainerProxima(orderName, platform, imageOS, createARecord);
//            case NOVA_CLUSTER -> templateOrderPage.setOrderFormContainerNova(orderName, platform, imageOS);
//            case ZVIRT -> templateOrderPage.setOrderFormForZvirt(orderName, platform);
//            case STARVAULT -> templateOrderPage.setOrderFormForStarVault(orderName, platform, imageOS, createARecord);
//            case MINIO -> templateOrderPage.setOrderFormForMinIO(orderName, platform, imageOS);
            default -> throw new IllegalArgumentException("Неизвестный продукт: %s".formatted(productName));
        }
    }


}
