package com.itk.finance.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.config.ExternalSystemConnectConfig;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service(CompanyService.NAME)
public class CompanyServiceBean implements CompanyService {

    @Inject
    private DataManager dataManager;
    @Inject
    private ExternalSystemConnectConfig externalSystemConnectConfig;
    @Inject
    private RestClientService restClientService;

    @Override
    public void getCompanyListFromExternal() throws IOException {
        //"http://localhost:8080/pmnt/VAADIN/company.json"
        String jsonString = restClientService.callGetMethod(externalSystemConnectConfig.getCompanyListUrl());
        if (!jsonString.isEmpty()) {
            parseJsonString(jsonString);
        }
    }

    @Override
    public Company getCompanyByEdrpou(String edrpou) {
        List<Company> companyList = dataManager.load(Company.class)
                .query("select e from finance_Company e where e.edrpou = :edrpou")
                .parameter("edrpou", edrpou)
                .view("company-all-property")
                .list();
        Company company = null;
        if (companyList.size() > 0) {
            company = companyList.get(0);
        }
        return company;
    }

    @Override
    public Company getCompanyById(String id) {
        UUID uuid = UUID.fromString(id);
        List<Company> companyList = dataManager.load(Company.class)
                .query("select e from finance_Company e where e.id = :id")
                .parameter("id", uuid)
                .view("company-all-property")
                .list();
        Company company = null;
        if (companyList.size() > 0) {
            company = companyList.get(0);
        }
        return company;
    }

    private void parseJsonString(String jsonString) {
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        HashMap<String, String> companyMap = new HashMap<>();
        for (JsonElement jsonElement:jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

//            companyMap.put("id", jsonObject.getAsJsonPrimitive("id").getAsString());
//            companyMap.put("edrpou", jsonObject.getAsJsonPrimitive("kodEDRPOU").getAsString());
//            companyMap.put("name", jsonObject.getAsJsonPrimitive("name").getAsString());
//            companyMap.put("shortName", jsonObject.getAsJsonPrimitive("name").getAsString());

            //from accounts
            companyMap.put("id", jsonObject.getAsJsonPrimitive("company").getAsString());
            companyMap.put("edrpou", jsonObject.getAsJsonPrimitive("company_edrpou").getAsString());
            companyMap.put("name", jsonObject.getAsJsonPrimitive("company_name").getAsString());
            companyMap.put("shortName", jsonObject.getAsJsonPrimitive("company_name").getAsString());

            fillCompanyEntity(companyMap);
        }
    }

    private void fillCompanyEntity(HashMap<String, String> companyMap){
        Company company = getCompanyById(companyMap.get("id"));

        if (Objects.isNull(company)) {
            company = dataManager.create(Company.class);
        }

        company.setId(UUID.fromString(companyMap.get("id")));
        company.setName(companyMap.get("name"));
        company.setShortName(companyMap.get("shortName"));
        company.setEdrpou(companyMap.get("edrpou"));
        if (Objects.isNull(company.getBusiness())) {
            company.setBusiness(
                    dataManager.getReference(
                            Business.class, UUID.fromString("88d0a5c0-16fe-199d-49df-431ecb8de113")
                    )
            );
        }

        dataManager.commit(company);
    }
}