package com.itk.finance.listeners;

import com.haulmont.cuba.core.app.events.EntityPersistingEvent;
import com.itk.finance.entity.PaymentClaim;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component(PaymentClaimListener.NAME)
public class PaymentClaimListener {
    public static final String NAME = "finance_PaymentClaimListener";

    @EventListener
    public void onPaymentClaimBeforePersist(EntityPersistingEvent<PaymentClaim> event) {
        PaymentClaim paymentClaim = event.getEntity();
        if(paymentClaim.getDocNumber()==null) {
            paymentClaim.setDocNumber(paymentClaim.getNumber().toString());
        }
    }
    
}