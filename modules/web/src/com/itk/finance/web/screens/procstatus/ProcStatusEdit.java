package com.itk.finance.web.screens.procstatus;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ProcStatus;

@UiController("finance_ProcStatus.edit")
@UiDescriptor("proc-status-edit.xml")
@EditedEntityContainer("procStatusDc")
@LoadDataBeforeShow
public class ProcStatusEdit extends StandardEditor<ProcStatus> {
}