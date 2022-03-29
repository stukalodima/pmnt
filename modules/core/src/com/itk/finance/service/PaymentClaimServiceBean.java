package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import com.haulmont.cuba.core.global.DataManager;
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

    @Override
    public void getPaymentClaimListfromExternal() throws IOException, ParseException {
        //"http://localhost:8080/pmnt/VAADIN/paymentClaim.json"
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getPaymentClaimListUrl());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    @Override
    public PaymentClaim getPaimentClaimById(String id) {
        return getPaimentClaimById(UUID.fromString(id));
    }

    @Override
    public PaymentClaim getPaimentClaimById(UUID id) {
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
        for (JsonElement jsonElement:jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            paymentClaimMap.put("id",jsonObject.getAsJsonPrimitive("id").getAsString());
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
        PaymentClaim paymentClaim = getPaimentClaimById(paymentClaimMap.get("id").toString());

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
        paymentClaim.setStatus(ClaimStatusEnum.APPROVED_BN);
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
}