package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;

@Table(name = "FINANCE_PAYMENT_REGISTER_DETAIL")
@Entity(name = "finance_PaymentRegisterDetail")
@NamePattern("%s %s|paymentRegister,paymentClaim")
public class PaymentRegisterDetail extends StandardEntity {
    private static final long serialVersionUID = -2688930667796502490L;

    @Column(name = "APPROVED")
    private String approved;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_CLAIM_ID")
    private PaymentClaim paymentClaim;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAYMENT_REGISTER_ID")
    private PaymentRegister paymentRegister;

    @Column(name = "PAYMENT_STATUS_ROW")
    private String paymentStatusRow;

    @Lob
    @Column(name = "COMMENT_")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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

    public ClaimStatusEnum getPaymentStatusRow() {
        return paymentStatusRow == null ? null : ClaimStatusEnum.fromId(paymentStatusRow);
    }

    public void setPaymentStatusRow(ClaimStatusEnum paymentStatusRow) {
        this.paymentStatusRow = paymentStatusRow == null ? null : paymentStatusRow.getId();
    }
}