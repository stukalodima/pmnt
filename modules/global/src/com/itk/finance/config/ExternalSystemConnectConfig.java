package com.itk.finance.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.global.Secret;

@Source(type = SourceType.DATABASE)
public interface ExternalSystemConnectConfig extends Config {
    @Property("finance.companyListUrl")
    @Default("http://localhost:7080/pmnt/VAADIN/company.json")
    String getCompanyListUrl();

    @Property("finance.paymentClaimListUrl")
    @Default("http://localhost:7080/pmnt/VAADIN/paymentClaim.json")
    String getPaymentClaimListUrl();

    @Property("finance.cashFlowListUrl")
    @Default("http://localhost:7080/pmnt/VAADIN/cashFlowItems.json")
    String getCashFlowListUrl();

    @Property("finance.clientListUrl")
    @Default("http://localhost:7080/pmnt/VAADIN/CLIENT_EDR_UO.xml")
    String getClientListUrl();
    @Property("finance.companyAccounts")
    @Default("http://localhost:7080/pmnt/VAADIN/accounts.json")
    String getCompanyAccounts();

    @Property("finance.userAuth")
    String getUserAuth();

    @Property("finance.passwordAuth")
    @Secret
    String getPasswordAuth();
}