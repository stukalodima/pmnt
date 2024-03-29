package com.itk.finance.service;

import com.itk.finance.entity.Business;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.RegisterType;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface PaymentClaimService {
    String NAME = "finance_PaymentClaimService";

    void getPaymentClaimListFromExternal() throws IOException, ParseException;

    PaymentClaim getPaymentClaimById(String id);
    PaymentClaim getPaymentClaimById(String id, boolean softDeletion);

    PaymentClaim getPaymentClaimById(UUID id);
    PaymentClaim getPaymentClaimById(UUID id, boolean softDeletion);

    List<PaymentClaim> getPaymentClaimsListByRegister(Business business, RegisterType registerType);

    void fillSumaInUahByAllDocuments();
}