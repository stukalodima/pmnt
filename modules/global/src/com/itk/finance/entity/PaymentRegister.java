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
@NamePattern("%s %s [%s]|number,onDate,business")
public class PaymentRegister extends StandardEntity {
    private static final long serialVersionUID = 244328380356450374L;

    @Column(name = "NUMBER_")
    private Long number;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "ON_DATE", nullable = false)
    private Date onDate;

    @Lookup(type = LookupType.DROPDOWN, actions = {"open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BUSINESS_ID")
    private Business business;

    @JoinColumn(name = "STATUS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    private ProcStatus status;

    @Lookup(type = LookupType.DROPDOWN, actions = {"open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGISTER_TYPE_ID")
    private RegisterType registerType;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "paymentRegister")
    private List<PaymentRegisterDetail> paymentRegisters;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROC_INSTANCE_ID")
    private ExtProcInstance procInstance;

    @Column(name = "SUMMA")
    private String summa;

    public String getSumma() {
        return summa;
    }

    public void setSumma(String summa) {
        this.summa = summa;
    }

    public void setProcInstance(ExtProcInstance procInstance) {
        this.procInstance = procInstance;
    }

    public ExtProcInstance getProcInstance() {
        return procInstance;
    }

    public void setStatus(ProcStatus status) {
        this.status = status;
    }

    public ProcStatus getStatus() {
        return status;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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