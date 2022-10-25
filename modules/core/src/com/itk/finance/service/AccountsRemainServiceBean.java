package com.itk.finance.service;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.itk.finance.entity.Bank;
import com.itk.finance.entity.BankGroup;
import com.itk.finance.entity.Business;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(AccountsRemainService.NAME)
public class AccountsRemainServiceBean implements AccountsRemainService {
    @Override
    public Double getAccountsRemainOnDate(Date onDate) {
        Double result;
        final String sqlString = "select sum(e.summEqualsUAH) " +
                "from finance_AccountRemains e " +
                "where e.summEqualsUAH > 0 " +
                "and e.onDate = (" +
                "select max(rm.onDate) " +
                "from finance_AccountRemains rm " +
                "where rm.account = e.account " +
                "and rm.onDate <= :onDate)";
        Persistence persistence = AppBeans.get(Persistence.class);
        try (Transaction transaction = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            result = (Double) entityManager.createQuery(sqlString).setParameter("onDate", onDate).getFirstResult();
            transaction.commit();
        }
        return result != null ? result : 0.;
    }

    @Override
    public Map<Business, Double> getAccountsRemainByGlBusiness(Date onDate) {
        Map<Business, Double> resultMap = new HashMap<>();
        final String sqlString = "select bn.parent as business, sum(e.summEqualsUAH) as suma " +
                "from finance_AccountRemains e " +
                "join e.account acc " +
                "join acc.company comp " +
                "join comp.business bn " +
                "where e.summEqualsUAH > 0 " +
                "and e.onDate = (" +
                "select max(rm.onDate) " +
                "from finance_AccountRemains rm " +
                "where rm.account = e.account " +
                "and rm.onDate <= :onDate) " +
                "group by bn.parent";

        @SuppressWarnings("rawtypes")
        List list = getListByRunQuery(onDate, sqlString);

        for (Object o : list) {
            Object[] row = (Object[]) o;
            Business business = (Business) row[0];
            Double suma = (Double) row[1];
            resultMap.put(business, suma);
        }
        return resultMap;
    }

    @Override
    public Map<BankGroup, Map<Bank, Map<Business, Double>>> getAccountsRemainByBankGroup(Date onDate) {
        Map<BankGroup, Map<Bank, Map<Business, Double>>> resultMap = new HashMap<>();
        final String sqlString = "select glBank.bankGroup as bankGroup, glBank as bank, " +
                "bis.parent as business, sum(e.summEqualsUAH) as suma " +
                "from finance_AccountRemains e " +
                "join e.account.bank.parentBank glBank " +
                "join e.account.company.business bis " +
                "where e.summEqualsUAH > 0 " +
                "and glBank.bankGroup is not null " +
                "and glBank.stan = 'Нормальний' " +
                "and e.onDate = (" +
                "select max(rm.onDate) " +
                "from finance_AccountRemains rm " +
                "where rm.account = e.account " +
                "and rm.onDate <= :onDate) " +
                "group by glBank.bankGroup.id, glBank.id, bis.parent.id " +
                "order by glBank.bankGroup.id, glBank.id, bis.parent.id";
        @SuppressWarnings("rawtypes")
        List list = getListByRunQuery(onDate, sqlString);
        for (Object o : list) {
            Object[] row = (Object[]) o;
            BankGroup bankGroup = (BankGroup) row[0];
            Bank bank = (Bank) row[1];
            Business business = (Business) row[2];
            Double suma = (Double) row[3];
            if (resultMap.containsKey(bankGroup)) {
                Map<Bank, Map<Business, Double>> mapBank = resultMap.get(bankGroup);
                fillMapByBank(mapBank, bank, business, suma);
            } else {
                Map<Business, Double> mapBusiness = new HashMap<>();
                mapBusiness.put(business, suma);
                Map<Bank, Map<Business, Double>> mapBank = new HashMap<>();
                mapBank.put(bank, mapBusiness);
                resultMap.put(bankGroup, mapBank);
            }
        }
        return resultMap;
    }

    @Override
    public Map<Bank, Map<Business, Double>> getAccountsRemainByEmptyBankGroup(Date onDate) {
        Map<Bank, Map<Business, Double>> resultMap = new HashMap<>();
        final String sqlString = "select glBank as bank, " +
                "bis.parent as business, sum(e.summEqualsUAH) as suma " +
                "from finance_AccountRemains e " +
                "join e.account.bank.parentBank glBank " +
                "join e.account.company.business bis " +
                "where e.summEqualsUAH > 0 " +
                "and glBank.bankGroup is null " +
                "and glBank.stan = 'Нормальний' " +
                "and e.onDate = (" +
                "select max(rm.onDate) " +
                "from finance_AccountRemains rm " +
                "where rm.account = e.account " +
                "and rm.onDate <= :onDate) " +
                "group by glBank.id, bis.parent.id " +
                "order by glBank.id, bis.parent.id";
        @SuppressWarnings("rawtypes")
        List list = getListByRunQuery(onDate, sqlString);
        for (Object o : list) {
            Object[] row = (Object[]) o;
            Bank bank = (Bank) row[0];
            Business business = (Business) row[1];
            Double suma = (Double) row[2];
            fillMapByBank(resultMap, bank, business, suma);
        }
        return resultMap;
    }

    @Override
    public Map<Bank, Map<Business, Double>> getAccountsRemainByBlockBank(Date onDate) {
        Map<Bank, Map<Business, Double>> resultMap = new HashMap<>();
        final String sqlString = "select glBank as bank, " +
                "bis.parent as business, sum(e.summEqualsUAH) as suma " +
                "from finance_AccountRemains e " +
                "join e.account.bank.parentBank glBank " +
                "join e.account.company.business bis " +
                "where e.summEqualsUAH > 0 " +
                "and glBank.bankGroup is null " +
                "and glBank.stan <> 'Нормальний' " +
                "and e.onDate = (" +
                "select max(rm.onDate) " +
                "from finance_AccountRemains rm " +
                "where rm.account = e.account " +
                "and rm.onDate <= :onDate) " +
                "group by glBank.id, bis.parent.id " +
                "order by glBank.id, bis.parent.id";
        @SuppressWarnings("rawtypes")
        List list = getListByRunQuery(onDate, sqlString);
        for (Object o : list) {
            Object[] row = (Object[]) o;
            Bank bank = (Bank) row[0];
            Business business = (Business) row[1];
            Double suma = (Double) row[2];
            fillMapByBank(resultMap, bank, business, suma);
        }
        return resultMap;
    }

    private void fillMapByBank(Map<Bank, Map<Business, Double>> resultMap, Bank bank, Business business, Double suma) {
        if (resultMap.containsKey(bank)) {
            Map<Business, Double> mapBusiness = resultMap.get(bank);
            if (!mapBusiness.containsKey(business)) {
                mapBusiness.put(business, suma);
            }
        } else {
            Map<Business, Double> mapBusiness = new HashMap<>();
            mapBusiness.put(business, suma);
            resultMap.put(bank, mapBusiness);
        }
    }

    @SuppressWarnings("rawtypes")
    private List getListByRunQuery(Date onDate, String sqlString) {
        @SuppressWarnings("rawtypes") List list;
        Persistence persistence = AppBeans.get(Persistence.class);
        try (Transaction transaction = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();

            list = entityManager.createQuery(sqlString).setParameter("onDate", onDate).getResultList();
            transaction.commit();
        }
        return list;
    }
}