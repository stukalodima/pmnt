package com.itk.finance.web.screens.paymentregister;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.PaymentRegister;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@UiController("finance_PaymentRegister.browse")
@UiDescriptor("payment-register-browse.xml")
@LookupComponent("paymentRegistersTable")
@LoadDataBeforeShow
public class PaymentRegisterBrowse extends StandardLookup<PaymentRegister> {
    @Inject
    private CollectionContainer<PaymentRegister> paymentRegistersDc;
    @Inject
    private CollectionLoader<PaymentRegister> paymentRegistersDl;
    @Inject
    private DataManager dataManager;

    @Install(to = "paymentRegistersTable.edit", subject = "afterCommitHandler")
    private void paymentRegistersTableEditAfterCommitHandler(PaymentRegister paymentRegister) {
        paymentRegistersDl.load();
        paymentRegistersDc.setItem(paymentRegister);
    }

    @Subscribe("paymentRegistersTable.createAllRegisterByType")
    public void onPaymentRegistersTableCreateAllRegisterByType(Action.ActionPerformedEvent event) {

    }
}