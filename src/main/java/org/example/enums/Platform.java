package org.example.enums;

import lombok.Getter;

@Getter
public enum Platform {
    OPENSTACK("OpenStack"),
    REDVIRT("RedVirt"),
    HYPER_V("Hyper-V"),
    VSPHERE("vSphere"),
    VSPHERE_67("vSphere67"),
    VSPHERE_80("vSphere80"),
    YANDEX_CLOUD("Yandex Cloud"),
    K2CLOUD("K2Cloud"),
    ZVIRT("zVirt");

    private final String name;

    Platform(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Platform fromString(String name) {
        for (Platform platform : Platform.values()) {
            if (platform.getName().equalsIgnoreCase(name)) {
                return platform;
            }
        }
        throw new IllegalArgumentException("Неизвестная платформа: %s".formatted(name));
    }
}
