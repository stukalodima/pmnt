package com.itk.finance.web.screens.paymentregisterdetail;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentRegisterDetail;

@UiController("finance_PaymentRegisterDetail.browse")
@UiDescriptor("payment-register-detail-browse.xml")
@LookupComponent("paymentRegisterDetailsTable")
@LoadDataBeforeShow
public class PaymentRegisterDetailBrowse extends StandardLookup<PaymentRegisterDetail> {
}