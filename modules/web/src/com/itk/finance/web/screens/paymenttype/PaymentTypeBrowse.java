package com.itk.finance.web.screens.paymenttype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentType;

@UiController("finance_PaymentType.browse")
@UiDescriptor("payment-type-browse.xml")
@LookupComponent("paymentTypesTable")
@LoadDataBeforeShow
public class PaymentTypeBrowse extends StandardLookup<PaymentType> {
}