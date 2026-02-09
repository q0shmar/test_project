package org.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("product-catalog.viewer")
    PRODUCT_CATALOG_VIEWER("product-catalog.viewer"),

    @JsonProperty("product-catalog.admin")
    PRODUCT_CATALOG_ADMIN("product-catalog.admin"),

    @JsonProperty("superadmin")
    SUPER_ADMIN("superadmin");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
