package com.itk.finance.web.screens.paymentregister;

import com.haulmont.bpm.BpmConstants;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.form.ProcFormDefinition;
import com.haulmont.bpm.gui.action.CompleteProcTaskAction;
import com.haulmont.bpm.gui.proctask.ProcTasksFrame;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.bpm.service.ProcessFormService;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogOutcome;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.itk.finance.entity.*;
import com.itk.finance.service.PaymentClaimService;
import com.itk.finance.service.PaymentRegisterService;
import com.itk.finance.service.ProcPropertyService;

import javax.inject.Inject;
import java.util.*;

@UiController("finance_PaymentRegister.edit")
@UiDescriptor("payment-register-edit.xml")
@EditedEntityContainer("paymentRegisterDc")
@LoadDataBeforeShow
public class PaymentRegisterEdit extends StandardEditor<PaymentRegister> {
    public static final String QUERY_STRING_ROLES_BY_BUSSINES =
            "select e from finance_AddressingDetail e " +
                    "where " +
                    "e.addressing.bussines = :bussines " +
                    "and e.addressing.procDefinition = :procDefinition ";
    @Inject
    private EntityStates entityStates;
    @Inject
    private BpmEntitiesService bpmEntitiesService;
    @Inject
    private ProcessRuntimeService processRuntimeService;
    @Inject
    private Notifications notifications;
    @Inject
    private MessageBundle messageBundle;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionPropertyContainer<PaymentRegisterDetail> paymentRegistersDc;
    @Inject
    private GroupTable<PaymentRegisterDetail> paymentRegistersDetailTable;
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Inject
    private Button sendToApprove;
    protected ProcTask procTask;
    @Inject
    private UiComponents uiComponents;
    private final List<CompleteProcTaskAction> completeProcTaskActions = new ArrayList<>();
    @Inject
    private HBoxLayout actionsBox;
    @Inject
    private ProcessFormService processFormService;
    @Inject
    private ProcTasksFrame taskFrame;
    @Inject
    private Button paymentRegistersDetailTableFillPaymentClaimsBtn;
    @Inject
    private Button paymentRegistersDetailTableRemoveBtn;
    @Inject
    private Button paymentRegistersDetailTableApproveBtn;
    @Inject
    private Button paymentRegistersDetailTableRejectBtn;
    @Inject
    private Button paymentRegistersDetailTableDeterminateBtn;
    @Inject
    private ScreenValidation screenValidation;
    @Inject
    private DataContext dataContext;
    @Inject
    private Messages messages;
    @Inject
    private ProcPropertyService procPropertyService;
    @Inject
    private InstanceLoader<PaymentRegister> paymentRegisterDl;
    @Inject
    private Label<String> totalMessage;
    @Inject
    private PaymentClaimService paymentClaimService;
    @Inject
    private PaymentRegisterService paymentRegisterService;
    @Inject
    private Dialogs dialogs;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        updateVisible();
        initProcAction();
    }

    private void initProcAction() {
        if (!Objects.isNull(getEditedEntity().getProcInstance())) {
            List<ProcTask> procTasks = bpmEntitiesService.findActiveProcTasksForCurrentUser(getEditedEntity().getProcInstance(), BpmConstants.Views.PROC_TASK_COMPLETE);
            procTask = procTasks.isEmpty() ? null : procTasks.get(0);
            if (procTask != null && procTask.getProcActor() != null) {
                initCompleteTaskUI();
            }
            taskFrame.setProcInstance(getEditedEntity().getProcInstance());
            taskFrame.refresh();
        }
    }

    protected void initCompleteTaskUI() {
        Map<String, ProcFormDefinition> outcomesWithForms = processFormService.getOutcomesWithForms(procTask);
        if (!outcomesWithForms.isEmpty()) {
            for (Map.Entry<String, ProcFormDefinition> entry : outcomesWithForms.entrySet()) {
                CompleteProcTaskAction action = new CompleteProcTaskAction(procTask, entry.getKey(), entry.getValue());
                completeProcTaskActions.add(action);
            }
        } else {
            ProcFormDefinition form = processFormService.getDefaultCompleteTaskForm(getEditedEntity().getProcInstance().getProcDefinition());
            CompleteProcTaskAction action = new CompleteProcTaskAction(procTask, BpmConstants.DEFAULT_TASK_OUTCOME, form);
            action.setCaption(messageBundle.getMessage("completeTask"));
            completeProcTaskActions.add(action);
        }

        for (CompleteProcTaskAction completeProcTaskAction : completeProcTaskActions) {
            completeProcTaskAction.addAfterActionListener(this::closeWithCommit);
            Button actionBtn = uiComponents.create(Button.class);
            actionBtn.setWidth("100%");
            actionBtn.setAction(completeProcTaskAction);
            actionsBox.add(actionBtn);
        }
    }

    private void updateVisible() {
        sendToApprove.setVisible(Objects.isNull(getEditedEntity().getProcInstance()));
        paymentRegistersDetailTableFillPaymentClaimsBtn.setVisible(Objects.isNull(getEditedEntity().getProcInstance()));
        paymentRegistersDetailTableRemoveBtn.setVisible(Objects.isNull(getEditedEntity().getProcInstance()));
        paymentRegistersDetailTableApproveBtn.setVisible(procInstanceIsActive());
        paymentRegistersDetailTableRejectBtn.setVisible(procInstanceIsActive());
        paymentRegistersDetailTableDeterminateBtn.setVisible(procInstanceIsActive());
    }

    private boolean procInstanceIsActive() {
        return !Objects.isNull(getEditedEntity().getProcInstance()) && getEditedEntity().getProcInstance().getActive();
    }

    @Subscribe("paymentRegistersDetailTable.fillPaymentClaims")
    public void onPaymentRegistersDetailTableFillPaymentClaims(Action.ActionPerformedEvent event) {
        ValidationErrors errors = validateUiComponents();
        if (errors.isEmpty()) {
            List<PaymentClaim> paymentClaimList = paymentClaimService.getPaymentClaimsListByRegister(
                    getEditedEntity().getBusiness(), getEditedEntity().getRegisterType()
            );

            clearPaymentRegisters();
            getEditedEntity().setPaymentRegisters(paymentRegisterService.getPaymentRegisterDetailsByPaymentClaimList(
                    getEditedEntity(), paymentClaimList)
            );
            dataContext.merge(getEditedEntity().getPaymentRegisters());
        } else {
            screenValidation.showValidationErrors(this, errors);
        }
    }

    private void clearPaymentRegisters() {
        if (Objects.requireNonNull(paymentRegistersDetailTable.getItems()).size() > 0) {
            paymentRegistersDc.getItems().forEach(e -> dataContext.remove(e));
            paymentRegistersDc.getMutableItems().clear();
        }
    }

    private void setApprovedByValue(PaymentRegisterDetailStatusEnum value) {
        paymentRegistersDetailTable.getLookupSelectedItems().forEach(e -> e.setApproved(value));
    }

    @Subscribe("paymentRegistersDetailTable.approve")
    public void onPaymentRegistersDetailTableApprove(Action.ActionPerformedEvent event) {
        setApprovedByValue(PaymentRegisterDetailStatusEnum.APPROVED);
    }

    @Subscribe("paymentRegistersDetailTable.reject")
    public void onPaymentRegistersDetailTableReject(Action.ActionPerformedEvent event) {
        setApprovedByValue(PaymentRegisterDetailStatusEnum.REJECTED);
    }

    @Subscribe("paymentRegistersDetailTable.determinate")
    public void onPaymentRegistersDetailTableDeterminate(Action.ActionPerformedEvent event) {
        dialogs.createInputDialog(this)
                .withCaption(messages.getMessage(PaymentRegisterEdit.class, "inputDialogDeterminate.caption"))
                .withParameter(
                        InputParameter.dateParameter("planDate")
                                .withCaption(messages.getMessage(PaymentRegisterEdit.class, "inputDialogDeterminate.planDate.caption"))
                                .withRequired(true)
                )
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        List<PaymentClaim> paymentClaimList = new ArrayList<>();
                        getEditedEntity().getPaymentRegisters().forEach(e -> {
                            e.getPaymentClaim().setPlanPaymentDate(closeEvent.getValue("planDate"));
                            paymentClaimList.add(e.getPaymentClaim());
                        });
                        dataManager.commit(new CommitContext(paymentClaimList));
                        paymentRegisterDl.load();
                        setApprovedByValue(PaymentRegisterDetailStatusEnum.TERMINATED);
                    }
                }).show();
    }

    @Install(to = "paymentRegistersDetailTable", subject = "styleProvider")
    private String paymentRegistersDetailTableStyleProvider(PaymentRegisterDetail entity, String property) {
        if (property.equals("approved")) {
            switch (entity.getApproved()) {
                case TERMINATED:
                    return "determinate-payment";
                case REJECTED:
                    return "rejected-payment";
            }
        }
        return null;
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            getEditedEntity().setNumber(uniqueNumbersService.getNextNumber(PaymentRegister.class.getSimpleName()));
        }
        getEditedEntity().setSumma(null);
        event.resume();
    }

    @Subscribe("sendToApprove")
    public void onSendToApproveClick(Button.ClickEvent event) {
        if (Objects.isNull(getEditedEntity().getProcInstance())) {
            if (Objects.requireNonNull(paymentRegistersDetailTable.getItems()).size() > 0) {
                if (commitChanges().getStatus() == OperationResult.Status.SUCCESS) {
                    List<AddressingDetail> listRoles = dataManager.load(AddressingDetail.class)
                            .query(QUERY_STRING_ROLES_BY_BUSSINES)
                            .parameter("bussines", getEditedEntity().getBusiness())
                            .parameter("procDefinition", dataManager.reload(getEditedEntity().getRegisterType(), "registerType-with-procDefinition").getProcDefinition())
                            .view("addressingDetail-all-property")
                            .list();

                    /*The ProcInstanceDetails object is used for describing a ProcInstance to be created with its proc actors*/
                    BpmEntitiesService.ProcInstanceDetails procInstanceDetails = new BpmEntitiesService.ProcInstanceDetails(
                            dataManager
                                    .reload(getEditedEntity().getRegisterType(), "registerType-with-procDefinition")
                                    .getProcDefinition().getCode()
                    );
                    listRoles.forEach(e -> procInstanceDetails.addProcActor(e.getProcRole(), e.getUser()));
                    procInstanceDetails.setEntity(getEditedEntity());

                    /*The created ProcInstance will have two proc actors. None of the entities is persisted yet.*/
                    ProcInstance procInstance = bpmEntitiesService.createProcInstance(procInstanceDetails);

                    /*A map with process variables that must be passed to the Activity process instance when it is started. This variable is used in the model to make a decision for one of gateways.*/
                    HashMap<String, Object> processVariables = new HashMap<>();
                    //processVariables.put("acceptanceRequired", getEditedEntity().getAcceptanceRequired());

                    /*Starts the process. The "startProcess" method automatically persists the passed procInstance with its actors*/
                    processRuntimeService.startProcess(procInstance, "Process started programmatically", processVariables);
                    notifications.create()
                            .withCaption(messages.getMessage(PaymentRegisterEdit.class, "PaymentRegisterEdit.msgProcessStarted"))
                            .withType(Notifications.NotificationType.HUMANIZED)
                            .show();
                    /*refresh the procActionsFragment to display complete tasks buttons (if a process task appears for the current user after the process is started)*/
                    procPropertyService.updateStateRegister(getEditedEntity().getId(), procPropertyService.getStartStatus().getCode());
                    paymentRegisterDl.load();
                    getEditedEntity().setProcInstance((ExtProcInstance) procInstance);
                    closeWithCommit();
                }
            }
        }
    }

    @Subscribe
    public void onAfterClose(AfterCloseEvent event) {
        if (!Objects.isNull(getEditedEntity().getProcInstance()) && Objects.isNull(getEditedEntity().getProcInstance().getPaymentRegister())) {
            ExtProcInstance extProcInstance = getEditedEntity().getProcInstance();
            extProcInstance.setPaymentRegister(getEditedEntity());
            dataManager.commit(extProcInstance);
        }
    }

    @Subscribe("paymentRegistersDetailTable")
    public void onPaymentRegistersDetailTableSelection(Table.SelectionEvent<PaymentRegisterDetail> event) {
        Set<PaymentRegisterDetail> paymentRegisterDetails = event.getSelected();
        String cap = "Количество строк: " + paymentRegisterDetails.size() + " ";
        double sum = 0.;
        for (PaymentRegisterDetail detail : paymentRegisterDetails) {
            sum = sum + detail.getPaymentClaim().getSumm();
        }
        cap = cap + "Сумма строк: " + getEditedEntity().calcSummaPaymentClaim(paymentRegisterDetails);
        totalMessage.setValue(cap);
    }

}