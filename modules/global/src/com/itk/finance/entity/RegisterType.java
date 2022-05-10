package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "FINANCE_REGISTER_TYPE")
@Entity(name = "finance_RegisterType")
@NamePattern("%s|name")
public class RegisterType extends StandardEntity {
    private static final long serialVersionUID = 7482415433633961367L;

    @NotNull
    @Column(name = "NUMBER_", nullable = false)
    private Integer number;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "registerType")
    private List<RegisterTypeDetail> registerTypeDetails;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}