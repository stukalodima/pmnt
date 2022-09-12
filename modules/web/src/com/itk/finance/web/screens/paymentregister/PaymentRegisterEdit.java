package com.itk.finance.web.screens.paymentregister;

import com.haulmont.bpm.BpmConstants;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.form.ProcFormDefinition;
import com.haulmont.bpm.gui.action.ClaimProcTaskAction;
import com.haulmont.bpm.gui.action.CompleteProcTaskAction;
import com.haulmont.bpm.gui.action.ProcAction;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.bpm.service.ProcessFormService;
import com.haulmont.bpm.service.ProcessMessagesService;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogOutcome;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.itk.finance.config.ConstantsConfig;
import com.itk.finance.entity.*;
import com.itk.finance.service.PaymentClaimService;
import com.itk.finance.service.PaymentRegisterService;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

@UiController("finance_PaymentRegister.edit")
@UiDescriptor("payment-register-edit.xml")
@EditedEntityContainer("paymentRegisterDc")
public class PaymentRegisterEdit extends StandardEditor<PaymentRegister> {
    public static final String QUERY_STRING_ROLES_BY_BUSINESS =
            "select e from finance_AddressingDetail e " +
                    "where " +
                    "e.addressing.bussines = :business " +
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
    private ScreenValidation screenValidation;
    @Inject
    private DataContext dataContext;
    @Inject
    private Messages messages;
    @Inject
    private InstanceLoader<PaymentRegister> paymentRegisterDl;
    @Inject
    private Label<String> totalMessage;
    @Inject
    private PaymentClaimService paymentClaimService;
    @Inject
    private PaymentRegisterService paymentRegisterService;
    @Inject
    private ProcessMessagesService processMessagesService;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private MessageTools messageTools;
    @Inject
    private PopupView popupView;
    @Inject
    private TextArea<String> comment;

    private PaymentRegisterDetail paymentRegisterDetailPopupElement;
    @Inject
    private Form formBody;
    private boolean fullScreenStatus = false;
    @Inject
    private GroupBoxLayout groupBody;
    @Inject
    private HBoxLayout editActions;
    @Inject
    private TabSheet tabBody;
    @Inject
    private CollectionLoader<ProcTask> paymentRegisterTaskDl;
    @Inject
    private ConstantsConfig constantsConfig;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Metadata metadata;
    @Inject
    private TimeSource timeSource;
    @Inject
    private Button paymentRegistersDetailTableApproveBtn;
    @Inject
    private Button paymentRegistersDetailTableDismissBtn;
    @Inject
    private Button paymentRegistersDetailTableNotPayedBtn;
    @Inject
    private Button paymentRegistersDetailTablePayedBtn;
    @Inject
    private Button paymentRegistersDetailTableRejectBtn;
    @Inject
    private Button paymentRegistersDetailTableFillPaymentClaimsBtn;
    @Inject
    private Button paymentRegistersDetailTableRemoveBtn;
    @Inject
    private CollectionLoader<Business> businessesDl;
    @Inject
    private CollectionLoader<RegisterType> registerTypesDl;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        paymentRegisterDl.load();
        if (Objects.isNull(getEditedEntity().getProcInstance())) {
            paymentRegisterTaskDl.setParameter("procInstance", null);
        } else {
            paymentRegisterTaskDl.setParameter("procInstance", getEditedEntity().getProcInstance().getId());
        }
        paymentRegisterTaskDl.load();
        initProcAction();
        updateVisible();
        if (formBody.isVisible()) {
            businessesDl.load();
            registerTypesDl.load();
        }
    }

    private void initProcAction() {
        if (!Objects.isNull(getEditedEntity().getProcInstance())) {
            List<ProcTask> procTasks = bpmEntitiesService.findActiveProcTasksForCurrentUser(getEditedEntity().getProcInstance(), BpmConstants.Views.PROC_TASK_COMPLETE);
            procTask = procTasks.isEmpty() ? null : procTasks.get(0);
            if (procTask != null && procTask.getProcActor() != null) {
                initCompleteTaskUI();
            } else if (procTask != null && procTask.getProcActor() == null) {
                initClaimTaskUI();
            }
            paymentRegisterTaskDl.setParameter("procInstance", getEditedEntity().getProcInstance().getId());
            paymentRegisterTaskDl.load();
        }
    }

    private void initClaimTaskUI() {
        Button claimTaskBtn = uiComponents.create(Button.class);
        claimTaskBtn.setWidth("100%");

        ProcAction.AfterActionListener afterClaimTaskListener = () -> {
            actionsBox.removeAll();
            initProcAction();
            updateVisible();
        };

        ClaimProcTaskAction claimProcTaskAction = new ClaimProcTaskAction(procTask);
        claimTaskBtn.setAction(claimProcTaskAction);
        claimProcTaskAction.addAfterActionListener(afterClaimTaskListener);
        actionsBox.add(claimTaskBtn);
    }

    protected void initCompleteTaskUI() {
        Map<String, ProcFormDefinition> outcomesWithForms = processFormService.getOutcomesWithForms(procTask);
        if (!outcomesWithForms.isEmpty()) {
            for (Map.Entry<String, ProcFormDefinition> entry : outcomesWithForms.entrySet()) {
                CompleteProcTaskAction action = new CompleteProcTaskAction(procTask, entry.getKey(), entry.getValue());
                action.setCaption(processMessagesService.getMessage(
                                procTask.getProcInstance().getProcDefinition().getActId(),
                                procTask.getActTaskDefinitionKey() + "." + entry.getKey(),
                                getUserLocale()
                        )
                );
                completeProcTaskActions.add(action);
            }
        } else {
            ProcFormDefinition form = processFormService.getDefaultCompleteTaskForm(getEditedEntity().getProcInstance().getProcDefinition());
            CompleteProcTaskAction action = new CompleteProcTaskAction(procTask, BpmConstants.DEFAULT_TASK_OUTCOME, form);
            action.setCaption(messageBundle.getMessage("completeTask"));
            completeProcTaskActions.add(action);
        }

        for (CompleteProcTaskAction completeProcTaskAction : completeProcTaskActions) {
            completeProcTaskAction.addBeforeActionPredicate(() -> {
                if (getEditedEntity().getStatus() != null
                        && getEditedEntity().getStatus().getCode().equals(constantsConfig.getPaymentRegisterStatusInPay())) {
                    boolean isNotPayed = getEditedEntity().getPaymentRegisters().stream()
                            .anyMatch(e -> e.getPayed() == PayStatusEnum.NOT_PAYED);
                    boolean isPrePayed = getEditedEntity().getPaymentRegisters().stream()
                            .anyMatch(e -> e.getPayed() == PayStatusEnum.PRE_PAYED);
                    if (isNotPayed || isPrePayed) {
                        notifications.create()
                                .withCaption(messageBundle.getMessage("notPayedDone.caption"))
                                .withDescription(messageBundle.getMessage("notPayedDone.description"))
                                .show();
                        return false;
                    }
                }
                return true;
            });
            completeProcTaskAction.addAfterActionListener(() -> {
                getScreenData().loadAll();
                closeWithCommit();
            });
            Button actionBtn = uiComponents.create(Button.class);
            actionBtn.setWidth("100%");
            actionBtn.setAction(completeProcTaskAction);
            actionsBox.add(actionBtn);
        }
    }

    private Locale getUserLocale() {
        return userSessionSource.checkCurrentUserSession() ?
                userSessionSource.getUserSession().getLocale() :
                messageTools.getDefaultLocale();
    }

    private void updateVisible() {
        sendToApprove.setVisible(Objects.isNull(getEditedEntity().getProcInstance()));
        formBody.setEditable(Objects.isNull(getEditedEntity().getProcInstance()));
        if (getEditedEntity().getProcInstance() != null
                && procTask != null
                && procTask.getProcActor() != null
                && procTask.getProcActor().getUser().equals(userSessionSource.getUserSession().getUser())) {
            paymentRegistersDetailTable.focus();
            if (!paymentRegistersDc.getItems().isEmpty()) {
                paymentRegistersDetailTable.setSelected(paymentRegistersDc.getItems().get(0));
            }
        }
    }

    @Install(to = "paymentRegistersDetailTable.fillPaymentClaims", subject = "enabledRule")
    private boolean paymentRegistersDetailTableFillPaymentClaimsEnabledRule() {
        paymentRegistersDetailTableFillPaymentClaimsBtn.setVisible(Objects.isNull(getEditedEntity().getProcInstance()));
        return Objects.isNull(getEditedEntity().getProcInstance());
    }

    @Install(to = "paymentRegistersDetailTable.remove", subject = "enabledRule")
    private boolean paymentRegistersDetailTableRemoveEnabledRule() {
        paymentRegistersDetailTableRemoveBtn.setVisible(Objects.isNull(getEditedEntity().getProcInstance()));
        return Objects.isNull(getEditedEntity().getProcInstance());
    }

    @Install(to = "paymentRegistersDetailTable.reject", subject = "enabledRule")
    private boolean paymentRegistersDetailTableRejectEnabledRule() {
        paymentRegistersDetailTableRejectBtn.setVisible(getEnabledApproveAndRejectAction());
        return getEnabledApproveAndRejectAction();
    }

    @Install(to = "paymentRegistersDetailTable.approve", subject = "enabledRule")
    private boolean paymentRegistersDetailTableApproveEnabledRule() {
        paymentRegistersDetailTableApproveBtn.setVisible(getEnabledApproveAndRejectAction());
        return getEnabledApproveAndRejectAction();
    }

    private boolean getEnabledApproveAndRejectAction() {
        return procInstanceIsActive()
                && !paymentRegistersDetailTable.getSelected().isEmpty()
                && procTask != null
                && procTask.getProcActor() != null
                && procTask.getProcActor().getUser() != null
                && procTask.getProcActor().getUser().equals(userSessionSource.getUserSession().getUser())
                && !getEditedEntity().getStatus().getCode().equals(constantsConfig.getPaymentRegisterStatusInPay());
    }

    private boolean getEnabledPayAction() {
        return procInstanceIsActive()
                && !paymentRegistersDetailTable.getSelected().isEmpty()
                && procTask != null
                && procTask.getProcActor() != null
                && procTask.getProcActor().getUser() != null
                && procTask.getProcActor().getUser().equals(userSessionSource.getUserSession().getUser())
                && getEditedEntity().getStatus().getCode().equals(constantsConfig.getPaymentRegisterStatusInPay());
    }

    @Install(to = "paymentRegistersDetailTable.notPayedAct", subject = "enabledRule")
    private boolean paymentRegistersDetailTableNotPayedActEnabledRule() {
        paymentRegistersDetailTableNotPayedBtn.setVisible(getEnabledPayAction());
        return getEnabledPayAction();
    }

    @Install(to = "paymentRegistersDetailTable.payedAct", subject = "enabledRule")
    private boolean paymentRegistersDetailTablePayedActEnabledRule() {
        paymentRegistersDetailTablePayedBtn.setVisible(getEnabledPayAction());
        return getEnabledPayAction();
    }

    @Install(to = "paymentRegistersDetailTable.dismissAct", subject = "enabledRule")
    private boolean paymentRegistersDetailTableDismissActEnabledRule() {
        paymentRegistersDetailTableDismissBtn.setVisible(getEnabledPayAction());
        return getEnabledPayAction();
    }


    private boolean procInstanceIsActive() {
        return !Objects.isNull(getEditedEntity().getProcInstance()) && getEditedEntity().getProcInstance().getActive();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
        paymentRegistersDetailTable.getLookupSelectedItems().forEach(e -> {
            e.setApproved(value);
            if (value == PaymentRegisterDetailStatusEnum.REJECTED) {
                e.setSumaToPay(0.);
                e.setPayed(PayStatusEnum.DISMISS);
            }
            if (value == PaymentRegisterDetailStatusEnum.APPROVED) {
                e.setSumaToPay(e.getPaymentClaim().getSumm());
                e.setPayed(PayStatusEnum.NOT_PAYED);
            }
        });
        commitChanges();
    }

    @Subscribe("paymentRegistersDetailTable.approve")
    public void onPaymentRegistersDetailTableApprove(Action.ActionPerformedEvent event) {
        setApprovedByValue(PaymentRegisterDetailStatusEnum.APPROVED);
    }

    @Subscribe("paymentRegistersDetailTable.reject")
    public void onPaymentRegistersDetailTableReject(Action.ActionPerformedEvent event) {
        setApprovedByValue(PaymentRegisterDetailStatusEnum.REJECTED);
    }


    @Subscribe
    public void onInit(InitEvent event) {
        paymentRegistersDetailTable.setStyleProvider(new GroupTable.GroupStyleProvider<PaymentRegisterDetail>() {
            @Nullable
            @Override
            public String getStyleName(@SuppressWarnings("NullableProblems") PaymentRegisterDetail entity, @Nullable String property) {
                if (property == null) {
                    if (entity.getPayed() == null) {
                        return null;
                    }
                    switch (entity.getPayed()) {
                        case PAYED:
                            return "approved1";
                        case PRE_PAYED:
                            return "startProc1";
                        case DISMISS:
                            return "terminated1";
                    }
                    switch (entity.getApproved()) {
                        case REJECTED:
                            return "rejected";
                        case TERMINATED:
                            return "terminated";
                    }
                }
                return null;
            }

            @Nullable
            @Override
            public String getStyleName(@SuppressWarnings("NullableProblems") GroupInfo info) {
                MetaPropertyPath metaPropertyPath = (MetaPropertyPath) info.getProperty();
                if ("payed".equals(metaPropertyPath.toPathString())) {
                    return PaymentRegisterHelper.getGroupTableStyleByPayedStatus(info);
                }
                if ("approved".equals(metaPropertyPath.toPathString())) {
                    return PaymentRegisterHelper.getGroupTableStyleByApproveStatus(info);
                }
                return null;
            }
        });
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
                            .query(QUERY_STRING_ROLES_BY_BUSINESS)
                            .parameter("business", getEditedEntity().getBusiness())
                            .parameter("procDefinition", dataManager.reload(getEditedEntity().getRegisterType(), "registerType-with-procDefinition").getProcDefinition())
                            .view("addressingDetail-all-property")
                            .list();

                    /*The ProcInstanceDetails object is used for describing a ProcInstance to be created with its proc actors*/
                    BpmEntitiesService.ProcInstanceDetails procInstanceDetails = new BpmEntitiesService.ProcInstanceDetails(
                            dataManager
                                    .reload(getEditedEntity().getRegisterType(), "registerType-with-procDefinition")
                                    .getProcDefinition().getCode()
                    );
                    listRoles.forEach(e -> {
                        if (constantsConfig.getPaymentRegisterControllerRole().equals(e.getProcRole().getCode())) {
                            View view = new View(Business.class)
                                    .addProperty("controllerList", new View(BusinessControllers.class)
                                            .addProperty("user")
                                    );
                            Business business = dataManager.reload(getEditedEntity().getBusiness(), view);
                            business.getControllerList().forEach(cont -> procInstanceDetails.addProcActor(e.getProcRole(), cont.getUser()));
                        }
                        procInstanceDetails.addProcActor(e.getProcRole(), e.getUser());
                    });
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
//                    procPropertyService.updateStateRegister(getEditedEntity().getId(), procPropertyService.getStartStatus().getCode());
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

    @Subscribe("paymentRegistersDetailTable.comment")
    public void onPaymentRegistersDetailTableCommentClick(Table.Column.ClickEvent<PaymentRegisterDetail> event) {
        if (!popupView.isPopupVisible()) {
            paymentRegisterDetailPopupElement = event.getItem();
            popupView.setPopupPosition(PopupView.PopupPosition.MIDDLE_CENTER);
            comment.setValue(paymentRegisterDetailPopupElement.getComment());
            popupView.setPopupVisible(true);
        }
    }

    @Subscribe("popupView")
    public void onPopupViewPopupVisibility(PopupView.PopupVisibilityEvent event) {
        if (!event.isPopupVisible()) {
            paymentRegistersDetailTable.setSelected(paymentRegisterDetailPopupElement);
            paymentRegisterDetailPopupElement.setComment(comment.getRawValue());
        }
    }

    @Subscribe("commentBtn")
    public void onCommentBtnClick(Button.ClickEvent event) {
        popupView.setPopupVisible(false);
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        if (event.isUserOriginated()) {
            onPaymentRegistersDetailTableFillPaymentClaims(null);
        }
    }

    @Subscribe("registerTypeField")
    public void onRegisterTypeFieldValueChange(HasValue.ValueChangeEvent<RegisterType> event) {
        if (event.isUserOriginated()) {
            onPaymentRegistersDetailTableFillPaymentClaims(null);
        }
    }

    @Install(to = "tabBody", subject = "contextHelpIconClickHandler")
    private void tabBodyContextHelpIconClickHandler(HasContextHelp.ContextHelpIconClickEvent contextHelpIconClickEvent) {
        if (fullScreenStatus) {
            contextHelpIconClickEvent.getSource().setContextHelpText(
                    messages.getMessage(PaymentRegisterEdit.class, "paymentRegisterEdit.normalScreen")
            );
        } else {
            contextHelpIconClickEvent.getSource().setContextHelpText(
                    messages.getMessage(PaymentRegisterEdit.class, "paymentRegisterEdit.fullScreen")
            );
        }
        groupBody.setVisible(fullScreenStatus);
        editActions.setVisible(fullScreenStatus);
        tabBody.setTabsVisible(fullScreenStatus);
        fullScreenStatus = !fullScreenStatus;
    }

    @Subscribe("paymentRegistersDetailTable.payedAct")
    public void onPaymentRegistersDetailTablePayedAct(Action.ActionPerformedEvent event) {
        getEditedEntity().setPayedStatus(null);
        Set<PaymentRegisterDetail> paymentRegisterDetailSet = paymentRegistersDetailTable.getSelected();
        if (paymentRegisterDetailSet.isEmpty()) {
            return;
        }
        if (paymentRegisterDetailSet.size() == 1) {
            PaymentRegisterDetail paymentRegisterDetail = paymentRegistersDetailTable.getSingleSelected();
            if (paymentRegisterDetail == null) {
                return;
            }
            dialogs.createInputDialog(this)
                    .withCaption(messageBundle.getMessage("payedParameter.caption"))
                    .withParameters(
                            InputParameter.dateParameter("payedDate")
                                    .withCaption(messageTools.getPropertyCaption(
                                            Objects.requireNonNull(metadata.getClass(PaymentRegisterDetail.class)),
                                            "payedDate")
                                    )
                                    .withDefaultValue(timeSource.currentTimestamp())
                                    .withRequired(true),
                            InputParameter.doubleParameter("payedSuma")
                                    .withCaption(messageTools.getPropertyCaption(
                                                    Objects.requireNonNull(metadata.getClass(PaymentRegisterDetail.class)),
                                                    "payedSuma"
                                            )
                                    )
                                    .withDefaultValue(
                                            paymentRegisterDetail.getSumaToPay() == null ? 0 : paymentRegisterDetail.getSumaToPay()
                                    )
                                    .withRequired(true)
                    )
                    .withActions(DialogActions.OK_CANCEL)
                    .withCloseListener(closeEvent -> {
                                if (closeEvent.closedWith(DialogOutcome.OK)) {
                                    if (paymentRegisterDetail.getApproved() == PaymentRegisterDetailStatusEnum.REJECTED) {
                                        return;
                                    }
                                    Double payedSumm = paymentRegisterDetail.getPayedSuma() == null ? 0.
                                            : paymentRegisterDetail.getPayedSuma()
                                            + (closeEvent.getValue("payedSuma") == null ? 0.
                                            : (Double) Objects.requireNonNull(closeEvent.getValue("payedSuma")));
                                    Double sumaToPay = paymentRegisterDetail.getPaymentClaim().getSumm() - paymentRegisterDetail.getPayedSuma();
                                    paymentRegisterDetail.setPayedDate(closeEvent.getValue("payedDate"));
                                    paymentRegisterDetail.setPayedSuma(payedSumm);
                                    paymentRegisterDetail.setSumaToPay(sumaToPay);
                                    if (paymentRegisterDetail.getSumaToPay() == 0) {
                                        paymentRegisterDetail.setPayed(PayStatusEnum.PAYED);
                                    } else {
                                        paymentRegisterDetail.setPayed(PayStatusEnum.PRE_PAYED);
                                    }
                                    commitChanges();
                                }
                            }
                    )
                    .show();
        } else {
            dialogs.createInputDialog(this)
                    .withCaption(messageBundle.getMessage("payedParameter.caption"))
                    .withParameter(
                            InputParameter.dateParameter("payedDate")
                                    .withCaption(messageTools.getPropertyCaption(
                                            Objects.requireNonNull(metadata.getClass(PaymentRegisterDetail.class)),
                                            "payedDate")
                                    )
                                    .withDefaultValue(timeSource.currentTimestamp())
                                    .withRequired(true)
                    )
                    .withActions(DialogActions.OK_CANCEL)
                    .withCloseListener(closeEvent -> {
                                if (closeEvent.closedWith(DialogOutcome.OK)) {
                                    paymentRegisterDetailSet.forEach(paymentRegisterDetail -> {
                                        if (paymentRegisterDetail.getApproved() == PaymentRegisterDetailStatusEnum.REJECTED) {
                                            return;
                                        }
                                        paymentRegisterDetail.setPayedDate(closeEvent.getValue("payedDate"));
                                        paymentRegisterDetail.setPayedSuma(paymentRegisterDetail.getPaymentClaim().getSumm());
                                        paymentRegisterDetail.setPayed(PayStatusEnum.PAYED);
                                        paymentRegisterDetail.setSumaToPay(null);
                                    });
                                    commitChanges();
                                }
                            }
                    )
                    .show();
        }
    }

    @Subscribe("paymentRegistersDetailTable.notPayedAct")
    public void onPaymentRegistersDetailTableNotPayedAct(Action.ActionPerformedEvent event) {
        getEditedEntity().setPayedStatus(null);
        Set<PaymentRegisterDetail> paymentRegisterDetailSet = paymentRegistersDetailTable.getSelected();
        if (paymentRegisterDetailSet.isEmpty()) {
            return;
        }
        paymentRegisterDetailSet.forEach(paymentRegisterDetail -> {
            if (paymentRegisterDetail.getApproved() == PaymentRegisterDetailStatusEnum.REJECTED) {
                return;
            }
            paymentRegisterDetail.setPayedDate(null);
            paymentRegisterDetail.setPayedSuma(0.);
            paymentRegisterDetail.setPayed(PayStatusEnum.NOT_PAYED);
            paymentRegisterDetail.setSumaToPay(paymentRegisterDetail.getPaymentClaim().getSumm());
        });
        commitChanges();
    }

    @Subscribe("paymentRegistersDetailTable.dismissAct")
    public void onPaymentRegistersDetailTableDismissAct(Action.ActionPerformedEvent event) {
        getEditedEntity().setPayedStatus(null);
        Set<PaymentRegisterDetail> paymentRegisterDetailSet = paymentRegistersDetailTable.getSelected();
        if (paymentRegisterDetailSet.isEmpty()) {
            return;
        }
        paymentRegisterDetailSet.forEach(paymentRegisterDetail -> {
            paymentRegisterDetail.setPayedDate(null);
            paymentRegisterDetail.setPayedSuma(0.);
            paymentRegisterDetail.setPayed(PayStatusEnum.DISMISS);
            paymentRegisterDetail.setSumaToPay(0.);
        });
        commitChanges();
    }
}