package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(PaymentClaimService.NAME)
public class PaymentClaimServiceBean implements PaymentClaimService {
    private static final String QUERY_STRING_FILL_PAYMENTS_CLAIM = "select e from finance_PaymentClaim e " +
            "where " +
            "e.status = :status " +
            "and e.business = :business " +
            "and e.express = :express ";
    @Inject
    private ExternalSystemConnectConfig externalSystemConnectConfig;
    @Inject
    private DataManager dataManager;
    @Inject
    private CompanyService companyService;
    @Inject
    private UniqueNumbersAPI uniqueNumbersAPI;
    @Inject
    private ClientService clientService;
    @Inject
    private CashFlowItemsBusinessService cashFlowItemsBusinessService;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private RestClientService restClientService;
    @Inject
    private ProcPropertyService procPropertyService;

    @Override
    public void getPaymentClaimListFromExternal() throws IOException, ParseException {
        //"http://localhost:8080/pmnt/VAADIN/paymentClaim.json"
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getPaymentClaimListUrl());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    @Override
    public PaymentClaim getPaymentClaimById(String id) {
        return getPaymentClaimById(UUID.fromString(id));
    }

    @Override
    public PaymentClaim getPaymentClaimById(UUID id) {
        List<PaymentClaim> paymentClaimList = dataManager.load(PaymentClaim.class)
                .query("select e from finance_PaymentClaim e where e.id = :id")
                .parameter("id", id)
                .view("paymentClaim.getEdit")
                .list();
        PaymentClaim paymentClaim = null;
        if (paymentClaimList.size() > 0) {
            paymentClaim = paymentClaimList.get(0);
        }
        return paymentClaim;
    }

    private void parseJsonString(String jsonString) throws ParseException {
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        HashMap<String, Object> paymentClaimMap = new HashMap<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            paymentClaimMap.put("id", jsonObject.getAsJsonPrimitive("id").getAsString());
            paymentClaimMap.put("onDate", jsonObject.getAsJsonPrimitive("onDate").getAsString());
            paymentClaimMap.put("planPaymentDate", jsonObject.getAsJsonPrimitive("planPaymentDate").getAsString());
            paymentClaimMap.put("companyId", jsonObject.getAsJsonPrimitive("company").getAsString());
            paymentClaimMap.put("clientEdrpou", jsonObject.getAsJsonPrimitive("clientEDRPOU").getAsString());
            paymentClaimMap.put("clientName", jsonObject.getAsJsonPrimitive("clientname").getAsString());
            paymentClaimMap.put("cashFlowItemsBusinessId", jsonObject.getAsJsonPrimitive("cashFlowItem").getAsString());
            paymentClaimMap.put("summ", jsonObject.getAsJsonPrimitive("summ").getAsDouble());
            paymentClaimMap.put("paymentPurpose", jsonObject.getAsJsonPrimitive("paymentPurpose").getAsString());

            fillPaymentClaimEntity(paymentClaimMap);
        }
    }

    private void fillPaymentClaimEntity(HashMap<String, Object> paymentClaimMap) throws ParseException {
        PaymentClaim paymentClaim = getPaymentClaimById(paymentClaimMap.get("id").toString());

        if (Objects.isNull(paymentClaim)) {
            paymentClaim = dataManager.create(PaymentClaim.class);
        }

        String format = "yyyy-MM-dd";
        Company company = companyService.getCompanyById(paymentClaimMap.get("companyId").toString());
        Client client = clientService.getClientByEDRPOU(paymentClaimMap.get("clientEdrpou").toString());
        Date onDate = new SimpleDateFormat(format).parse(paymentClaimMap.get("onDate").toString());
        Date planPaymentDate = new SimpleDateFormat(format).parse(paymentClaimMap.get("planPaymentDate").toString());
        CashFlowItemBusiness cashFlowItemBusiness = cashFlowItemsBusinessService.getCashFlowItemsItemsById(
                paymentClaimMap.get("cashFlowItemsBusinessId").toString()
        );

        if (Objects.isNull(client)) {
            client = addNewClient(paymentClaimMap);
        }

        paymentClaim.setId(UUID.fromString(paymentClaimMap.get("id").toString()));
        paymentClaim.setOnDate(onDate);
        paymentClaim.setPlanPaymentDate(planPaymentDate);
        paymentClaim.setSumm((Double) paymentClaimMap.get("summ"));
        paymentClaim.setStatus(procPropertyService.getNewStatus());
        paymentClaim.setCompany(company);
        paymentClaim.setBusiness(company.getBusiness());
        paymentClaim.setClient(client);
        paymentClaim.setPaymentPurpose(paymentClaimMap.get("paymentPurpose").toString());
        paymentClaim.setCashFlowItemBusiness(cashFlowItemBusiness);
        paymentClaim.setCashFlowItem(cashFlowItemBusiness.getCashFlowItem());
        paymentClaim.setPaymentType(dataManager.getReference(
                PaymentType.class, UUID.fromString("a53b97e7-5604-cced-eff9-31e6d3c8babb"))
        );
        paymentClaim.setAuthor(userSessionSource.getUserSession().getUser());

        if (Objects.isNull(paymentClaim.getNumber())) {
            paymentClaim.setNumber(uniqueNumbersAPI.getNextNumber(PaymentClaim.class.getSimpleName()));
        }

        dataManager.commit(paymentClaim);
    }

    private Client addNewClient(HashMap<String, Object> paymentClaimMap) {
        Client client = dataManager.create(Client.class);

        client.setName(paymentClaimMap.get("clientName").toString());
        client.setShortName(paymentClaimMap.get("clientName").toString());
        client.setClientType(ClientTypeEnum.JUR_OSOBA);
        client.setEdrpou(paymentClaimMap.get("clientEdrpou").toString());

        dataManager.commit(client);

        return client;

    }

    public List<PaymentClaim> getPaymentClaimsListByRegister(Business business, RegisterType registerType) {
        StringBuilder conditionStr = new StringBuilder(QUERY_STRING_FILL_PAYMENTS_CLAIM);
        FluentLoader.ByQuery<PaymentClaim, UUID> byQuery;
        if (Boolean.TRUE.equals(registerType.getExpress())) {
            byQuery = dataManager.load(PaymentClaim.class).query(conditionStr.toString());
            byQuery
                    .parameter("status", procPropertyService.getNewStatus())
                    .parameter("business", business)
                    .parameter("express", registerType.getExpress());
        } else {

            Map<String, Object> mapParam = new HashMap<>();

            boolean conditionByRegisterType = getQueryStrByCondition(registerType, conditionStr, mapParam);

            byQuery = dataManager.load(PaymentClaim.class).query(conditionStr.toString());
            byQuery
                    .parameter("status", procPropertyService.getNewStatus())
                    .parameter("business", business)
                    .parameter("express", null);
            if (conditionByRegisterType) {
                byQuery.parameter("summ", registerType.getConditionValue());
                byQuery.parameter("cashFlowItems", getCashFlowItemsByRegisterType(registerType));
            } else {
                mapParam.forEach(byQuery::parameter);
            }
        }
        return byQuery.view("paymentClaim.getEdit")
                .list();
    }

    private boolean getQueryStrByCondition(RegisterType registerType, StringBuilder conditionStr, Map<String, Object> mapParam) {
        boolean conditionByRegisterType = !Objects.isNull(registerType.getUseCondition())
                && registerType.getUseCondition();
        int colCondition = 0;
        if (conditionByRegisterType) {
            conditionStr
                    .append(" and e.summ ")
                    .append(registerType.getCondition())
                    .append(" :summ ")
                    .append("and e.cashFlowItem IN :cashFlowItems");
        } else {
            conditionStr.append(" and (");
            for (RegisterTypeDetail registerTypeDetail : registerType.getRegisterTypeDetails()) {
                if (colCondition != 0) {
                    conditionStr.append(" or ");
                }
                colCondition = colCondition + 1;
                mapParam.put("cashFlowItem" + colCondition, registerTypeDetail.getCashFlowItem());
                if (!Objects.isNull(registerTypeDetail.getUseCondition()) && registerTypeDetail.getUseCondition()) {
                    conditionStr
                            .append(" (e.summ ")
                            .append(registerTypeDetail.getCondition())
                            .append(" :summ").append(colCondition)
                            .append(" and e.cashFlowItem = :cashFlowItem")
                            .append(colCondition)
                            .append(") ");
                    mapParam.put("summ" + colCondition, registerTypeDetail.getConditionValue());
                } else {
                    conditionStr.append(" (e.cashFlowItem = :cashFlowItem").append(colCondition).append(") ");
                }
            }
            conditionStr.append(")");
        }
        return conditionByRegisterType;
    }

    private ArrayList<CashFlowItem> getCashFlowItemsByRegisterType(RegisterType registerType) {
        ArrayList<CashFlowItem> cashFlowItems = new ArrayList<>();
        registerType.getRegisterTypeDetails().forEach(
                e -> cashFlowItems.add(e.getCashFlowItem())
        );
        return cashFlowItems;
    }
}