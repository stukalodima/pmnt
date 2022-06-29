package com.itk.finance.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.service.ProcPropertyService;
import com.itk.finance.service.UserPropertyService;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Table(name = "FINANCE_PAYMENT_REGISTER")
@Entity(name = "finance_PaymentRegister")
@Listeners("finance_PaymentRegisterEntityListener")
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
        if (Objects.isNull(summa)) {
            this.summa = calcSummaPaymentClaim(new HashSet<>(paymentRegisters));
        } else {
            this.summa = summa;
        }
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

    @PostConstruct
    public void init() {
        TimeSource timeSource = AppBeans.get(TimeSource.class);
        UserSession userSession = AppBeans.get(UserSession.class);
        UserPropertyService userPropertyService = AppBeans.get(UserPropertyService.class);
        ProcPropertyService procPropertyService = AppBeans.get(ProcPropertyService.class);
        onDate = timeSource.currentTimestamp();
        business = userPropertyService.getDefaultBusiness();
        author = userSession.getUser();
        status = procPropertyService.getNewStatus();
    }

    public String calcSummaPaymentClaim(Set<PaymentRegisterDetail> paymentRegisterDetails) {
        DataManager dataManager = AppBeans.get(DataManager.class);
        Map<String, Double> mapSumma = new HashMap<>();
        paymentRegisterDetails.forEach(e -> {
            PaymentClaim paymentClaim = dataManager.reload(e.getPaymentClaim(), "paymentClaim.getEdit");
            String key = paymentClaim.getCurrency().getShortName();
            double value = paymentClaim.getSumm();
            if (mapSumma.containsKey(key)) {
                mapSumma.replace(key, mapSumma.get(key) + value);
            } else {
                mapSumma.put(key, value);
            }
        });
        StringBuilder text = new StringBuilder();
        mapSumma.forEach((key, value) -> {
                    if (!text.toString().equals("")) {
                        text.append("; ");
                    }
                    text.append(
                                    String.format("%,.2f", value)
                                            .replace(",", " ")
                                            .replace(".", ",")
                            )
                            .append(" ")
                            .append(key);
                }
        );
        return text.toString();
    }
}