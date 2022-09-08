package com.itk.finance.web.screens.paymentregisterdetail;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentRegisterDetail;

@UiController("finance_PaymentRegisterDetail.edit")
@UiDescriptor("payment-register-detail-edit.xml")
@EditedEntityContainer("paymentRegisterDetailDc")
@LoadDataBeforeShow
public class PaymentRegisterDetailEdit extends StandardEditor<PaymentRegisterDetail> {
}