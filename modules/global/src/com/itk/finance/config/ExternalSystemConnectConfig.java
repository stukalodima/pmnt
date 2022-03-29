package com.itk.finance.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.global.Secret;

@Source(type = SourceType.DATABASE)
public interface ExternalSystemConnectConfig extends Config {
    @Property("finance.companyListUrl")
    String getCompanyListUrl();

    @Property("finance.paymentClaimListUrl")
    String getPaymentClaimListUrl();

    @Property("finance.cashFlowListUrl")
    String getCashFlowListUrl();

    @Property("finance.clientListUrl")
    String getClientListUrl();

    @Property("finance.userAuth")
    String getUserAuth();

    @Property("finance.passwordAuth")
    @Secret
    String getPasswordAuth();
}