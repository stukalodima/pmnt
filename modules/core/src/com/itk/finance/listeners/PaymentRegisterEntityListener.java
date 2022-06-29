package com.itk.finance.listeners;

import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.itk.finance.entity.PaymentRegister;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Objects;

@Component("finance_PaymentRegisterEntityListener")
public class PaymentRegisterEntityListener implements BeforeDeleteEntityListener<PaymentRegister> {
    @SuppressWarnings("all")
    @Inject
    private ProcessRuntimeService processRuntimeService;

    @Override
    public void onBeforeDelete(PaymentRegister entity, EntityManager entityManager) {
        if (!Objects.isNull(entity.getProcInstance())) {
            ProcInstance procInstance = entityManager.reload(entity.getProcInstance(), "_local");
            processRuntimeService.cancelProcess(procInstance, "Удален реестр");
        }
    }
}