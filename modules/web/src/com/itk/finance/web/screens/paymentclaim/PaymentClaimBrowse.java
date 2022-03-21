package com.itk.finance.web.screens.paymentclaim;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentClaim;

@UiController("finance_PaymentClaim.browse")
@UiDescriptor("payment-claim-browse.xml")
@LookupComponent("paymentClaimsTable")
@LoadDataBeforeShow
public class PaymentClaimBrowse extends StandardLookup<PaymentClaim> {
}