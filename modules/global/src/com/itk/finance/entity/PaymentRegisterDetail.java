package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_PAYMENT_REGISTER_DETAIL")
@Entity(name = "finance_PaymentRegisterDetail")
@NamePattern("%s %s|client,summ")
public class PaymentRegisterDetail extends StandardEntity {
    private static final long serialVersionUID = -2688930667796502490L;

    @Column(name = "APPROVED")
    private String approved;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @NotNull
    @Column(name = "SUMM", nullable = false)
    private Double summ;

    @NotNull
    @Lob
    @Column(name = "PAYMENT_PURPOSE", nullable = false)
    private String paymentPurpose;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CASH_FLOW_ITEM_ID")
    private CashFlowItem cashFlowItem;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAYMENT_TYPE_ID")
    private PaymentType paymentType;

    @Lob
    @Column(name = "COMMENT_")
    private String comment;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_CLAIM_ID")
    private PaymentClaim paymentClaim;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAYMENT_REGISTER_ID")
    private PaymentRegister paymentRegister;

    public void setApproved(PaymentRegisterDetailStatusEnum approved) {
        this.approved = approved == null ? null : approved.getId();
    }

    public PaymentRegisterDetailStatusEnum getApproved() {
        return approved == null ? null : PaymentRegisterDetailStatusEnum.fromId(approved);
    }

    public PaymentClaim getPaymentClaim() {
        return paymentClaim;
    }

    public void setPaymentClaim(PaymentClaim paymentClaim) {
        this.paymentClaim = paymentClaim;
    }

    public PaymentRegister getPaymentRegister() {
        return paymentRegister;
    }

    public void setPaymentRegister(PaymentRegister paymentRegister) {
        this.paymentRegister = paymentRegister;
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
}