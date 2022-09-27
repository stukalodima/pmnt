package com.itk.finance.web.screens.paymentclaim;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@UiController("finance_PaymentClaim.checkCashFlow")
@UiDescriptor("payment-claim-check-cash-flow.xml")
public class PaymentClaimCheckCashFlow extends Screen {
    @Inject
    private CollectionContainer<PaymentClaim> paymentClaimsDc;
    @Inject
    private DataManager dataManager;
    @Inject
    private Table<PaymentClaim> paymentClaimsTable;
    @Inject
    private UiComponents uiComponents;
    private List<PaymentClaim> paymentClaimForCheckList;

    @Subscribe
    public void onInit(InitEvent event) {
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if (paymentClaimForCheckList.size() > 0) {
            for (PaymentClaim paymentClaim : paymentClaimForCheckList) {
                paymentClaimsDc.getMutableItems().add(paymentClaim);
            }

            paymentClaimsTable.addGeneratedColumn("cashFlowItem", entity -> {
                LookupField<CashFlowItem> field = uiComponents.create(LookupField.NAME);
                List<CashFlowItem> cashFlowItemList = dataManager.load(CashFlowItem.class)
                        .query("select e.cashFlowItem from finance_CashFlowItemBusinessAlternativeValues e " +
                                "where e.cashFlowItemBusiness = :cashFlowItemBusiness")
                        .parameter("cashFlowItemBusiness", entity.getCashFlowItemBusiness())
                        .view("_local")
                        .list();
                CashFlowItemBusiness cashFlowItemBusiness = dataManager.reload(entity.getCashFlowItemBusiness(), "cashFlowItemBusiness-all-property");
                if (!Objects.isNull(cashFlowItemBusiness.getCashFlowItem())
                        && !cashFlowItemList.contains(cashFlowItemBusiness.getCashFlowItem())) {
                    cashFlowItemList.add(cashFlowItemBusiness.getCashFlowItem());
                }
                cashFlowItemList.sort(Comparator.comparing(CashFlowItem::getName));
                field.setOptionsList(cashFlowItemList);
                field.setValueSource(new ContainerValueSource<>(paymentClaimsTable.getInstanceContainer(entity), "cashFlowItem"));
                return field;
            });
        }
    }

    @Subscribe("saveBtn")
    public void onSaveBtnClick(Button.ClickEvent event) {
        List<PaymentClaim> paymentClaimList = paymentClaimsDc.getMutableItems();
        CommitContext commitContext = new CommitContext();
        paymentClaimList.forEach(e -> {
            PaymentClaim paymentClaimOriginal = dataManager.load(PaymentClaim.class)
                    .query("select e from finance_PaymentClaim e " +
                            "where e = :paymentClaim")
                    .parameter("paymentClaim", e)
                    .view("paymentClaim.getEdit")
                    .one();
            if (!e.getCashFlowItem().equals(paymentClaimOriginal.getCashFlowItem())){
                commitContext.addInstanceToCommit(e);
            }
        });
        dataManager.commit(commitContext);
        close(StandardOutcome.COMMIT);
    }

    public void setPaymentClaimForCheckList(List<PaymentClaim> paymentClaimForCheckList) {
        this.paymentClaimForCheckList = paymentClaimForCheckList;
    }
}