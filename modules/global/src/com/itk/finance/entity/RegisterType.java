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

@Table(name = "FINANCE_REGISTER_TYPE")
@Entity(name = "finance_RegisterType")
@NamePattern("%s|name")
public class RegisterType extends StandardEntity {
    private static final long serialVersionUID = 7482415433633961367L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EXPRESS")
    private Boolean express;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROC_DEFINITION_ID")
    private ProcDefinition procDefinition;

    @Column(name = "USE_CONDITION")
    private Boolean useCondition;

    @Column(name = "CONDITION_")
    private String condition;

    @Column(name = "CONDITION_VALUE")
    private Double conditionValue;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "registerType")
    private List<RegisterTypeDetail> registerTypeDetails;

    public Boolean getExpress() {
        return express;
    }

    public void setExpress(Boolean express) {
        this.express = express;
    }

    public Double getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Double conditionValue) {
        this.conditionValue = conditionValue;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(Boolean useCondition) {
        this.useCondition = useCondition;
    }

    public ProcDefinition getProcDefinition() {
        return procDefinition;
    }

    public void setProcDefinition(ProcDefinition procDefinition) {
        this.procDefinition = procDefinition;
    }

    public List<RegisterTypeDetail> getRegisterTypeDetails() {
        return registerTypeDetails;
    }

    public void setRegisterTypeDetails(List<RegisterTypeDetail> registerTypeDetails) {
        this.registerTypeDetails = registerTypeDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}