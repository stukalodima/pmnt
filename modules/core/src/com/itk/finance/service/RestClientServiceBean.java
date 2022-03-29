package com.itk.finance.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service(RestClientService.NAME)
public class RestClientServiceBean implements RestClientService {

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
}