package com.itk.finance.web.screens.paymentregister;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.entity.ClaimStatusEnum;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.entity.PaymentRegisterDetail;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("finance_PaymentRegister.edit")
@UiDescriptor("payment-register-edit.xml")
@EditedEntityContainer("paymentRegisterDc")
@LoadDataBeforeShow
public class PaymentRegisterEdit extends StandardEditor<PaymentRegister> {
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

    @Subscribe
    public void onInitEntity(InitEntityEvent<PaymentRegister> event) {
        event.getEntity().setOnDate(timeSource.currentTimestamp());
        event.getEntity().setBusiness(userPropertyService.getDefaulBusiness());
        event.getEntity().setStatus(ClaimStatusEnum.NEW);
        event.getEntity().setAuthor(userSession.getUser());
    }

    @Subscribe("paymentRegistersDetailTable.fillPaymentClaims")
    public void onPaymentRegistersDetailTableFillPaymentClaims(Action.ActionPerformedEvent event) {
        if (commitChanges().getStatus() == OperationResult.Status.SUCCESS) {
            List<PaymentClaim> paymentClaimList = dataManager.load(PaymentClaim.class)
                    .query("select e from finance_PaymentClaim e where e.status = :status " +
                            "and e.business = :business")
                    .parameter("status", ClaimStatusEnum.APPROVED_BN)
                    .parameter("business", getEditedEntity().getBusiness())
                    .view("paymentClaim.getEdit")
                    .list();

            List<PaymentRegisterDetail> toSave = new ArrayList<>();

            List<PaymentRegisterDetail> toDelete = new ArrayList<>(paymentRegistersDc.getItems());

            paymentRegistersDc.getMutableItems().clear();

            for (PaymentClaim paymentClaim : paymentClaimList) {
                PaymentRegisterDetail paymentRegisterDetail = metadata.create(PaymentRegisterDetail.class);
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
        setApprovedByValue(true);
    }

    private void setApprovedByValue(Boolean value) {
        for (PaymentRegisterDetail selRow :
                paymentRegistersDetailTable.getLookupSelectedItems()) {
            selRow.setApproved(value);
        }
    }

    @Subscribe("paymentRegistersDetailTable.reject")
    public void onPaymentRegistersDetailTableReject(Action.ActionPerformedEvent event) {
        setApprovedByValue(false);
    }

    @Subscribe("paymentRegistersDetailTable.addLine")
    public void onPaymentRegistersDetailTableAddLine(Action.ActionPerformedEvent event) {
        //paymentRegistersDetailTable.
    }
}