package com.itk.finance.web.screens.paymentregister;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.entity.RegisterType;
import com.itk.finance.service.PaymentClaimService;
import com.itk.finance.service.PaymentRegisterService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("finance_PaymentRegister.browse")
@UiDescriptor("payment-register-browse.xml")
@LookupComponent("paymentRegistersTable")
@LoadDataBeforeShow
public class PaymentRegisterBrowse extends StandardLookup<PaymentRegister> {
    @Inject
    private CollectionLoader<PaymentRegister> paymentRegistersDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Inject
    private PaymentRegisterService paymentRegisterService;

    @SuppressWarnings("unused")
    @Install(to = "paymentRegistersTable.edit", subject = "afterCommitHandler")
    private void paymentRegistersTableEditAfterCommitHandler(PaymentRegister paymentRegister) {
        paymentRegistersDl.load();
    }

    @SuppressWarnings("all")
    @Subscribe("paymentRegistersTable.createAllRegisterByType")
    public void onPaymentRegistersTableCreateAllRegisterByType(Action.ActionPerformedEvent event) {
        PaymentClaimService paymentClaimService = AppBeans.get(PaymentClaimService.class);
        List<RegisterType> registerTypeList = dataManager.load(RegisterType.class)
                .query("select e from finance_RegisterType e")
                .view("registerType-all-property")
                .list();
        List paymentRegisterList = new ArrayList<>();
        registerTypeList.forEach(e -> {
                    PaymentRegister paymentRegister = dataManager.create(PaymentRegister.class);
                    paymentRegister.setRegisterType(e);
                    List<PaymentClaim> paymentClaimList = paymentClaimService.getPaymentClaimsListByRegister(
                            paymentRegister.getBusiness(), paymentRegister.getRegisterType()
                    );
                    if (paymentClaimList.size() > 0) {
                        paymentRegister.setNumber(uniqueNumbersService.getNextNumber(PaymentRegister.class.getSimpleName()));
                        paymentRegister.setPaymentRegisters(
                                paymentRegisterService.getPaymentRegisterDetailsByPaymentClaimList(
                                        paymentRegister, paymentClaimList
                                )
                        );
                        paymentRegister.setSumma(null);

                        paymentRegisterList.add(paymentRegister);
                        paymentRegisterList.addAll(paymentRegister.getPaymentRegisters());
                    }
                }
        );
        CommitContext commitContext = new CommitContext(paymentRegisterList);
        dataManager.commit(commitContext);
        paymentRegistersDl.load();
    }
}