package com.itk.finance.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.entity.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Objects;
import java.util.UUID;

@Service(ProcPropertyService.NAME)
public class ProcPropertyServiceBean implements ProcPropertyService {

    private static final String QUERY_STRING_GET_PROC_STAT_BY_CODE = "select e from finance_ProcStatus e " +
            "where e.code = :code";
    private static final String QUERY_STRING_GET_PROC_STAT = "select e from finance_ProcStatus e ";

    private static final String CONDITION_IS_NEW = "where e.isNew = true";

    private static final String CONDITION_IS_START = "where e.isStart = true";
    @Inject
    private Persistence persistence;
    @Inject
    private DataManager dataManager;

    public void updateStateRegister(UUID entityId, String state) {
        try (Transaction tx = persistence.getTransaction()) {
            PaymentRegister paymentRegister = persistence.getEntityManager().find(PaymentRegister.class, entityId);
            if (!Objects.isNull(paymentRegister)) {
                paymentRegister.setStatus(getProcStatByCode(state));
                paymentRegister.getPaymentRegisters().forEach(e -> e.getPaymentClaim().setStatus(getProcStatByCode(state))
                );
            }
            tx.commit();
        }
    }

    @Override
    public ProcStatus getProcStatByCode(String code) {
        return dataManager.load(ProcStatus.class)
                .query(QUERY_STRING_GET_PROC_STAT_BY_CODE)
                .parameter("code", code)
                .view("_local")
                .one();
    }

    @Override
    public ProcStatus getNewStatus() {
        return getStatusByCondition(CONDITION_IS_NEW);
    }

    @Override
    public ProcStatus getStartStatus() {
        return getStatusByCondition(CONDITION_IS_START);
    }

    private ProcStatus getStatusByCondition(String condition) {
        return dataManager.load(ProcStatus.class)
                .query(QUERY_STRING_GET_PROC_STAT + condition)
                .view("_local")
                .one();
    }
}