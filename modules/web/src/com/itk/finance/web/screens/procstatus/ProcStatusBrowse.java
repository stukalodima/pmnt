package com.itk.finance.web.screens.procstatus;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ProcStatus;

@UiController("finance_ProcStatus.browse")
@UiDescriptor("proc-status-browse.xml")
@LookupComponent("procStatusesTable")
@LoadDataBeforeShow
public class ProcStatusBrowse extends StandardLookup<ProcStatus> {
}