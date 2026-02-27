package org.example.tests.product_tests;

import org.example.AbstractOrder;
import org.example.enums.OperatingSystem;
import org.example.enums.Platform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.example.ProductName.ALMA;

public class AlmaTest extends AbstractOrder {
    @Override
    protected String getProductName() {
        return ALMA;
    }

    @Override
    protected String getPlatformName() {
        return Platform.HYPER_V.getName();
    }

    @Override
    protected String getExpectedOS() {
        return OperatingSystem.ALMA.toString();
    }

    @Override
    protected String getExpectedImageOS() {
        return null;
    }
    @Test
    @DisplayName("Заказ продукта")
    public void createOrderTest(){
        super.createOrder(false);
    }
}
