package org.example.enums;

public enum OperatingSystem {
    ALMA("alma"),
    ASTRA("astra"),
    CENTOS("centos"),
    DEBIAN("debian"),
    REDOS("redos"),
    UBUNTU("ubuntu"),
    WINDOWS8("Industry Pro"),
    WINDOWS10("pro"),
    WINDOWS2012("server"),
    WINDOWS2016("server"),
    WINDOWS2019("server"),
    ZVIRT("rhel"),
    ZVIRT_4_1("rhel"),
    PROXIMA("redos"),
    ORACLE("oracle");

    private final String name;

    OperatingSystem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
