package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.itk.finance.config.RestApiSmartPlatformConfig;
import com.itk.finance.entity.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service(PaymentClaimService.NAME)
public class PaymentClaimServiceBean implements PaymentClaimService {
    private static final String QUERY_STRING_FILL_PAYMENTS_CLAIM = "select e from finance_PaymentClaim e " +
            "where " +
            "e.status = :status " +
            "and e.business = :business " +
            "and e.express = :express ";
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
    @Inject
    private RestApiSmartPlatformConfig restApiSmartPlatformConfig;
    @Inject
    private AccountsService accountsService;
    @Inject
    private CurrencyService currencyService;

    @Override
    public void getPaymentClaimListFromExternal() throws IOException, ParseException {
        String jsonString = restClientService.callGetMethod(restApiSmartPlatformConfig.getPaymentClaimList(), true);
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    @Override
    public PaymentClaim getPaymentClaimById(String id) {
        return getPaymentClaimById(UUID.fromString(id), true);
    }

    @Override
    public PaymentClaim getPaymentClaimById(String id, boolean softDeletion) {
        return getPaymentClaimById(UUID.fromString(id), softDeletion);
    }

    @Override
    public PaymentClaim getPaymentClaimById(UUID id) {
        return getPaymentClaimById(id, true);
    }

    @Override
    public PaymentClaim getPaymentClaimById(UUID id, boolean softDeletion) {
        List<PaymentClaim> paymentClaimList = dataManager.load(PaymentClaim.class)
                .query("select e from finance_PaymentClaim e where e.id = :id")
                .parameter("id", id)
                .view("paymentClaim.getEdit")
                .softDeletion(softDeletion)
                .list();
        PaymentClaim paymentClaim = null;
        if (paymentClaimList.size() > 0) {
            paymentClaim = paymentClaimList.get(0);
        }
        return paymentClaim;
    }

    private void parseJsonString(String jsonString) throws ParseException {
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        HashMap<String, Object> paymentClaimMap = new HashMap<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            paymentClaimMap.put("id", jsonObject.getAsJsonPrimitive("id").getAsString());
            paymentClaimMap.put("number", jsonObject.getAsJsonPrimitive("number").getAsString());
            paymentClaimMap.put("numberRow", jsonObject.getAsJsonPrimitive("numberRow").getAsString());
            paymentClaimMap.put("onDate", jsonObject.getAsJsonPrimitive("onDate").getAsString());
            paymentClaimMap.put("planPaymentDate", jsonObject.getAsJsonPrimitive("planPaymentDate").getAsString());
            paymentClaimMap.put("companyId", jsonObject.getAsJsonPrimitive("company").getAsString());
            paymentClaimMap.put("companyEdrpou", jsonObject.getAsJsonPrimitive("companyEdrpou").getAsString());
            paymentClaimMap.put("companyIban", jsonObject.getAsJsonPrimitive("companyIban").getAsString());
            paymentClaimMap.put("clientEdrpou", jsonObject.getAsJsonPrimitive("clientEDRPOU").getAsString());
            paymentClaimMap.put("clientShortName", jsonObject.getAsJsonPrimitive("clientShortName").getAsString());
            paymentClaimMap.put("clientName", jsonObject.getAsJsonPrimitive("clientName").getAsString());
            paymentClaimMap.put("clientType", jsonObject.getAsJsonPrimitive("clientType").getAsString());
            paymentClaimMap.put("clientIban", jsonObject.getAsJsonPrimitive("clientIban").getAsString());
            paymentClaimMap.put("currency", jsonObject.getAsJsonPrimitive("currency").getAsString());
            paymentClaimMap.put("summ", jsonObject.getAsJsonPrimitive("summ").getAsDouble());
            paymentClaimMap.put("paymentPurpose", jsonObject.getAsJsonPrimitive("paymentPurpose").getAsString());
            paymentClaimMap.put("cashFlowItemsBusinessId", jsonObject.getAsJsonPrimitive("cashFlowItem").getAsString());
            paymentClaimMap.put("cashFlowItemName", jsonObject.getAsJsonPrimitive("cashFlowItemName").getAsString());
            paymentClaimMap.put("comment", jsonObject.getAsJsonPrimitive("comment").getAsString());
            paymentClaimMap.put("paymentTypeCode", jsonObject.getAsJsonPrimitive("paymentTypeCode").getAsString());
            paymentClaimMap.put("paymentType", jsonObject.getAsJsonPrimitive("paymentType").getAsString());
            paymentClaimMap.put("addDate", jsonObject.getAsJsonPrimitive("addDate").getAsString());

            fillPaymentClaimEntity(paymentClaimMap);
        }
    }

    private void fillPaymentClaimEntity(HashMap<String, Object> paymentClaimMap) throws ParseException {
        String format = "yyyy-MM-dd";
        Date dateAdd = new SimpleDateFormat(format).parse(paymentClaimMap.get("addDate").toString());
        Company company = companyService.getCompanyByEdrpou(paymentClaimMap.get("companyEdrpou").toString());
        if (company == null
                || company.getIntegrationEnable() == null
                || !company.getIntegrationEnable()
                || company.getDateStartIntegration() == null
                || company.getDateStartIntegration().after(dateAdd)) {
            return;
        }
        PaymentClaim paymentClaim = getPaymentClaimById(paymentClaimMap.get("id").toString(), false);

        if (Objects.isNull(paymentClaim)) {
            paymentClaim = dataManager.create(PaymentClaim.class);
        } else {
            if (!paymentClaim.getStatus().equals(procPropertyService.getNewStatus())) {
                return;
            }
            if (paymentClaim.getDeletedBy() != null) {
                return;
            }
        }

        Client client = clientService.getClientByEDRPOU(paymentClaimMap.get("clientEdrpou").toString());
        Date onDate = new SimpleDateFormat(format).parse(paymentClaimMap.get("onDate").toString());
        Date planPaymentDate = new SimpleDateFormat(format).parse(paymentClaimMap.get("planPaymentDate").toString());
        CashFlowItemBusiness cashFlowItemBusiness = cashFlowItemsBusinessService.getCashFlowItemsItemsByName(
                paymentClaimMap.get("cashFlowItemName").toString(), company.getBusiness()
        );

        if (Objects.isNull(client)) {
            client = addNewClient(paymentClaimMap);
        }

        paymentClaim.setId(UUID.fromString(paymentClaimMap.get("id").toString()));
        paymentClaim.setOnDate(onDate);
        paymentClaim.setDocNumber(paymentClaimMap.get("number").toString() + "/" + paymentClaimMap.get("numberRow").toString());
        paymentClaim.setPlanPaymentDate(planPaymentDate);
        paymentClaim.setSumm((Double) paymentClaimMap.get("summ"));
        paymentClaim.setStatus(procPropertyService.getNewStatus());
        paymentClaim.setCompany(company);
        paymentClaim.setBusiness(company.getBusiness());
        paymentClaim.setClient(client);
        paymentClaim.setClientAccount(paymentClaimMap.get("clientIban").toString());
        paymentClaim.setPaymentPurpose(paymentClaimMap.get("paymentPurpose").toString());
        paymentClaim.setCashFlowItemBusiness(cashFlowItemBusiness);
        Account account = accountsService.getCompanyAccountsByIban(paymentClaimMap.get("companyIban").toString(),
                paymentClaimMap.get("currency").toString());
        if (account != null) {
            paymentClaim.setAccount(account);
            paymentClaim.setCurrency(account.getCurrency());
        } else {
            paymentClaim.setCurrency(currencyService.getCurrencyByCode(paymentClaimMap.get("currency").toString()));
        }

        if (cashFlowItemBusiness != null) {
            paymentClaim.setCashFlowItem(cashFlowItemBusiness.getCashFlowItem());
        }
        paymentClaim.setPaymentType(dataManager.getReference(
                PaymentType.class, UUID.fromString("a53b97e7-5604-cced-eff9-31e6d3c8babb"))
        );
        paymentClaim.setAuthor(userSessionSource.getUserSession().getUser());

        if (Objects.isNull(paymentClaim.getNumber())) {
            paymentClaim.setNumber(uniqueNumbersAPI.getNextNumber(PaymentClaim.class.getSimpleName()));
        }
        paymentClaim.setComment(paymentClaimMap.get("comment").toString());
        paymentClaim.setFromExternalSystem(true);
        dataManager.commit(paymentClaim);
    }

    private Client addNewClient(HashMap<String, Object> paymentClaimMap) {
        Client client = dataManager.create(Client.class);

        client.setName(paymentClaimMap.get("clientName").toString());
        client.setShortName(paymentClaimMap.get("clientShortName").toString());
        client.setClientType(paymentClaimMap.get("clientType").toString().equals("1") ? ClientTypeEnum.JUR_OSOBA : ClientTypeEnum.FIZ_OSOBA);
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
            //noinspection ConstantConditions
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