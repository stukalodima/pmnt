package com.itk.finance.core;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.itk.finance.entity.ClaimStatusEnum;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.PaymentRegister;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

@Component(ApprovalHelperBean.NAME)
public class ApprovalHelperBean {
    public static final String NAME = "finance_ApprovalHelperBean";

    @Inject
    private Persistence persistence;

    public void updateState(UUID entityId, String state) {
        try (Transaction tx = persistence.getTransaction()) {
            PaymentClaim paymentClaim = persistence.getEntityManager().find(PaymentClaim.class, entityId);
            if (paymentClaim != null) {
                paymentClaim.setStatus(ClaimStatusEnum.fromId(state));
            }
            tx.commit();
        }
    }
    public void updateStateRegister(UUID entityId, String state) {
        try (Transaction tx = persistence.getTransaction()) {
            PaymentRegister paymentRegister = persistence.getEntityManager().find(PaymentRegister.class, entityId);
            if (paymentRegister != null) {
                paymentRegister.setStatus(ClaimStatusEnum.fromId(state));
            }
            tx.commit();
        }
    }
}