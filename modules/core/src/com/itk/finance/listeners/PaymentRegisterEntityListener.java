package com.itk.finance.listeners;

import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.itk.finance.config.ConstantsConfig;
import com.itk.finance.entity.*;
import com.itk.finance.service.ProcPropertyService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component("finance_PaymentRegisterEntityListener")
public class PaymentRegisterEntityListener implements
        BeforeDeleteEntityListener<PaymentRegister>,
        BeforeUpdateEntityListener<PaymentRegister> {
    @Inject
    private ProcessRuntimeService processRuntimeService;
    @Inject
    private ConstantsConfig constantsConfig;
    @Inject
    private ProcPropertyService procPropertyService;

    @Override
    public void onBeforeDelete(PaymentRegister entity, @SuppressWarnings("NullableProblems") EntityManager entityManager) {
        if (!Objects.isNull(entity.getProcInstance()) && entity.getProcInstance().getActive()) {
            processRuntimeService.cancelProcess(entity.getProcInstance(), "Удален реестр");
        }
        entity.getPaymentRegisters().forEach(e-> e.getPaymentClaim().setStatus(procPropertyService.getNewStatus()));
    }

    @Override
    public void onBeforeUpdate(PaymentRegister entity, @SuppressWarnings("NullableProblems") EntityManager entityManager) {
        if (entity.getStatus() != null && entity.getStatus().getCode().equals(constantsConfig.getPaymentRegisterStatusInPay())) {
            List<PaymentRegisterDetail> approvedList = entity.getPaymentRegisters().stream().filter(
                    (PaymentRegisterDetail e) -> e.getApproved().equals(PaymentRegisterDetailStatusEnum.APPROVED)
            ).collect(Collectors.toList());

            boolean payed = approvedList.stream().allMatch(e -> e.getPayed() == PayStatusEnum.PAYED);

            if (payed) {
                entity.setPayedStatus(PayStatusEnum.PAYED);
            } else {
                boolean notPayed = approvedList.stream().allMatch(e -> e.getPayed() == PayStatusEnum.NOT_PAYED);
                notPayed = notPayed || approvedList.stream().allMatch(e -> e.getPayed() == null);

                if (notPayed) {
                    entity.setPayedStatus(PayStatusEnum.NOT_PAYED);
                } else {
                    boolean dismiss = approvedList.stream().allMatch(e -> e.getPayed() == PayStatusEnum.DISMISS);

                    if (dismiss) {
                        entity.setPayedStatus(PayStatusEnum.DISMISS);
                    } else {
                        entity.setPayedStatus(PayStatusEnum.PRE_PAYED);
                    }
                }
            }
        }
    }
}