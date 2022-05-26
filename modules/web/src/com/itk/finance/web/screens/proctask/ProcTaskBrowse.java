package com.itk.finance.web.screens.proctask;

import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.entity.ExtProcInstance;
import com.itk.finance.entity.PaymentRegister;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;

@UiController("bpm$ProcTask.browse")
@UiDescriptor("proc-task-browse.xml")
@LookupComponent("procTasksTable")
@LoadDataBeforeShow
public class ProcTaskBrowse extends StandardLookup<ProcTask> {
    @Inject
    private CollectionLoader<ProcTask> procTasksDl;
    @Inject
    private UserSession userSession;
    @Inject
    private CollectionContainer<ProcTask> procTasksDc;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private GroupTable<ProcTask> procTasksTable;
    @Named("procTasksTable.openEntityEdit")
    private BaseAction procTasksTableOpenEntityEdit;
    @Named("procTasksTable.openProcInstanceEdit")
    private BaseAction procTasksTableOpenProcInstanceEdit;

    @Subscribe
    public void onInit(InitEvent event) {
        procTasksDl.setParameter("userId", userSession.getUser().getId());
        procTasksTable.setItemClickAction(procTasksTableOpenEntityEdit);
    }

    @Subscribe("procTasksTable.openEntityEdit")
    public void onProcTasksTableOpenEntityEdit(Action.ActionPerformedEvent event) {
        ExtProcInstance extProcInstance = (ExtProcInstance) procTasksDc.getItem().getProcInstance();
        Screen screen = screenBuilders.editor(PaymentRegister.class, this)
                .editEntity(extProcInstance.getPaymentRegister())
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        screen.addAfterCloseListener(e->procTasksDl.load());
        screen.show();
    }

    @Subscribe("procTasksTable.openProcInstanceEdit")
    public void onProcTasksTableOpenProcInstanceEdit(Action.ActionPerformedEvent event) {
        Screen screen = screenBuilders.editor(ExtProcInstance.class, this)
                .editEntity((ExtProcInstance) procTasksDc.getItem().getProcInstance())
                .withLaunchMode(OpenMode.THIS_TAB)
                .build();
        screen.addAfterCloseListener(e->procTasksDl.load());
        screen.show();
    }

    @Subscribe("procTasksTable")
    public void onProcTasksTableSelection(Table.SelectionEvent<ProcTask> event) {
        if (event.getSelected().size() == 1) {
            event.getSelected().forEach(e -> {
                procTasksTableOpenEntityEdit.setEnabled(!Objects.isNull(((ExtProcInstance)e.getProcInstance()).getPaymentRegister()));
                procTasksTableOpenProcInstanceEdit.setEnabled(!Objects.isNull(((ExtProcInstance)e.getProcInstance()).getPaymentRegister()));
            });
        }
    }
}