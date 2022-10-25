package com.itk.finance.service;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.itk.finance.entity.Business;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(BusinessService.NAME)
public class BusinessServiceBean implements BusinessService {

    @Override
    public List<Business> getGlBusiness() {
        List<Business> resultList;
        final String sqlString = "select e from finance_Business e where e.parent.id = e.id";
        Persistence persistence = AppBeans.get(Persistence.class);
        try (Transaction transaction = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            resultList = entityManager.createQuery(sqlString, Business.class).getResultList();
            transaction.commit();
        }
        return resultList;
    }

}