package com.itk.finance.service;

import com.itk.finance.config.RestApiSmartPlatformConfig;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service(RestClientService.NAME)
public class RestClientServiceBean implements RestClientService {


    @Inject
    private RestApiSmartPlatformConfig restApiSmartPlatformConfig;

    @Override
    public String callGetMethod(String restServiceUrl) throws IOException {
        StringBuilder jsonString = new StringBuilder();
        URL obj = new URL(restServiceUrl);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonString.append(inputLine);
            }
            bufferedReader.close();
        }
        return jsonString.toString();
    }

    @Override
    public String callGetMethod(String restServiceUrl, boolean withAuth) throws IOException {
        StringBuilder jsonString = new StringBuilder();
        URL obj = new URL(restServiceUrl);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        if (withAuth) {
            String authHeaderValue = restApiSmartPlatformConfig.getFinanceServiceApiUser() + ":" + restApiSmartPlatformConfig.getFinanceServiceApiPassword();
            byte[] encodedAuth = Base64.encodeBase64(authHeaderValue.getBytes(StandardCharsets.UTF_8));
            String authValue = "Basic " + new String(encodedAuth);
            connection.setRequestProperty("Authorization", authValue);
        }
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonString.append(inputLine);
            }
            bufferedReader.close();
        }
        return jsonString.toString();
    }
}