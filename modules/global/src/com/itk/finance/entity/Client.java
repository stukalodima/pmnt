package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "FINANCE_CLIENT")
@Entity(name = "finance_Client")
@NamePattern("%s [%s]|shortName,edrpou")
public class Client extends StandardEntity {
    private static final long serialVersionUID = 7519661033370406506L;

    @NotNull
    @Column(name = "SHORT_NAME", nullable = false)
    private String shortName;

    @Column(name = "NAME", nullable = false)
    @Lob
    @NotNull
    private String name;

    @NotNull
    @Column(name = "CLIENT_TYPE", nullable = false)
    private String clientType;

    @NotNull
    @Column(name = "EDRPOU", nullable = false, length = 10)
    private String edrpou;

    @Lob
    @Column(name = "ADDRESS")
    private String address;

    @Lob
    @Column(name = "KVED")
    private String kved;

    @Column(name = "BOSS")
    private String boss;

    @Column(name = "STAN")
    private String stan;

    public ClientTypeEnum getClientType() {
        return clientType == null ? null : ClientTypeEnum.fromId(clientType);
    }

    public void setClientType(ClientTypeEnum clientType) {
        this.clientType = clientType == null ? null : clientType.getId();
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getKved() {
        return kved;
    }

    public void setKved(String kved) {
        this.kved = kved;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEdrpou() {
        return edrpou;
    }

    public void setEdrpou(String edrpou) {
        this.edrpou = edrpou;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}