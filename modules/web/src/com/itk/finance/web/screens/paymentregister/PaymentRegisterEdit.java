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
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.entity.*;
import com.itk.finance.service.ProcPropertyService;
import com.itk.finance.service.UserPropertyService;

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
    public static final String QUERY_STRING_FILL_PAYMENTS_CLAIM = "select e from finance_PaymentClaim e " +
            "where " +
            "e.status = :status " +
            "and e.business = :business ";
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
    private Metadata metadata;
    @Inject
    private TimeSource timeSource;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private GroupTable<PaymentRegisterDetail> paymentRegistersDetailTable;
    @Inject
    private UserSession userSession;
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

    @Subscribe
    public void onInitEntity(InitEntityEvent<PaymentRegister> event) {
        event.getEntity().setOnDate(timeSource.currentTimestamp());
        event.getEntity().setBusiness(userPropertyService.getDefaultBusiness());
        event.getEntity().setStatus(procPropertyService.getNewStatus());
        event.getEntity().setAuthor(userSession.getUser());
    }

    @Subscribe("paymentRegistersDetailTable.fillPaymentClaims")
    public void onPaymentRegistersDetailTableFillPaymentClaims(Action.ActionPerformedEvent event) {
        ValidationErrors errors = validateUiComponents();
        if (errors.isEmpty()) {
            StringBuilder conditionStr = new StringBuilder(QUERY_STRING_FILL_PAYMENTS_CLAIM);
            Map<String, Object> mapParam = new HashMap<>();

            boolean conditionByRegisterType = getQueryStrByCondition(conditionStr, mapParam);

            FluentLoader.ByQuery<PaymentClaim, UUID> byQuery = dataManager.load(PaymentClaim.class).query(conditionStr.toString());
            byQuery.parameter("status", procPropertyService.getNewStatus())
                    .parameter("business", getEditedEntity().getBusiness());
            if (conditionByRegisterType) {
                byQuery.parameter("summ", getEditedEntity().getRegisterType().getConditionValue());
                byQuery.parameter("cashFlowItems", getCashFlowItemsByRegisterType());
            } else {
                mapParam.forEach(byQuery::parameter);
            }
            List<PaymentClaim> paymentClaimList = byQuery.view("paymentClaim.getEdit")
                    .list();

            clearPaymentRegisters();
            List<PaymentRegisterDetail> listDetail = getPaymentRegisterDetailsByPaymentClaimList(paymentClaimList);
            getEditedEntity().setPaymentRegisters(listDetail);
        } else {
            screenValidation.showValidationErrors(this, errors);
        }
    }

    private boolean getQueryStrByCondition(StringBuilder conditionStr, Map<String, Object> mapParam) {
        boolean conditionByRegisterType = !Objects.isNull(getEditedEntity().getRegisterType().getUseCondition())
                && getEditedEntity().getRegisterType().getUseCondition();
        int colCondition = 0;
        if (conditionByRegisterType) {
            conditionStr
                    .append(" and e.summ ")
                    .append(getEditedEntity().getRegisterType().getCondition())
                    .append(" :summ ")
                    .append("and e.cashFlowItem IN :cashFlowItems");
        } else {
            conditionStr.append(" and (");
            for (RegisterTypeDetail registerTypeDetail : getEditedEntity().getRegisterType().getRegisterTypeDetails()) {
                if (colCondition != 0) {
                    conditionStr.append(" or ");
                }
                colCondition = colCondition + 1;
                mapParam.put("cashFlowItem" + colCondition, registerTypeDetail.getCashFlowItem());
                if (!Objects.isNull(registerTypeDetail.getUseCondition()) && registerTypeDetail.getUseCondition()) {
                    conditionStr
                            .append(" (e.summ ")
                            .append(registerTypeDetail.getCondition())
                            .append(" :summ").append(colCondition)
                            .append(" and e.cashFlowItem = :cashFlowItem")
                            .append(colCondition)
                            .append(") ");
                    mapParam.put("summ" + colCondition, registerTypeDetail.getConditionValue());
                } else {
                    conditionStr.append(" (e.cashFlowItem = :cashFlowItem").append(colCondition).append(") ");
                }
            }
            conditionStr.append(")");
        }
        return conditionByRegisterType;
    }

    private List<PaymentRegisterDetail> getPaymentRegisterDetailsByPaymentClaimList(List<PaymentClaim> paymentClaimList) {
        List<PaymentRegisterDetail> listDetail = new ArrayList<>();
        paymentClaimList.forEach(e -> listDetail.add(createPaymentRegisterDetail(e)));
        return listDetail;
    }

    private ArrayList<CashFlowItem> getCashFlowItemsByRegisterType() {
        ArrayList<CashFlowItem> cashFlowItems = new ArrayList<>();
        getEditedEntity().getRegisterType().getRegisterTypeDetails().forEach(e -> cashFlowItems.add(e.getCashFlowItem()));
        return cashFlowItems;
    }

    private void clearPaymentRegisters() {
        if (Objects.requireNonNull(paymentRegistersDetailTable.getItems()).size() > 0) {
            paymentRegistersDc.getItems().forEach(e -> dataContext.remove(e));
            paymentRegistersDc.getMutableItems().clear();
        }
    }

    private PaymentRegisterDetail createPaymentRegisterDetail(PaymentClaim paymentClaim) {
        PaymentRegisterDetail paymentRegisterDetail = metadata.create(PaymentRegisterDetail.class);
        paymentRegisterDetail.setApproved(PaymentRegisterDetailStatusEnum.APPROVED);
        paymentRegisterDetail.setPaymentClaim(paymentClaim);
        paymentRegisterDetail.setPaymentRegister(getEditedEntity());
        return dataContext.merge(paymentRegisterDetail);
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
        setApprovedByValue(PaymentRegisterDetailStatusEnum.TERMINATED);
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            getEditedEntity().setNumber(uniqueNumbersService.getNextNumber(PaymentRegister.class.getSimpleName()));
        }
        getEditedEntity().setSumma(calcSummaPaymentClaim(new HashSet<>(getEditedEntity().getPaymentRegisters())));
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
        cap = cap + "Сумма строк: " + calcSummaPaymentClaim(paymentRegisterDetails);
        totalMessage.setValue(cap);
    }

    public String calcSummaPaymentClaim(Set<PaymentRegisterDetail> paymentRegisterDetails) {
        Map<String, Double> mapSumma = new HashMap<>();
        paymentRegisterDetails.forEach(e -> {
            PaymentClaim paymentClaim = dataManager.reload(e.getPaymentClaim(), "paymentClaim.getEdit");
            String key = paymentClaim.getCurrency().getShortName();
            double value = paymentClaim.getSumm();
            if (mapSumma.containsKey(key)) {
                mapSumma.replace(key, mapSumma.get(key) + value);
            } else {
                mapSumma.put(key, value);
            }
        });
        StringBuilder text = new StringBuilder();
        mapSumma.forEach((key, value) -> {
                    if (!text.toString().equals("")) {
                        text.append("; ");
                    }
                    text.append(
                                    String.format("%,.2f", value)
                                            .replace(",", " ")
                                            .replace(".", ",")
                            )
                            .append(" ")
                            .append(key);
                }
        );
        return text.toString();
    }
}