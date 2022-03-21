package com.itk.finance.service;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.UserProperty;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(UserPropertyService.NAME)
public class UserPropertyServiceBean implements UserPropertyService {

    @Inject
    private DataManager dataManager;
    @Inject
    private UserSessionSource userSessionSource;

    private UserProperty getUserProperty() {
        UserProperty userProperty;
        try {
            userProperty = dataManager.load(UserProperty.class)
                    .query("e.user = :user")
                    .parameter("user", userSessionSource.getUserSession().getUser()).
                    one();
            userProperty = dataManager.reload(userProperty, "userProperty.get.edit");
        } catch (IllegalStateException e) {
            userProperty = null;
        }
        return userProperty;
    }

    @Override
    public Business getDefaulBusiness() {
        UserProperty userProperty = getUserProperty();
        Business business = null;
        if (userProperty != null) {
            business = userProperty.getBusiness();
        }
        return business;
    }

    @Override
    public Company getDefaulCompany() {
        UserProperty userProperty = getUserProperty();
        Company company = null;
        if (userProperty != null) {
            company = userProperty.getCompany();
        }
        return company;
    }
}