package com.itk.finance.web.screens.paymentregister;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentRegister;

import javax.inject.Inject;

@UiController("finance_PaymentRegister.browse")
@UiDescriptor("payment-register-browse.xml")
@LookupComponent("paymentRegistersTable")
@LoadDataBeforeShow
public class PaymentRegisterBrowse extends StandardLookup<PaymentRegister> {
    @Inject
    private CollectionContainer<PaymentRegister> paymentRegistersDc;
    @Inject
    private CollectionLoader<PaymentRegister> paymentRegistersDl;

    @Install(to = "paymentRegistersTable.edit", subject = "afterCommitHandler")
    private void paymentRegistersTableEditAfterCommitHandler(PaymentRegister paymentRegister) {
        paymentRegistersDl.load();
        paymentRegistersDc.setItem(paymentRegister);
    }

}