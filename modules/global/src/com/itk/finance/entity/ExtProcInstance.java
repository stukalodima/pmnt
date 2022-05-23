package com.itk.finance.entity;

import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "finance_ExtProcInstance")
@Extends(ProcInstance.class)
public class ExtProcInstance extends ProcInstance {
    private static final long serialVersionUID = 4061287699353023985L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_REGISTER_ID")
    private PaymentRegister paymentRegister;

    public PaymentRegister getPaymentRegister() {
        return paymentRegister;
    }

    public void setPaymentRegister(PaymentRegister paymentRegister) {
        this.paymentRegister = paymentRegister;
    }
}