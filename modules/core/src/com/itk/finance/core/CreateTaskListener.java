package com.itk.finance.core;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.entity.AddressingDetail;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.service.EmailService;
import com.itk.finance.service.UserPropertyService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;

import java.io.Serializable;
import java.util.*;

public class CreateTaskListener implements TaskListener {

    public static final String QUERY_STRING_GET_USER_BY_ID = "select e from sec$User e where e.id = :id";

    private Expression autoApproveExpress;

    @Override
    public void notify(DelegateTask delegateTask) {

        EmailService emailService = AppBeans.get(EmailService.class);
        Messages messages = AppBeans.get(Messages.class);
        UserPropertyService userPropertyService = AppBeans.get(UserPropertyService.class);
        Persistence persistence = AppBeans.get(Persistence.class);
        Metadata metadata = AppBeans.get(Metadata.class);

        RuntimeService runtimeService = delegateTask.getExecution().getEngineServices().getRuntimeService();
        String executionId = delegateTask.getExecutionId();
        UUID entityId = (UUID) runtimeService.getVariable(executionId, "entityId");
        String entityName = (String) runtimeService.getVariable(executionId, "entityName");

        MetaClass metaClass = metadata.getClass(entityName);

        EntityManager entityManager = persistence.getEntityManager();
        PaymentRegister paymentRegister = entityManager.find(Objects.requireNonNull(metaClass).getJavaClass(), entityId);

        String userStrId = delegateTask.getAssignee();
        List<UUID> users = new ArrayList<>();

        String isAutoApproveExpress;

        try {
            isAutoApproveExpress = (String) autoApproveExpress.getValue(delegateTask);
        }
        catch (Exception e) {
            isAutoApproveExpress = "false";
        }

        if ("true".equals(isAutoApproveExpress) && paymentRegister != null && Boolean.TRUE.equals(paymentRegister.getExpress())) {
            TaskService taskService = delegateTask.getExecution().getEngineServices().getTaskService();
            taskService.complete(delegateTask.getId());
        }

        if (userStrId == null) {
            Set<IdentityLink> userStrIdSet = delegateTask.getCandidates();
            userStrIdSet.forEach(identityLink -> users.add(UUID.fromString(identityLink.getUserId())));
        } else {
            users.add(UUID.fromString(userStrId));
        }

        for (UUID uuidUser : users) {
            User user = (User) entityManager.createQuery(QUERY_STRING_GET_USER_BY_ID)
                    .setParameter("id", uuidUser)
                    .setView(User.class, "user.browse")
                    .getFirstResult();
            if (Objects.isNull(user)) {
                continue;
            }

            AddressingDetail addressingDetail = (AddressingDetail) entityManager.createQuery("select e from finance_AddressingDetail e " +
                            "where e.addressing.bussines = :bussines " +
                            "and e.addressing.procDefinition = :procDefinition " +
                            "and e.user = :user")
                    .setParameter("bussines", Objects.requireNonNull(paymentRegister).getBusiness())
                    .setParameter("procDefinition", Objects.requireNonNull(paymentRegister).getRegisterType().getProcDefinition())
                    .setParameter("user", user)
                    .getFirstResult();
            if (!Objects.isNull(addressingDetail) && !Objects.isNull(addressingDetail.getAuto()) && addressingDetail.getAuto()) {
                TaskService taskService = delegateTask.getExecution().getEngineServices().getTaskService();
                taskService.complete(delegateTask.getId());
            }

            if (!Objects.isNull(user.getEmail()) && !userPropertyService.dontSendEmailByTask(user)) {
//                if (!Objects.isNull(user.getEmail())) {

                Map<String, Serializable> mapParam = new HashMap<>();

                mapParam.put("systemName", messages.getMessage(CreateTaskListener.class, "mail.systemName"));
                mapParam.put("welcome", messages.getMessage(CreateTaskListener.class, "mail.welcome"));
                mapParam.put("newTask", messages.getMessage(CreateTaskListener.class, "mail.newTask"));
                mapParam.put("inSystem", messages.getMessage(CreateTaskListener.class, "mail.inSystem"));
                mapParam.put("goToSystem", messages.getMessage(CreateTaskListener.class, "mail.goToSystem"));
                mapParam.put("userName", user.getName());
                mapParam.put("taskName", delegateTask.getName());

                emailService.sendEmail(user.getEmail(),
                        messages.getMessage(CreateTaskListener.class, "mail.createTaskListenerEmailCaption"),
                        messages.getMessage(CreateTaskListener.class, "mail.createTaskListenerEmailTemplate"),
                        mapParam);
//                }
            }
        }
        users.clear();
    }
}
