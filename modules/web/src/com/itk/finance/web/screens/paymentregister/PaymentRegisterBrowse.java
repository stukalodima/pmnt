package com.itk.finance.web.screens.paymentregister;

import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;
import com.itk.finance.service.PaymentClaimService;
import com.itk.finance.service.PaymentRegisterService;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@UiController("finance_PaymentRegister.browse")
@UiDescriptor("payment-register-browse.xml")
@LookupComponent("paymentRegistersTable")
@LoadDataBeforeShow
public class PaymentRegisterBrowse extends StandardLookup<PaymentRegister> {
    @Inject
    private GroupTable<PaymentRegister> paymentRegistersTable;
    @Inject
    private CollectionLoader<PaymentRegister> paymentRegistersDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Inject
    private PaymentRegisterService paymentRegisterService;
    @Inject
    private PaymentClaimService paymentClaimService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;

    @SuppressWarnings("unused")
    @Install(to = "paymentRegistersTable.edit", subject = "afterCommitHandler")
    private void paymentRegistersTableEditAfterCommitHandler(PaymentRegister paymentRegister) {
        paymentRegistersDl.load();
    }

    @Install(to = "paymentRegistersTable.remove", subject = "enabledRule")
    private boolean paymentRegistersTableRemoveEnabledRule() {
        boolean enable = true;
        Set<PaymentRegister> paymentRegisterList = paymentRegistersTable.getSelected();
        for (PaymentRegister paymentRegister : paymentRegisterList) {
            paymentRegister = dataManager.reload(paymentRegister, "paymentRegister-all-property");
            ProcInstance procInstance = paymentRegister.getProcInstance();
            if (!Objects.isNull(procInstance)) {
                procInstance = dataManager.reload(paymentRegister.getProcInstance(), "procInstance-full");
                if (!Objects.isNull(procInstance.getEndDate())) {
                    enable = false;
                }
            }
        }
        return enable;
    }

    @Subscribe("paymentRegistersTable.createAllRegisterByType")
    public void onPaymentRegistersTableCreateAllRegisterByType(Action.ActionPerformedEvent event) {
        List<Business> businessList = dataManager.load(Business.class)
                .query("select e from finance_Business e")
                .view("_local")
                .list();
        if (businessList.isEmpty()) {
            return;
        }
        if (businessList.size() > 1) {
            InputParameter parameter = InputParameter.entityParameter("business", Business.class);
            InputDialog inputDialog = dialogs
                    .createInputDialog(this)
                    .withCaption(messages.getMessage(PaymentRegisterBrowse.class, "paymentRegisterBrowse.input.business.caption"))
                    .withParameter(parameter)
                    .build();
            inputDialog.setResultHandler(inputDialogResult -> createRegisterByBusiness(inputDialogResult.getValue("business")));
            inputDialog.show();
        } else {
            createRegisterByBusiness(null);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void createRegisterByBusiness(Business business) {
        List<RegisterType> registerTypeList = dataManager.load(RegisterType.class)
                .query("select e from finance_RegisterType e")
                .view("registerType-all-property")
                .list();
        List paymentRegisterList = new ArrayList<>();
        registerTypeList.forEach(e -> {
                    PaymentRegister paymentRegister = dataManager.create(PaymentRegister.class);
                    paymentRegister.setRegisterType(e);
                    if (!Objects.isNull(business)) {
                        paymentRegister.setBusiness(business);
                    }
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

    @Subscribe
    public void onInit(InitEvent event) {
        paymentRegistersTable.setStyleProvider(new GroupTable.GroupStyleProvider<PaymentRegister>() {
            @Nullable
            @Override
            public String getStyleName(@SuppressWarnings("NullableProblems") PaymentRegister entity, @Nullable String property) {
                if (property == null) {
                    if (entity.getPayedStatus() == null) {
                        return null;
                    }
                    switch (entity.getPayedStatus()) {
                        case PAYED:
                            return "approved1";
                        case PRE_PAYED:
                            return "startProc1";
                        case DISMISS:
                            return "terminated1";
                    }
                }
                return null;
            }

            @Nullable
            @Override
            public String getStyleName(@SuppressWarnings("NullableProblems") GroupInfo info) {
                MetaPropertyPath metaPropertyPath = (MetaPropertyPath) info.getProperty();
                if ("payedStatus".equals(metaPropertyPath.toPathString())) {
                    return PaymentRegisterHelper.getGroupTableStyleByPayedStatus(info);
                }
                return null;
            }
        });
    }
}