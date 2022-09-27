package com.itk.finance.core;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.service.UserPropertyService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import java.util.Objects;
import java.util.UUID;

public class CreateTaskListenerSetVisibleExpressField implements TaskListener {

    private Expression setExpressField;

    @Override
    public void notify(DelegateTask delegateTask) {
        String isSetExpressField = (String) setExpressField.getValue(delegateTask);
        if ("true".equals(isSetExpressField)) {
            Persistence persistence = AppBeans.get(Persistence.class);

            Metadata metadata = AppBeans.get(Metadata.class);

            RuntimeService runtimeService = delegateTask.getExecution().getEngineServices().getRuntimeService();
            String executionId = delegateTask.getExecutionId();
            UUID entityId = (UUID) runtimeService.getVariable(executionId, "entityId");
            String entityName = (String) runtimeService.getVariable(executionId, "entityName");

            MetaClass metaClass = metadata.getClass(entityName);

            try (Transaction tx = persistence.createTransaction()) {
                // get EntityManager for the current transaction
                EntityManager entityManager = persistence.getEntityManager();

                PaymentRegister paymentRegister = entityManager.find(Objects.requireNonNull(metaClass).getJavaClass(), entityId);

                if (paymentRegister != null) {
                    paymentRegister.setSetExpressField(true);
                }

                tx.commit();
            }
        }
    }
}
