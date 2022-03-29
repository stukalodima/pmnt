package com.itk.finance.service;

import com.itk.finance.entity.PaymentClaim;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

public interface PaymentClaimService {
    String NAME = "finance_PaymentClaimService";

    void getPaymentClaimListfromExternal() throws IOException, ParseException;
    PaymentClaim getPaimentClaimById(String id);
    PaymentClaim getPaimentClaimById(UUID id);
}