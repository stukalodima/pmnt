package com.itk.finance.core;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.service.EmailService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.io.Serializable;
import java.util.*;

public class CreateTaskListener implements TaskListener {

    public static final String QUERY_STRING_GET_USER_BY_ID = "select e from sec$User e where e.id = :id";

    @Override
    public void notify(DelegateTask delegateTask) {

        EmailService emailService = AppBeans.get(EmailService.class);
        DataManager dataManager = AppBeans.get(DataManager.class);
        Messages messages = AppBeans.get(Messages.class);

        String userStrId = delegateTask.getAssignee();

        UUID uuidUser = UUID.fromString(userStrId);

        User user = dataManager.load(User.class)
                .query(QUERY_STRING_GET_USER_BY_ID)
                .parameter("id", uuidUser)
                .view("user.browse")
                .one();

        if (!Objects.isNull(user.getEmail())) {

            Map<String, Serializable> mapParam = new HashMap<>();

            mapParam.put("systemName",messages.getMessage(CreateTaskListener.class,"mail.systemName"));
            mapParam.put("welcome",messages.getMessage(CreateTaskListener.class,"mail.welcome"));
            mapParam.put("newTask",messages.getMessage(CreateTaskListener.class,"mail.newTask"));
            mapParam.put("inSystem",messages.getMessage(CreateTaskListener.class,"mail.inSystem"));
            mapParam.put("goToSystem",messages.getMessage(CreateTaskListener.class,"mail.goToSystem"));
            mapParam.put("userName", user.getName());
            mapParam.put("taskName", delegateTask.getName());

            emailService.sendEmail(user.getEmail(),
                    messages.getMessage(CreateTaskListener.class,"mail.createTaskListenerEmailCaption"),
                    messages.getMessage(CreateTaskListener.class, "mail.createTaskListenerEmailTemplate"),
                    mapParam);
        }

    }
}
