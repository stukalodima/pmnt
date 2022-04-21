package com.itk.finance.web.screens.paymentregister;

import com.haulmont.bpm.BpmConstants;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.gui.action.ClaimProcTaskAction;
import com.haulmont.bpm.gui.procactionsfragment.ProcActionsFragment;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.entity.*;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.*;

@UiController("finance_PaymentRegister.edit")
@UiDescriptor("payment-register-edit.xml")
@EditedEntityContainer("paymentRegisterDc")
@LoadDataBeforeShow
public class PaymentRegisterEdit extends StandardEditor<PaymentRegister> {
    private static final String PROCESS_CODE = "pmntRegister";
    @Inject
    private InstanceLoader<PaymentRegister> paymentRegisterDl;
    @Inject
    private ProcActionsFragment procActionsFragment;
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
    private DataGrid<PaymentRegisterDetail> paymentRegistersDetailTable;
    @Inject
    private UserSession userSession;
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Inject
    private GroupBoxLayout procActionsBox;
    @Inject
    private Button sendToApprove;
    @Inject
    private Button approved;
    @Inject
    private Button rejected;
    @Inject
    private Messages messages;
    protected ProcTask procTask;
    protected ClaimProcTaskAction claimProcTaskAction;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private HBoxLayout editActions;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
       initProcActionsFragment();
       updateVisible();
//       initProcAction(procActionsFragment.getProcInstance());
    }

    private void initProcAction(ProcInstance procInstance) {
        List<ProcTask> procTasks = bpmEntitiesService.findActiveProcTasksForCurrentUser(procInstance, BpmConstants.Views.PROC_TASK_COMPLETE);
        procTask = procTasks.isEmpty() ? null : procTasks.get(0);
        if (procTask == null) {

        } else if (procTask.getProcActor() != null) {
            initClaimTaskUI();
        }
    }

    private void initClaimTaskUI() {
       Button claimTaskBtn = uiComponents.create(Button.class);
        claimTaskBtn.setWidth("100");
        claimProcTaskAction = new ClaimProcTaskAction(procTask);
        claimTaskBtn.setAction(claimProcTaskAction);
        editActions.add(claimTaskBtn);
    }

    private void updateVisible() {
        sendToApprove.setVisible(getEditedEntity().getStatus() == ClaimStatusEnum.NEW);
        approved.setVisible(getEditedEntity().getStatus() != ClaimStatusEnum.NEW);
        rejected.setVisible(getEditedEntity().getStatus() != ClaimStatusEnum.NEW);
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<PaymentRegister> event) {
        event.getEntity().setOnDate(timeSource.currentTimestamp());
        event.getEntity().setBusiness(userPropertyService.getDefaultBusiness());
        event.getEntity().setStatus(ClaimStatusEnum.NEW);
        event.getEntity().setAuthor(userSession.getUser());
    }

    @Subscribe("paymentRegistersDetailTable.fillPaymentClaims")
    public void onPaymentRegistersDetailTableFillPaymentClaims(Action.ActionPerformedEvent event) {
        if (commitChanges().getStatus() == OperationResult.Status.SUCCESS) {
            List<PaymentClaim> paymentClaimList = dataManager.load(PaymentClaim.class)
                    .query("select e from finance_PaymentClaim e where e.status = :status " +
                            "and e.business = :business ")
                    .parameter("status", ClaimStatusEnum.NEW)
                    .parameter("business", getEditedEntity().getBusiness())
                    .view("paymentClaim.getEdit")
                    .list();

            List<PaymentRegisterDetail> toSave = new ArrayList<>();

            List<PaymentRegisterDetail> toDelete = new ArrayList<>(paymentRegistersDc.getItems());

            paymentRegistersDc.getMutableItems().clear();

            for (PaymentClaim paymentClaim : paymentClaimList) {
                PaymentRegisterDetail paymentRegisterDetail = metadata.create(PaymentRegisterDetail.class);
                paymentRegisterDetail.setApproved(PaymentRegisterDetailStatusEnum.APPROVED);
                paymentRegisterDetail.setCompany(paymentClaim.getCompany());
                paymentRegisterDetail.setClient(paymentClaim.getClient());
                paymentRegisterDetail.setSumm(paymentClaim.getSumm());
                paymentRegisterDetail.setPaymentPurpose(paymentClaim.getPaymentPurpose());
                paymentRegisterDetail.setCashFlowItem(paymentClaim.getCashFlowItem());
                paymentRegisterDetail.setPaymentType(paymentClaim.getPaymentType());
                paymentRegisterDetail.setComment(paymentClaim.getComment());
                paymentRegisterDetail.setPaymentRegister(getEditedEntity());
                paymentRegisterDetail.setPaymentClaim(paymentClaim);
                toSave.add(paymentRegisterDetail);
                paymentRegistersDc.getMutableItems().add(paymentRegisterDetail);
            }

            CommitContext commitContext = new CommitContext(toSave, toDelete);

            dataManager.commit(commitContext);
        }
    }

    @Subscribe("paymentRegistersDetailTable.approve")
    public void onPaymentRegistersDetailTableApprove(Action.ActionPerformedEvent event) {
        setApprovedByValue(PaymentRegisterDetailStatusEnum.APPROVED);
    }

    private void setApprovedByValue(PaymentRegisterDetailStatusEnum value) {
        for (PaymentRegisterDetail selRow :
                paymentRegistersDetailTable.getLookupSelectedItems()) {
            selRow.setApproved(value);
        }
        paymentRegistersDetailTable.deselectAll();
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
    }

    private void initProcActionsFragment() {
        procActionsFragment.initializer()
                .standard()
                .setBeforeStartProcessPredicate(() -> {
                    /*the predicate creates process actors and sets them to the process instance created by the ProcActionsFragment*/
                    if (commitChanges().getStatus() == OperationResult.Status.SUCCESS) {
                        ProcInstance procInstance = procActionsFragment.getProcInstance();
                        Business business = dataManager.reload(getEditedEntity().getBusiness(), "business-all-property");
                        ProcActor initiatorProcActor = createProcActor("finControler", procInstance, business.getManagementCompany().getFinControler());
                        ProcActor executorProcActor = createProcActor("finDirector", procInstance, business.getManagementCompany().getFinDirector());
                        ProcActor finDirBN = createProcActor("finDirectorBN", procInstance, business.getFinDirector());
                        Set<ProcActor> procActors = new HashSet<>();
                        procActors.add(initiatorProcActor);
                        procActors.add(executorProcActor);
                        procActors.add(finDirBN);
                        procInstance.setProcActors(procActors);
                        return true;
                    }
                    return false;
                })
                .setAfterStartProcessListener(() -> {
                    closeWithCommit();
                })
                .setAfterCompleteTaskListener(() -> {
                    closeWithCommit();
                })
                .init(PROCESS_CODE, getEditedEntity());
    }

    private ProcActor createProcActor(String procRoleCode, ProcInstance procInstance, User user) {
        ProcActor initiatorProcActor = metadata.create(ProcActor.class);
        initiatorProcActor.setUser(user);
        ProcRole initiatorProcRole = bpmEntitiesService.findProcRole(PROCESS_CODE, procRoleCode, View.MINIMAL);
        initiatorProcActor.setProcRole(initiatorProcRole);
        initiatorProcActor.setProcInstance(procInstance);
        return initiatorProcActor;
    }

    @Subscribe("approved")
    public void onApprovedClick(Button.ClickEvent event) {
        closeWithCommit();
    }

    @Subscribe("rejected")
    public void onRejectedClick(Button.ClickEvent event) {
        closeWithCommit();
    }

    @Subscribe("sendToApprove")
    public void onSendToApproveClick(Button.ClickEvent event) {
        paymentRegisterDl.load();
        procActionsFragment.initializer()
                .standard()
                .init(PROCESS_CODE, getEditedEntity());
        if (getEditedEntity().getStatus() == ClaimStatusEnum.NEW && !Objects.isNull(getEditedEntity().getPaymentRegisters())) {
            Business business = dataManager.reload(getEditedEntity().getBusiness(), "business-all-property");
            /*The ProcInstanceDetails object is used for describing a ProcInstance to be created with its proc actors*/
            BpmEntitiesService.ProcInstanceDetails procInstanceDetails = new BpmEntitiesService.ProcInstanceDetails(PROCESS_CODE)
                    .addProcActor("finControler", business.getManagementCompany().getFinControler())
                    .addProcActor("finDirector", business.getManagementCompany().getFinDirector())
                    .addProcActor("finDirectorBN", business.getFinDirector())
                    .setEntity(getEditedEntity());

            /*The created ProcInstance will have two proc actors. None of the entities is persisted yet.*/
            ProcInstance procInstance = bpmEntitiesService.createProcInstance(procInstanceDetails);

            /*A map with process variables that must be passed to the Activiti process instance when it is started. This variable is used in the model to make a decision for one of gateways.*/
            HashMap<String, Object> processVariables = new HashMap<>();
            //processVariables.put("acceptanceRequired", getEditedEntity().getAcceptanceRequired());

            /*Starts the process. The "startProcess" method automatically persists the passed procInstance with its actors*/
            processRuntimeService.startProcess(procInstance, "Process started programmatically", processVariables);
            notifications.create()
                    .withCaption(messageBundle.getMessage("processStarted"))
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();

            /*refresh the procActionsFragment to display complete tasks buttons (if a process task appears for the current user after the process is started)*/
            initProcActionsFragment();
//            for (PaymentRegisterDetail paymentRegisterDetail:getEditedEntity().getPaymentRegisters()
//                 ) {
//                PaymentClaim paymentClaim = paymentRegisterDetail.getPaymentClaim();
//                paymentClaim.setStatus(ClaimStatusEnum.IN_APPROVE);
//                dataManager.commit(paymentClaim);
//            }
//            getEditedEntity().setStatus(ClaimStatusEnum.PREPARED);
//            commitChanges();
            closeWithCommit();
        }
    }
}