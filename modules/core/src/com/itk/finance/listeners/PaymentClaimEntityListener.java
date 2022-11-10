package com.itk.finance.listeners;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.service.CurrencyService;
import org.springframework.stereotype.Component;

@Component(PaymentClaimEntityListener.NAME)
public class PaymentClaimEntityListener implements
        BeforeUpdateEntityListener<PaymentClaim>,
        BeforeInsertEntityListener<PaymentClaim> {
    public static final String NAME = "finance_PaymentClaimEntityListener";

    @SuppressWarnings("NullableProblems")
    @Override
    public void onBeforeUpdate(PaymentClaim entity, EntityManager entityManager) {
        fillSumaInUah(entity);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onBeforeInsert(PaymentClaim entity, EntityManager entityManager) {
        fillSumaInUah(entity);
    }

    private static void fillSumaInUah(PaymentClaim entity) {
        if (entity.getCurrency() != null && entity.getOnDate() != null && entity.getSumm() != null) {
            CurrencyService currencyService = AppBeans.get(CurrencyService.class);
            entity.setSumaInUah(currencyService.getSumaInUahByCurrency(entity.getCurrency(), entity.getOnDate(), entity.getSumm()));
        }
    }
}