package com.itk.finance.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ClaimStatusEnum implements EnumClass<String> {

    NEW("NEW"),
    APPROVED_BN("APPROVED_BN"),
    APPROVED_SH("APPROVED_SH"),
    REJECTED_SH("REJECTED_SH");

    private String id;

    ClaimStatusEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ClaimStatusEnum fromId(String id) {
        for (ClaimStatusEnum at : ClaimStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}