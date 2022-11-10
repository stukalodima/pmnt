package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_USER_PROPERTY")
@Entity(name = "finance_UserProperty")
@NamePattern("%s|user")
public class UserProperty extends StandardEntity {
    private static final long serialVersionUID = 5429720925864483546L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MANAGEMENT_COMPANY_ID")
    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    private ManagementCompany managementCompany;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BUSINESS_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Business business;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Company company;

    @Column(name = "DONT_SEND_EMAIL_BY_TASK")
    private Boolean dontSendEmailByTask;

    @Column(name = "DONT_SEND_EMAIL_BY_APPROVAL_RESULT")
    private Boolean dontSendEmailByApprovalResult;

    @Column(name = "SEND_NOTIFICATION_TASK")
    private Boolean sendNotificationTask;

    public Boolean getSendNotificationTask() {
        return sendNotificationTask;
    }

    public void setSendNotificationTask(Boolean sendNotificationTask) {
        this.sendNotificationTask = sendNotificationTask;
    }

    public Boolean getDontSendEmailByApprovalResult() {
        return dontSendEmailByApprovalResult;
    }

    public void setDontSendEmailByApprovalResult(Boolean dontSendEmailByApprovalResult) {
        this.dontSendEmailByApprovalResult = dontSendEmailByApprovalResult;
    }

    public Boolean getDontSendEmailByTask() {
        return dontSendEmailByTask;
    }

    public void setDontSendEmailByTask(Boolean dontSendEmailByTask) {
        this.dontSendEmailByTask = dontSendEmailByTask;
    }

    public ManagementCompany getManagementCompany() {
        return managementCompany;
    }

    public void setManagementCompany(ManagementCompany managementCompany) {
        this.managementCompany = managementCompany;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}