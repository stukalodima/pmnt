package com.itk.finance.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.global.Secret;

@Source(type = SourceType.DATABASE)
public interface RestApiSmartPlatformConfig extends Config {
    @Property("REST-API.FINANCE-SERVICE.PAYMENT_CLAIM_LIST")
    @Default("http://172.29.5.231/kazna_shtest/hs/PaymentRegister/getPaymentClaims")
    String getPaymentClaimList();

    @Property("REST-API.FINANCE-SERVICE.USER_VALUE")
    String getFinanceServiceApiUser();

    @Property("REST-API.FINANCE-SERVICE.PASSWORD_VALUE")
    @Secret
    String getFinanceServiceApiPassword();
}