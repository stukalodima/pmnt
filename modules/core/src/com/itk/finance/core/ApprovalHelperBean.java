package com.itk.finance.core;

import com.itk.finance.service.ProcPropertyService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

@Component(ApprovalHelperBean.NAME)
public class ApprovalHelperBean {
    public static final String NAME = "finance_ApprovalHelperBean";

    @Inject
    private ProcPropertyService procPropertyService;

    public void updateStateRegister(UUID entityId, String state) {
        procPropertyService.updateStateRegister(entityId, state);
    }
}