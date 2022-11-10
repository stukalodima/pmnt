package com.itk.finance.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

@Source(type = SourceType.DATABASE)
public interface ConstantsConfig extends Config {

    @Property("CONSTANTS.PAYMENT_REGISTER_STATUS_IN_PAY")
    @Default("IN_PAY")
    String getPaymentRegisterStatusInPay();

    @Property("CONSTANTS.PAYMENT_REGISTER_CONTROLLER_ROLE")
    @Default("controller")
    String getPaymentRegisterControllerRole();

    @Property("CONSTANTS.PAYMENT_REGISTER_USER_FROM_LOGIN")
    @Default("Julia.Kiryanova")
    String getUserFromLogin();

    @Property("CONSTANTS.PAYMENT_REGISTER_USER_TO_LOGIN")
    @Default("inna.nedostup")
    String getUserToLogin();

}