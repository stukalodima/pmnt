package com.itk.finance.entity;

import com.haulmont.bpm.entity.ProcDefinition;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "FINANCE_ADDRESSING")
@Entity(name = "finance_Addressing")
@NamePattern("%s [%s]|bussines,procDefinition")
public class Addressing extends StandardEntity {
    private static final long serialVersionUID = 7196136610640373131L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BUSSINES_ID")
    private Business bussines;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROC_DEFINITION_ID")
    private ProcDefinition procDefinition;

    @Column(name = "USE_COMPANY")
    private Boolean useCompany;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "addressing")
    private List<AddressingDetail> addressingDetail;

    public Boolean getUseCompany() {
        return useCompany;
    }

    public void setUseCompany(Boolean useCompany) {
        this.useCompany = useCompany;
    }

    public List<AddressingDetail> getAddressingDetail() {
        return addressingDetail;
    }

    public void setAddressingDetail(List<AddressingDetail> addressingDetail) {
        this.addressingDetail = addressingDetail;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ProcDefinition getProcDefinition() {
        return procDefinition;
    }

    public void setProcDefinition(ProcDefinition procDefinition) {
        this.procDefinition = procDefinition;
    }

    public Business getBussines() {
        return bussines;
    }

    public void setBussines(Business bussines) {
        this.bussines = bussines;
    }
}