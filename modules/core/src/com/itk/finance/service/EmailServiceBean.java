package com.itk.finance.service;

import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.core.global.EmailInfoBuilder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service(EmailService.NAME)
public class EmailServiceBean implements EmailService {

    @Inject
    private com.haulmont.cuba.core.app.EmailService emailService;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void sendEmail(String address, String caption, String templateName, Map<String, Serializable> templateParameters) {
        EmailInfo emailInfo = EmailInfoBuilder.create()
                .setAddresses(address)
                .setCaption(caption)
                .setFrom(null)
                .setTemplatePath("com/itk/finance/templates/" + templateName)
                .setTemplateParameters(templateParameters)
                .build();
        emailService.sendEmailAsync(emailInfo);
    }

    @Override
    public void sendEmail(String address, String caption, String body, EmailAttachment... attachment) {
        EmailInfo emailInfo = EmailInfoBuilder.create(address, caption, body)
                .setAttachments(attachment)
                .build();
        emailService.sendEmailAsync(emailInfo);
    }
}