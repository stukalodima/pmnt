package com.itk.finance.listeners;

import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.itk.finance.entity.ClaimStatusEnum;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.service.UserPropertyService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Objects;
import java.util.UUID;

@Component("finance_PaymentClaimChangedListener")
public class PaymentClaimChangedListener {

    @Inject
    private TransactionalDataManager transactionalDataManager;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private UserSessionSource userSessionSource;

    @EventListener
    public void beforeCommit(EntityChangedEvent<PaymentClaim, UUID> event) {
//        Id<PaymentClaim, UUID> entityId = event.getEntityId();
//        PaymentClaim paymentClaim = transactionalDataManager.load(entityId).one();
//
//        if (Objects.isNull(paymentClaim.getBusiness())) {
//            paymentClaim.setBusiness(userPropertyService.getDefaultBusiness());
//        }
//
//        if (Objects.isNull(paymentClaim.getAuthor())) {
//            paymentClaim.setAuthor(userSessionSource.getUserSession().getUser());
//        }
//
//        if (Objects.isNull(paymentClaim.getStatus())) {
//            paymentClaim.setStatus(ClaimStatusEnum.NEW);
//        }
//
//        transactionalDataManager.save(paymentClaim);
    }
}