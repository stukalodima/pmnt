package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_BUSINESS")
@Entity(name = "finance_Business")
@NamePattern("%s|name")
public class Business extends StandardEntity {
    private static final long serialVersionUID = -1722208945367473874L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_CONTROLER_ID")
    private User finControler;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_DIRECTOR_ID")
    private User finDirector;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GEN_DIRECTOR_ID")
    private User genDirector;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_CONTROLER_SH_ID")
    private User finControlerSH;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GEN_DIRECTOR_SH_ID")
    private User finDirectorSH;

    @Column(name = "USE_PAYMANT_CLAIM")
    private Boolean usePaymantClaim;

    @Column(name = "USE_PAYMENT_CLAIM_APPROVAL")
    private Boolean usePaymentClaimApproval;

    public Boolean getUsePaymentClaimApproval() {
        return (usePaymentClaimApproval != null && usePaymentClaimApproval);
    }

    public void setUsePaymentClaimApproval(Boolean usePaymentClaimApproval) {
        this.usePaymentClaimApproval = usePaymentClaimApproval;
    }

    public Boolean getUsePaymantClaim() {
        return (usePaymantClaim != null && usePaymantClaim);
    }

    public void setUsePaymantClaim(Boolean usePatmantClaim) {
        this.usePaymantClaim = usePatmantClaim;
    }

    public User getFinDirectorSH() {
        return finDirectorSH;
    }

    public void setFinDirectorSH(User genDirectorSH) {
        this.finDirectorSH = genDirectorSH;
    }

    public User getFinControlerSH() {
        return finControlerSH;
    }

    public void setFinControlerSH(User finControlerSH) {
        this.finControlerSH = finControlerSH;
    }

    public User getGenDirector() {
        return genDirector;
    }

    public void setGenDirector(User genDirector) {
        this.genDirector = genDirector;
    }

    public User getFinDirector() {
        return finDirector;
    }

    public void setFinDirector(User finDirector) {
        this.finDirector = finDirector;
    }

    public User getFinControler() {
        return finControler;
    }

    public void setFinControler(User finControler) {
        this.finControler = finControler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}