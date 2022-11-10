package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "FINANCE_PAYMENT_REGISTER_DETAIL")
@Entity(name = "finance_PaymentRegisterDetail")
@NamePattern("%s %s|paymentRegister,paymentClaim")
public class PaymentRegisterDetail extends StandardEntity {
    private static final long serialVersionUID = -2688930667796502490L;

    @Column(name = "APPROVED")
    private String approved;

    @Column(name = "PAYED")
    private String payed;

    @Column(name = "SUMA_TO_PAY")
    private Double sumaToPay;

    @Column(name = "PAYED_DATE")
    private Date payedDate;

    @Column(name = "PAYED_SUMA")
    private Double payedSuma;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_CLAIM_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private PaymentClaim paymentClaim;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAYMENT_REGISTER_ID")
    private PaymentRegister paymentRegister;

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

    public PayStatusEnum getPayed() {
        return payed == null ? null : PayStatusEnum.fromId(payed);
    }

    public void setPayed(PayStatusEnum payed) {
        this.payed = payed == null ? null : payed.getId();
    }

    public Double getSumaToPay() {
        return sumaToPay;
    }

    public void setSumaToPay(Double sumaToPay) {
        this.sumaToPay = sumaToPay;
    }

    public Date getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(Date payedDate) {
        this.payedDate = payedDate;
    }

    public Double getPayedSuma() {
        return payedSuma;
    }

    public void setPayedSuma(Double payedSuma) {
        this.payedSuma = payedSuma;
    }
}