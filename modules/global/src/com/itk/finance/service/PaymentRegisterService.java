package com.itk.finance.service;

import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.entity.PaymentRegisterDetail;

import java.util.List;

public interface PaymentRegisterService {
    String NAME = "finance_PaymentRegisterService";

    List<PaymentRegisterDetail> getPaymentRegisterDetailsByPaymentClaimList(PaymentRegister paymentRegister, List<PaymentClaim> paymentClaimList);
}