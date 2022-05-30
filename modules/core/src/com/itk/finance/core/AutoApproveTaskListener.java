package com.itk.finance.core;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class AutoApproveTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String userStrId = delegateTask.getAssignee();
    }
}
