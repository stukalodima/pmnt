package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table(name = "FINANCE_PAYMENT_REGISTER")
@Entity(name = "finance_PaymentRegister")
@NamePattern("%s %s|onDate,business")
public class PaymentRegister extends StandardEntity {
    private static final long serialVersionUID = 244328380356450374L;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "ON_DATE", nullable = false)
    private Date onDate;

    @Lookup(type = LookupType.DROPDOWN, actions = {"open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;

    @Column(name = "STATUS")
    private String status;

    @Lookup(type = LookupType.DROPDOWN, actions = {"open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "paymentRegister")
    private List<PaymentRegisterDetail> paymentRegisters;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ClaimStatusEnum getStatus() {
        return status == null ? null : ClaimStatusEnum.fromId(status);
    }

    public void setStatus(ClaimStatusEnum status) {
        this.status = status == null ? null : status.getId();
    }

    public List<PaymentRegisterDetail> getPaymentRegisters() {
        return paymentRegisters;
    }

    public void setPaymentRegisters(List<PaymentRegisterDetail> paymentRegisters) {
        this.paymentRegisters = paymentRegisters;
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
}