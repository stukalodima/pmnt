package com.itk.finance.web.screens.paymenttype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentType;

@UiController("finance_PaymentType.edit")
@UiDescriptor("payment-type-edit.xml")
@EditedEntityContainer("paymentTypeDc")
@LoadDataBeforeShow
public class PaymentTypeEdit extends StandardEditor<PaymentType> {
}