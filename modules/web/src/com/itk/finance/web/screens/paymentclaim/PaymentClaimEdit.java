package com.itk.finance.web.screens.paymentclaim;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@UiController("finance_PaymentClaim.edit")
@UiDescriptor("payment-claim-edit.xml")
@EditedEntityContainer("paymentClaimDc")
@LoadDataBeforeShow
public class PaymentClaimEdit extends StandardEditor<PaymentClaim> {
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private LookupPickerField<Company> companyField;
    @Inject
    private EntityStates entityStates;
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Inject
    private CollectionLoader<Account> accountsDl;
    @Inject
    private LookupPickerField<Account> accountField;
    @Inject
    private LookupPickerField<Currency> currencyField;
    @Inject
    private Form formBody;
    @Inject
    private DataManager dataManager;
    @Inject
    private PickerField<PaymentRegister> paymentRegisterField;
    @Inject
    private TextField<String> approvedField;
    @Inject
    private TextArea<String> commentTextField;
    @Inject
    private Messages messages;

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            getEditedEntity().setNumber(uniqueNumbersService.getNextNumber(PaymentClaim.class.getSimpleName()));
            event.resume();
        }
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        if (!Objects.isNull(companyField.getValue()) && !Objects.equals(companyField.getValue().getBusiness(), event.getValue())) {
            companyField.setValue(companyField.getEmptyValue());
        }
        refreshForm(event.getValue(), getEditedEntity().getCompany());
    }

    @Subscribe("companyField")
    public void onCompanyFieldValueChange(HasValue.ValueChangeEvent<Company> event) {
        if (!Objects.isNull(accountField.getValue()) && !Objects.equals(accountField.getValue().getCompany(), event.getValue())) {
            accountField.setValue(accountField.getEmptyValue());
        }
        refreshForm(getEditedEntity().getBusiness(), event.getValue());
    }

    @Subscribe("accountField")
    public void onAccountFieldValueChange(HasValue.ValueChangeEvent<Account> event) {
        if (!Objects.isNull(event.getValue())) {
            currencyField.setValue(event.getValue().getCurrency());
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        refreshForm(getEditedEntity().getBusiness(), getEditedEntity().getCompany());
        boolean enable = Objects.isNull(getEditedEntity().getStatus())
                || (!Objects.isNull(getEditedEntity().getStatus().getIsNew()) && getEditedEntity().getStatus().getIsNew());
        formBody.setEditable(enable);
        fillPaymentRegisterProperty();
    }

    private void fillPaymentRegisterProperty() {
        Optional<PaymentRegisterDetail> paymentRegisterDetail = dataManager.load(PaymentRegisterDetail.class)
                .query("select e from finance_PaymentRegisterDetail e where e.paymentClaim = :paymentClaim")
                .parameter("paymentClaim", getEditedEntity())
                .view("paymentRegisterDetail-view")
                .optional();
        paymentRegisterDetail.ifPresent(registerDetail -> {
            paymentRegisterField.setValue(registerDetail.getPaymentRegister());
            approvedField.setValue(messages.getMessage(registerDetail.getApproved()));
            commentTextField.setValue(registerDetail.getComment());

        });
    }

    private void refreshForm(Business thisBusiness, Company company) {
        companiesDl.setParameter("business", thisBusiness);
        companiesDl.load();
        accountsDl.setParameter("company", company);
        accountsDl.load();
    }
}