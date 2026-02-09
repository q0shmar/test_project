package org.example.enums;

import lombok.Getter;

@Getter
public enum ProductCategory {
    БАЗОВЫЕ_ВЫЧИСЛЕНИЯ ("Базовые вычисления"),
    БАЗЫ_ДАННЫХ("Базы данных"),
    ВЕБ_ПРИЛОЖЕНИЯ("Веб-приложения"),
    БРОКЕРЫ_СООБЩЕНИЙ("Брокеры сообщений"),
    ;
    private final String categoryName;

    ProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
