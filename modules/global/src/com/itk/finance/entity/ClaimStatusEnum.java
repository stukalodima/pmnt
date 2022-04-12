package com.itk.finance.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ClaimStatusEnum implements EnumClass<String> {

    NEW("NEW"),
    IN_APPROVE("IN_APPROVE"),
    APPROVED_FIN_CONTROLER("APPROVED_FIN_CONTROLER"),
    REJECTED_FIN_CONTROLER("REJECTED_FIN_CONTROLER"),
    APPROVED_FIN_DIR("APPROVED_FIN_DIR"),
    REJECTED_FIN_DIR("REJECTED_FIN_DIR");

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