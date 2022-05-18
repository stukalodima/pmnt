package com.itk.finance.web.screens.paymentregisterdetail;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.Objects;

@UiController("finance_PaymentRegisterDetail.edit")
@UiDescriptor("payment-register-detail-edit.xml")
@EditedEntityContainer("paymentRegisterDetailDc")
@LoadDataBeforeShow
public class PaymentRegisterDetailEdit extends StandardEditor<PaymentRegisterDetail> {
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private CollectionLoader<PaymentClaim> paymentClaimsDl;

    private void initDataLoader(Business business, Company company) {
        paymentClaimsDl.setParameter("status", ClaimStatusEnum.NEW);
        if (business != null){
            companiesDl.setParameter("business", business);
            paymentClaimsDl.setParameter("business", business);
        } else {
            companiesDl.removeParameter("business");
            paymentClaimsDl.removeParameter("business");
        }

        if (company != null) {
            paymentClaimsDl.setParameter("company", company);
        } else {
            paymentClaimsDl.removeParameter("company");
        }

        companiesDl.load();
        paymentClaimsDl.load();
    }
}