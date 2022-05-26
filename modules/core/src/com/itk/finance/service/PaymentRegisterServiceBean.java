package com.itk.finance.service;

import com.haulmont.cuba.core.global.Metadata;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.entity.PaymentRegisterDetail;
import com.itk.finance.entity.PaymentRegisterDetailStatusEnum;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service(PaymentRegisterService.NAME)
public class PaymentRegisterServiceBean implements PaymentRegisterService {

    @Inject
    private Metadata metadata;

    @Override
    public List<PaymentRegisterDetail> getPaymentRegisterDetailsByPaymentClaimList(PaymentRegister paymentRegister, List<PaymentClaim> paymentClaimList) {
        List<PaymentRegisterDetail> listDetail = new ArrayList<>();
        paymentClaimList.forEach(e -> listDetail.add(createPaymentRegisterDetail(e, paymentRegister)));
        return listDetail;
    }

    private PaymentRegisterDetail createPaymentRegisterDetail(PaymentClaim paymentClaim, PaymentRegister paymentRegister) {
        PaymentRegisterDetail paymentRegisterDetail = metadata.create(PaymentRegisterDetail.class);
        paymentRegisterDetail.setApproved(PaymentRegisterDetailStatusEnum.APPROVED);
        paymentRegisterDetail.setPaymentClaim(paymentClaim);
        paymentRegisterDetail.setPaymentRegister(paymentRegister);
        //return dataContext.merge(paymentRegisterDetail);
        return (paymentRegisterDetail);
    }
}