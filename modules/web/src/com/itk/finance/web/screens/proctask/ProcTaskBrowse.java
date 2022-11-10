package com.itk.finance.web.screens.proctask;

import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.client.sys.UsersRepository;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.config.ConstantsConfig;
import com.itk.finance.entity.ExtProcInstance;
import com.itk.finance.entity.PaymentRegister;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;
import java.util.Set;

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
    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataManager dataManager;
    @Inject
    private MetadataTools metadataTools;
    @Inject
    private CheckBox allTask;
    @Inject
    private ConstantsConfig constantsConfig;
    @Inject
    private UsersRepository usersRepository;

    @Subscribe
    public void onInit(InitEvent event) {
        procTasksDl.setParameter("userId", userSession.getUser().getId());
        if (allTask.isChecked()) {
            procTasksDl.setParameter("userId1", Objects.requireNonNull(usersRepository.findUserByLogin(constantsConfig.getUserFromLogin())).getId());
        } else {
            procTasksDl.setParameter("userId1", userSession.getUser().getId());
        }
        procTasksTable.setItemClickAction(procTasksTableOpenEntityEdit);
        allTask.setVisible(constantsConfig.getUserToLogin().equals(userSession.getUser().getLogin()));
    }

    @Subscribe("allTask")
    public void onAllTaskValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.isUserOriginated()) {
            procTasksDl.setParameter("userId", userSession.getUser().getId());
            if (allTask.isChecked()) {
                procTasksDl.setParameter("userId1", Objects.requireNonNull(usersRepository.findUserByLogin(constantsConfig.getUserFromLogin())).getId());
            } else {
                procTasksDl.setParameter("userId1", userSession.getUser().getId());
            }
            procTasksDl.load();
        }
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

    public Component generateNameAllProcActors(ProcTask entity) {
        String nameAllUsers;

        Label<String> amountField = uiComponents.create(Label.TYPE_STRING);

        if (Objects.isNull(entity.getProcActor())) {

            entity = dataManager.reload(entity, ViewBuilder.of(ProcTask.class)
                    .addAll("candidateUsers", "candidateUsers.name", "candidateUsers.login")
                    .build());

            nameAllUsers = "";
            if (entity.getCandidateUsers() != null) {
                Set<User> canditateUser = entity.getCandidateUsers();

                int n = 0;
                for (User cUser : canditateUser) {
                    n++;
                    nameAllUsers = nameAllUsers + metadataTools.getInstanceName(cUser) + (canditateUser.size() == n ? "" : ",\n");
                }
            }
            amountField.setValue(nameAllUsers);

        } else {
            entity = dataManager.reload(entity, ViewBuilder.of(ProcTask.class)
                    .addAll("candidateUsers", "candidateUsers.name", "candidateUsers.login", "procActor.user",
                            "procActor.user.login", "procActor.user.name", "procActor.procRole", "procActor.procRole.name", "procActor")
                    .build());
            amountField.setValue(metadataTools.getInstanceName(entity.getProcActor()));
        }

        return amountField;
    }
}