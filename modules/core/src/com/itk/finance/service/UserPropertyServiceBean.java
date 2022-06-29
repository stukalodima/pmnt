package com.itk.finance.service;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.ManagementCompany;
import com.itk.finance.entity.UserProperty;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Service(UserPropertyService.NAME)
public class UserPropertyServiceBean implements UserPropertyService {

    @Inject
    private DataManager dataManager;
    @Inject
    private UserSessionSource userSessionSource;

    private UserProperty getUserProperty() {
        return getUserProperty(userSessionSource.getUserSession().getUser());
    }

    private UserProperty getUserProperty(User user) {
        UserProperty userProperty = null;
        List<UserProperty> userPropertyList = dataManager.load(UserProperty.class)
                .query("select e from finance_UserProperty e where e.user = :user")
                .parameter("user", user)
                .view("userProperty.get.edit")
                .list();
        if (userPropertyList.size() > 0) {
            userProperty = userPropertyList.get(0);
        }
        return userProperty;
    }

    @Override
    public ManagementCompany getDefaultManagementCompany() {
        return (ManagementCompany) getEntityFromUserProperty(ManagementCompany.class.getSimpleName(), getUserProperty());
    }

    @Override
    public Business getDefaultBusiness() {
        return (Business) getEntityFromUserProperty(Business.class.getSimpleName(), getUserProperty());
    }

    @Override
    public Company getDefaultCompany() {
        return (Company) getEntityFromUserProperty(Company.class.getSimpleName(), getUserProperty());
    }

    @Override
    public boolean dontSendEmailByTask() {

        return dontSendEmailByTask(userSessionSource.getUserSession().getUser());
    }

    @Override
    public boolean dontSendEmailByTask(User user) {
        if (!Objects.isNull(user) && !Objects.isNull(getUserProperty(user)) && !Objects.isNull(getUserProperty(user).getDontSendEmailByTask())) {
            return getUserProperty(user).getDontSendEmailByTask();
        }
        return false;
    }

    @Override
    public boolean dontSendEmailByApprovalResult() {
        return dontSendEmailByApprovalResult(userSessionSource.getUserSession().getUser());
    }

    @Override
    public boolean dontSendEmailByApprovalResult(User user) {
        if (!Objects.isNull(user) && !Objects.isNull(getUserProperty(user)) && !Objects.isNull(getUserProperty(user).getDontSendEmailByApprovalResult())) {
            return getUserProperty().getDontSendEmailByApprovalResult();
        }
        return false;
    }

    private Object getEntityFromUserProperty(String propertyName, UserProperty userProperty) {
        Object result = null;
        switch (propertyName) {
            case "Business":
                result = Objects.isNull(userProperty) ? null : userProperty.getBusiness();
                break;
            case "Company":
                result = Objects.isNull(userProperty) ? null : userProperty.getCompany();
                break;
            case "ManagementCompany":
                result = Objects.isNull(userProperty) ? null : userProperty.getManagementCompany();
                break;
        }
        return result;
    }
}