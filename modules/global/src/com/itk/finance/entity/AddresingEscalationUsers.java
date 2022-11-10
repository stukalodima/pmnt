package com.itk.finance.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;

@Table(name = "FINANCE_ADDRESING_ESCALATION_USERS")
@Entity(name = "finance_AddresingEscalationUsers")
public class AddresingEscalationUsers extends StandardEntity {
    private static final long serialVersionUID = 3080809600651312558L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESING_DETAIL_ID")
    private AddressingDetail addressingDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public AddressingDetail getAddressingDetail() {
        return addressingDetail;
    }

    public void setAddressingDetail(AddressingDetail addressingDetail) {
        this.addressingDetail = addressingDetail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}