package com.itk.finance.service;

import com.itk.finance.entity.ProcStatus;

import java.util.UUID;

public interface ProcPropertyService {
    String NAME = "finance_ProcPropertyService";

    void updateStateRegister(UUID entityId, String state);

    ProcStatus getProcStatByCode(String code);

    ProcStatus getNewStatus();

    ProcStatus getStartStatus();
}