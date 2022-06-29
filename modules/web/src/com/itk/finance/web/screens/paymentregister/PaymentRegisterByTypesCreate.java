package com.itk.finance.web.screens.paymentregister;

import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.itk.finance.entity.PaymentRegister;

import javax.inject.Inject;

@UiController("finance_PaymentRegisterByTypesCreate")
@UiDescriptor("payment-register-by-types-create.xml")
public class PaymentRegisterByTypesCreate extends Screen {
    @Inject
    private CollectionLoader<PaymentRegister> paymentRegistersDl;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        paymentRegistersDl.load();
    }
}