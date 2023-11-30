package io.github.kismet2399.common;

import lombok.Getter;

/**
 * 应用类型
 *
 * @author kismet
 */
@Getter
public enum AppEnum {

    /**
     * spring应用名
     */
    DEFAULT("spring应用名");

    private String name;

    AppEnum(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
