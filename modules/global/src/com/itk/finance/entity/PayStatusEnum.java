package com.itk.finance.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum PayStatusEnum implements EnumClass<String> {

    PAYED("PAYED"),
    PRE_PAYED("PRE_PAYED"),
    NOT_PAYED("NOT_PAYED"),
    DISMISS("DISMISS");

    private final String id;

    PayStatusEnum(String value) {
        this.id = value;
    }


    @SuppressWarnings("NullableProblems")
    public String getId() {
        return id;
    }

    @Nullable
    public static PayStatusEnum fromId(String id) {
        for (PayStatusEnum at : PayStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}