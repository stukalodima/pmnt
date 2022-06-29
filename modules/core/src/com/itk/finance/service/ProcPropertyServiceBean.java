package com.itk.finance.service;

import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.entity.ExtProcInstance;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.entity.ProcStatus;
import com.itk.finance.entity.UserProperty;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(ProcPropertyService.NAME)
public class ProcPropertyServiceBean implements ProcPropertyService {

    private static final String QUERY_STRING_GET_PROC_STAT_BY_CODE = "select e from finance_ProcStatus e " +
            "where e.code = :code";
    private static final String QUERY_STRING_GET_PROC_STAT = "select e from finance_ProcStatus e ";
    private static final String CONDITION_IS_NEW = "where e.isNew = true";
    private static final String CONDITION_IS_START = "where e.isStart = true";
    public static final String SEND_NOTIFICATIONS = "select e from finance_UserProperty e where e.sendNotificationTask = :sendNotifications";
    public static final String TASK_BY_USER = "select pt from bpm$ProcTask pt left join pt.procActor pa left join pa.user pau " +
            "where pau.id = :userId " +
            "and pt.endDate is null";
    public static final String DD_MM_YYYY = "dd.MM.yyyy";
    @Inject
    private Persistence persistence;
    @Inject
    private DataManager dataManager;
    @Inject
    private Messages messages;
    @Inject
    private EmailService emailService;

    public void updateStateRegister(UUID entityId, String state) {
        try (Transaction tx = persistence.getTransaction()) {
            PaymentRegister paymentRegister = persistence.getEntityManager().find(PaymentRegister.class, entityId);
            if (!Objects.isNull(paymentRegister)) {
                paymentRegister.setStatus(getProcStatByCode(state));
                paymentRegister.getPaymentRegisters().forEach(e -> e.getPaymentClaim().setStatus(getProcStatByCode(state))
                );
            }
            tx.commit();
        }
    }

    @Override
    public ProcStatus getProcStatByCode(String code) {
        return dataManager.load(ProcStatus.class)
                .query(QUERY_STRING_GET_PROC_STAT_BY_CODE)
                .parameter("code", code)
                .view("_local")
                .one();
    }

    @Override
    public ProcStatus getNewStatus() {
        return getStatusByCondition(CONDITION_IS_NEW);
    }

    @Override
    public ProcStatus getStartStatus() {
        return getStatusByCondition(CONDITION_IS_START);
    }

    @Override
    public void sendNotificationsTask() {
        List<UserProperty> userPropertyList = getUserPropertiesList();
        userPropertyList.forEach(e -> {
            User user = dataManager.reload(e.getUser(),"user.browse");
            List<ProcTask> procTasks = dataManager.load(ProcTask.class)
                    .query(TASK_BY_USER)
                    .parameter("userId", e.getUser().getId())
                    .view("procTask-complete")
                    .list();
            StringBuilder taskList = getTaskList(procTasks);

            if (!Objects.isNull(user.getEmail()) && !taskList.toString().isEmpty()) {

                Map<String, Serializable> mapParam = new HashMap<>();

                mapParam.put("systemName", messages.getMessage(ProcPropertyServiceBean.class, "mail.systemName"));
                mapParam.put("TaskToDo", messages.getMessage(ProcPropertyServiceBean.class, "mail.TaskToDo"));
                mapParam.put("welcome", messages.getMessage(ProcPropertyServiceBean.class, "mail.welcome"));
                mapParam.put("inSystem", messages.getMessage(ProcPropertyServiceBean.class, "mail.taskInSystem"));
                mapParam.put("goToSystem", messages.getMessage(ProcPropertyServiceBean.class, "mail.goToSystem"));
                mapParam.put("taskList", taskList.toString());
                mapParam.put("userName", user.getName());

                emailService.sendEmail(user.getEmail(),
                        messages.getMessage(ProcPropertyServiceBean.class, "mail.emailCaption"),
                        messages.getMessage(ProcPropertyServiceBean.class, "mail.emailTemplate"),
                        mapParam);
            }
        });
    }

    private StringBuilder getTaskList(List<ProcTask> procTasks) {
        StringBuilder taskList = new StringBuilder();
        int index =1;
        DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
        for (ProcTask task: procTasks) {
            PaymentRegister paymentRegister = ((ExtProcInstance)task.getProcInstance()).getPaymentRegister();
            if (paymentRegister.isDeleted()) {
                continue;
            }
            paymentRegister = dataManager.reload(paymentRegister, "paymentRegister-all-property");
            taskList.append(index)
                    .append(". ")
                    .append(messages.getMessage(ProcPropertyServiceBean.class, "mail.registerText"))
                    .append(" ")
                    .append("№")
                    .append(paymentRegister.getNumber())
                    .append(" ")
                    .append(messages.getMessage(ProcPropertyServiceBean.class,"mail.captionFrom"))
                    .append(" ")
                    .append(dateFormat.format(paymentRegister.getOnDate()))
                    .append(" \"")
                    .append(paymentRegister.getRegisterType().getName())
                    .append("\" ")
                    .append("- БН")
                    .append(" ")
                    .append(paymentRegister.getBusiness().getName())
                    .append("<p>");
            index ++;
        }
        return taskList;
    }

    private List<UserProperty> getUserPropertiesList() {
        return dataManager.load(UserProperty.class)
                .query(SEND_NOTIFICATIONS)
                .parameter("sendNotifications", Boolean.TRUE)
                .view("userProperty.get.edit")
                .list();
    }

    private ProcStatus getStatusByCondition(String condition) {
        return dataManager.load(ProcStatus.class)
                .query(QUERY_STRING_GET_PROC_STAT + condition)
                .view("_local")
                .one();
    }
}