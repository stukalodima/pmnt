package com.itk.finance.service;

import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;

import java.io.Serializable;
import java.util.Map;

public interface EmailService {
    String NAME = "finance_EmailService";

    void sendEmail(String address, String caption, String templateName, Map<String, Serializable> templateParameters);

    void sendEmail(String address, String caption, String body, EmailAttachment... attachment) throws EmailException;
}