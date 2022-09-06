package com.itk.finance.service;

import java.io.IOException;

public interface RestClientService {
    String NAME = "finance_RestClientService";

    String callGetMethod(String restServiceUrl) throws IOException;
    String callGetMethod(String restServiceUrl, boolean withAuth) throws IOException;
}