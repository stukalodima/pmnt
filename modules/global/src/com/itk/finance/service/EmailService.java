package com.itk.finance.service;

import java.io.Serializable;
import java.util.Map;

public interface EmailService {
    String NAME = "finance_EmailService";

    void sendEmail(String address, String caption, String templateName, Map<String, Serializable> templateParameters);
}