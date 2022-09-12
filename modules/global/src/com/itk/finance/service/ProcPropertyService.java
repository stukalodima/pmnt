package com.itk.finance.service;

import com.haulmont.bpm.entity.ProcDefinition;
import com.haulmont.bpm.entity.ProcRole;
import com.itk.finance.entity.ProcStatus;

import java.util.List;
import java.util.UUID;

public interface ProcPropertyService {
    String NAME = "finance_ProcPropertyService";

    void updateStateRegister(UUID entityId, String state);

    ProcStatus getProcStatByCode(String code);

    ProcStatus getNewStatus();

    ProcStatus getStartStatus();
    void sendNotificationsTask();

    List<ProcRole> getProcRolesOnDefinition(ProcDefinition procDefinition);
}