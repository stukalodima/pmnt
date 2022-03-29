package com.itk.finance.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ClientTypeEnum implements EnumClass<String> {

    JUR_OSOBA("JUR_OSOBA"),
    FIZ_OSOBA("FIZ_OSOBA");

    private String id;

    ClientTypeEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ClientTypeEnum fromId(String id) {
        for (ClientTypeEnum at : ClientTypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}