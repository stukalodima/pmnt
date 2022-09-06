package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.service.ProcPropertyService;
import com.itk.finance.service.UserPropertyService;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@PublishEntityChangedEvents
@Table(name = "FINANCE_PAYMENT_CLAIM")
@Entity(name = "finance_PaymentClaim")
@NamePattern("%s %s %s|onDate,client,summ")
public class PaymentClaim extends StandardEntity {
    private static final long serialVersionUID = 8918063338854500391L;

    @Column(name = "NUMBER_")
    private Long number;

    @Column(name = "DOC_NUMBER")
    private String docNumber;

    @Column(name = "CLIENT_ACCOUNT")
    private String clientAccount;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "ON_DATE")
    private Date onDate;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;

    @Column(name = "SUMM")
    @NotNull
    private Double summ;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "PLAN_PAYMENT_DATE")
    private Date planPaymentDate;

    @NotNull
    @Lob
    @Column(name = "PAYMENT_PURPOSE")
    private String paymentPurpose;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CASH_FLOW_ITEM_ID")
    @NotNull
    private CashFlowItem cashFlowItem;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CASH_FLOW_ITEM_BUSINESS_ID")
    private CashFlowItemBusiness cashFlowItemBusiness;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_TYPE_ID")
    @NotNull
    private PaymentType paymentType;

    @Lob
    @Column(name = "COMMENT_")
    private String comment;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @JoinColumn(name = "STATUS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    private ProcStatus status;

    @Column(name = "EXPRESS")
    private Boolean express;

    @Lob
    @Column(name = "BUDGET_ANALITIC")
    private String budgetAnalitic;

    public String getBudgetAnalitic() {
        return budgetAnalitic;
    }

    public void setBudgetAnalitic(String budgetAnalitic) {
        this.budgetAnalitic = budgetAnalitic;
    }

    public Boolean getExpress() {
        return express;
    }

    public void setExpress(Boolean express) {
        this.express = express;
    }

    public void setStatus(ProcStatus status) {
        this.status = status;
    }

    public ProcStatus getStatus() {
        return status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public CashFlowItemBusiness getCashFlowItemBusiness() {
        return cashFlowItemBusiness;
    }

    public void setCashFlowItemBusiness(CashFlowItemBusiness cashFlowItemBusiness) {
        this.cashFlowItemBusiness = cashFlowItemBusiness;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getPlanPaymentDate() {
        return planPaymentDate;
    }

    public void setPlanPaymentDate(Date planPaymentDate) {
        this.planPaymentDate = planPaymentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public CashFlowItem getCashFlowItem() {
        return cashFlowItem;
    }

    public void setCashFlowItem(CashFlowItem cashFlowItem) {
        this.cashFlowItem = cashFlowItem;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    @PostConstruct
    public void initEntity(Metadata metadata) {
        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
        UserPropertyService userPropertyService = AppBeans.get(UserPropertyService.class);
        ProcPropertyService procPropertyService = AppBeans.get(ProcPropertyService.class);
        TimeSource timeSource = AppBeans.get(TimeSource.class);
        author = userSessionSource.getUserSession().getUser();
        business = userPropertyService.getDefaultBusiness();
        company = userPropertyService.getDefaultCompany();
        status = procPropertyService.getNewStatus();
        onDate = timeSource.currentTimestamp();
        planPaymentDate = new Date(timeSource.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(String clientAccount) {
        this.clientAccount = clientAccount;
    }
}