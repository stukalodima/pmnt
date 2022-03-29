package com.itk.finance.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum PaymentRegisterDetailStatusEnum implements EnumClass<String> {

    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    TERMINATED("TERMINATED");

    private String id;

    PaymentRegisterDetailStatusEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static PaymentRegisterDetailStatusEnum fromId(String id) {
        for (PaymentRegisterDetailStatusEnum at : PaymentRegisterDetailStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}