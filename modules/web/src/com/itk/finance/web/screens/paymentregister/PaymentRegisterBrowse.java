package com.itk.finance.web.screens.paymentregister;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentRegister;

@UiController("finance_PaymentRegister.browse")
@UiDescriptor("payment-register-browse.xml")
@LookupComponent("paymentRegistersTable")
@LoadDataBeforeShow
public class PaymentRegisterBrowse extends StandardLookup<PaymentRegister> {
}