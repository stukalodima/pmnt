package com.itk.finance.web.screens.paymentclaim;

import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcAttachment;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.gui.procactionsfragment.ProcActionsFragment;
import com.haulmont.bpm.gui.proctask.ProcTasksFrame;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.app.core.file.FileDownloadHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.ClaimStatusEnum;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.*;

@UiController("finance_PaymentClaim.edit")
@UiDescriptor("payment-claim-edit.xml")
@EditedEntityContainer("paymentClaimDc")
@LoadDataBeforeShow
public class PaymentClaimEdit extends StandardEditor<PaymentClaim> {
    private static final String PROCESS_CODE = "paymentclaimapproval";
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private TimeSource timeSource;
    @Inject
    private LookupPickerField<Company> companyField;
    @Inject
    private Form form;
    @Inject
    private InstanceLoader<PaymentClaim> paymentClaimDl;
    @Inject
    private CollectionLoader<ProcAttachment> procAttachmentsDl;
    @Inject
    private InstanceContainer<PaymentClaim> paymentClaimDc;
    @Inject
    private ProcActionsFragment procActionsFragment;
    @Inject
    private Table<ProcAttachment> attachmentsTable;
    @Inject
    private GroupBoxLayout procActionsBox;
    @Inject
    private UserSession userSession;
    @Inject
    private BpmEntitiesService bpmEntitiesService;
    @Inject
    private ProcessRuntimeService processRuntimeService;
    @Inject
    private Notifications notifications;
    @Inject
    private MessageBundle messageBundle;
    @Inject
    private Metadata metadata;
    @Inject
    private Button sendApprovedBtn;
    @Inject
    private ProcTasksFrame procTaskFragment;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionLoader<Business> businessesDl;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        paymentClaimDl.load();
        procAttachmentsDl.setParameter("entityId", paymentClaimDc.getItem().getId());
        procAttachmentsDl.load();
        procActionsFragment.initializer()
                .standard()
                .init(PROCESS_CODE, getEditedEntity());
        procTaskFragment.setProcInstance(procActionsFragment.getProcInstance());
        procTaskFragment.refresh();
        FileDownloadHelper.initGeneratedColumn(attachmentsTable, "file");
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<PaymentClaim> event) {
        event.getEntity().setOnDate(timeSource.currentTimestamp());
        event.getEntity().setPlanPaymentDate(new Date(timeSource.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        event.getEntity().setStatus(ClaimStatusEnum.NEW);
        event.getEntity().setAuthor(userSession.getUser());

        Business business = userPropertyService.getDefaulBusiness();
        Company company = userPropertyService.getDefaulCompany();

        if (business != null) {
            if (business.getUsePaymantClaim()) {
                event.getEntity().setBusiness(business);
                event.getEntity().setCompany(company);
            }
            if (!business.getUsePaymentClaimApproval()) {
                event.getEntity().setStatus(ClaimStatusEnum.APPROVED_BN);
            }
        }
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        companyField.setValue(companyField.getEmptyValue());
        if (Objects.requireNonNull(event.getValue()).getUsePaymentClaimApproval()) {
            getEditedEntity().setStatus(ClaimStatusEnum.NEW);
        } else {
            getEditedEntity().setStatus(ClaimStatusEnum.APPROVED_BN);
        }
        refreshForm(getEditedEntity().getStatus(), event.getValue());
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        refreshForm(getEditedEntity().getStatus(), getEditedEntity().getBusiness());
    }

    private void refreshForm(ClaimStatusEnum thisStatus, Business thisBusiness) {
        if (thisBusiness.getUsePaymentClaimApproval()) {
            form.setEditable(thisStatus == ClaimStatusEnum.NEW);
        }
        procActionsBox.setVisible(thisStatus == ClaimStatusEnum.PREPARED);
        sendApprovedBtn.setEnabled(thisStatus == ClaimStatusEnum.NEW);
        businessesDl.setParameter("usePaymantClaim", true);
        companiesDl.setParameter("business", thisBusiness);
        companiesDl.load();
        sendApprovedBtn.setEnabled(thisBusiness.getUsePaymentClaimApproval());
    }

    @Subscribe("sendApprovedBtn")
    public void onSendApprovedBtnClick(Button.ClickEvent event) {
        if (commitChanges().getStatus() == OperationResult.Status.SUCCESS) {
            Business business = dataManager.reload(getEditedEntity().getBusiness(), "business.getEdit");
            /*The ProcInstanceDetails object is used for describing a ProcInstance to be created with its proc actors*/
            BpmEntitiesService.ProcInstanceDetails procInstanceDetails = new BpmEntitiesService.ProcInstanceDetails(PROCESS_CODE)
                    .addProcActor("FinControlerBN", business.getFinControler())
                    .addProcActor("FinDirectorBN", business.getFinDirector())
                    .addProcActor("GenDirectorBN", business.getGenDirector())
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
            getEditedEntity().setStatus(ClaimStatusEnum.PREPARED);
            commitChanges();
            this.close(WINDOW_COMMIT_AND_CLOSE_ACTION);
        }
    }

    private void initProcActionsFragment() {
        procActionsFragment.initializer()
                .standard()
                .setBeforeStartProcessPredicate(() -> {
                    /*the predicate creates process actors and sets them to the process instance created by the ProcActionsFragment*/
                    if (commitChanges().getStatus() == OperationResult.Status.SUCCESS) {
                        ProcInstance procInstance = procActionsFragment.getProcInstance();
                        Business business = dataManager.reload(getEditedEntity().getBusiness(), "business.getEdit");
                        ProcActor finControlerBN = createProcActor("FinControlerBN", procInstance
                                , business.getFinControler());
                        ProcActor finDirectorBN = createProcActor("FinDirectorBN", procInstance
                                , business.getFinDirector());
                        ProcActor GenDirectorBN = createProcActor("GenDirectorBN", procInstance
                                , business.getGenDirector());
                        Set<ProcActor> procActors = new HashSet<>();
                        procActors.add(finControlerBN);
                        procActors.add(finDirectorBN);
                        procActors.add(GenDirectorBN);
                        procInstance.setProcActors(procActors);
                        return true;
                    }
                    return false;
                })
                .setAfterCompleteTaskListener(() -> {
                    initProcActionsFragment();
                    paymentClaimDl.setEntityId(getEditedEntity().getId());
                    paymentClaimDl.load();
                })
                .init(PROCESS_CODE, getEditedEntity());
        procTaskFragment.setProcInstance(procActionsFragment.getProcInstance());
        procTaskFragment.refresh();
    }

    private ProcActor createProcActor(String procRoleCode, ProcInstance procInstance, User user) {
        ProcActor initiatorProcActor = metadata.create(ProcActor.class);
        initiatorProcActor.setUser(user);
        ProcRole initiatorProcRole = bpmEntitiesService.findProcRole(PROCESS_CODE, procRoleCode, View.MINIMAL);
        initiatorProcActor.setProcRole(initiatorProcRole);
        initiatorProcActor.setProcInstance(procInstance);
        return initiatorProcActor;
    }

    @Subscribe("statusField")
    public void onStatusFieldValueChange(HasValue.ValueChangeEvent<ClaimStatusEnum> event) {
        refreshForm(event.getValue(), getEditedEntity().getBusiness());
    }
}