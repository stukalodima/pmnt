package com.itk.finance.web.screens.paymentclaim;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.PaymentRegisterDetail;
import com.itk.finance.service.PaymentClaimService;
import de.diedavids.cuba.dataimport.web.WithImportWizard;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

@UiController("finance_PaymentClaim.browse")
@UiDescriptor("payment-claim-browse.xml")
@LookupComponent("paymentClaimsTable")
@LoadDataBeforeShow
public class PaymentClaimBrowse extends StandardLookup<PaymentClaim> implements WithImportWizard {
    @Inject
    private PaymentClaimService paymentClaimService;
    @Inject
    private CollectionLoader<PaymentClaim> paymentClaimsDl;
    @Inject
    private Notifications notifications;
    @Inject
    private Messages messages;
    @Inject
    private GroupTable<PaymentClaim> paymentClaimsTable;
    @Inject
    private CollectionContainer<PaymentClaim> paymentClaimsDc;
    @Inject
    private ButtonsPanel buttonsPanel;
    @Inject
    private DataManager dataManager;
    @Inject
    private UniqueNumbersService uniqueNumbersService;

    @Subscribe("paymentClaimsTable.fillPaymentClaim")
    public void onPaymentClaimsTableFillPaymentClaim(Action.ActionPerformedEvent event) {
        try {
            paymentClaimService.getPaymentClaimListFromExternal();
        } catch (IOException e) {
            notifications.create()
                    .withCaption(messages.getMessage(PaymentClaimBrowse.class, "message.restCall.error.caption"))
                    .withContentMode(ContentMode.HTML)
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription(
                            messages.getMessage(PaymentClaimBrowse.class, "message.restCall.error.text")
                                    +"\n"
                                    + e.getMessage()
                    )
                    .show();
        } catch (ParseException e) {
            notifications.create()
                    .withCaption(messages.getMessage(PaymentClaimBrowse.class, "message.parseDate.error.caption"))
                    .withContentMode(ContentMode.HTML)
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription(
                            messages.getMessage(PaymentClaimBrowse.class, "message.parseDate.error.text")
                                    +"\n"
                                    + e.getMessage()
                    )
                    .show();
        }
        paymentClaimsDl.load();
    }

    @SuppressWarnings("all")
    @Override
    public ListComponent getListComponent() {
        return paymentClaimsTable;
    }

    @SuppressWarnings("all")
    @Override
    public CollectionContainer getCollectionContainer() {
        return paymentClaimsDc;
    }

    @Override
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    @Install(to = "paymentClaimsTable.remove", subject = "enabledRule")
    private boolean paymentClaimsTableRemoveEnabledRule() {
        List<PaymentRegisterDetail> paymentRegisterDetailList = dataManager.load(PaymentRegisterDetail.class)
                .query("select e from finance_PaymentRegisterDetail e where e.paymentClaim in :paymentClaim")
                .parameter("paymentClaim", paymentClaimsTable.getSelected())
                .view("_base")
                .list();
        return paymentRegisterDetailList.isEmpty();
    }

    @Subscribe("paymentClaimsTable.copy")
    public void onPaymentClaimsTableCopy(Action.ActionPerformedEvent event) {
        PaymentClaim selectedClaim = paymentClaimsTable.getSingleSelected();
        if (!Objects.isNull(selectedClaim)) {
            PaymentClaim paymentClaim = dataManager.create(PaymentClaim.class);
            paymentClaim.setOnDate(selectedClaim.getOnDate());
            paymentClaim.setBusiness(selectedClaim.getBusiness());
            paymentClaim.setCompany(selectedClaim.getCompany());
            paymentClaim.setClient(selectedClaim.getClient());
            paymentClaim.setAccount(selectedClaim.getAccount());
            paymentClaim.setCurrency(selectedClaim.getCurrency());
            paymentClaim.setSumm(selectedClaim.getSumm());
            paymentClaim.setPlanPaymentDate(selectedClaim.getPlanPaymentDate());
            paymentClaim.setPaymentPurpose(selectedClaim.getPaymentPurpose());
            paymentClaim.setCashFlowItem(selectedClaim.getCashFlowItem());
            paymentClaim.setCashFlowItemBusiness(selectedClaim.getCashFlowItemBusiness());
            paymentClaim.setPaymentType(selectedClaim.getPaymentType());
            paymentClaim.setComment(selectedClaim.getComment());
            paymentClaim.setNumber(uniqueNumbersService.getNextNumber(PaymentClaim.class.getSimpleName()));
            paymentClaim.setBudgetAnalitic(selectedClaim.getBudgetAnalitic());
            paymentClaim = dataManager.commit(paymentClaim);
            paymentClaimsDl.load();
            if (paymentClaimsDc.containsItem(paymentClaim)) {
                paymentClaimsTable.setSelected(paymentClaim);
            }
        }
    }

    @Install(to = "paymentClaimsTable.copy", subject = "enabledRule")
    private boolean paymentClaimsTableCopyEnabledRule() {
        if (!Objects.isNull(paymentClaimsTable.getSingleSelected())) {
            return true;
        }
        return false;
    }

}