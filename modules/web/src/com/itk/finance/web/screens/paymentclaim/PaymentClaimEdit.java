package com.itk.finance.web.screens.paymentclaim;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.entity.*;
import com.itk.finance.service.ProcPropertyService;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.Date;
import java.util.Objects;

@UiController("finance_PaymentClaim.edit")
@UiDescriptor("payment-claim-edit.xml")
@EditedEntityContainer("paymentClaimDc")
@LoadDataBeforeShow
public class PaymentClaimEdit extends StandardEditor<PaymentClaim> {
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private TimeSource timeSource;
    @Inject
    private LookupPickerField<Company> companyField;
    @Inject
    private UserSession userSession;
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
    private ProcPropertyService procPropertyService;

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (entityStates.isNew(getEditedEntity())) {
            getEditedEntity().setNumber(uniqueNumbersService.getNextNumber(PaymentClaim.class.getSimpleName()));
            event.resume();
        }
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<PaymentClaim> event) {
        event.getEntity().setOnDate(timeSource.currentTimestamp());
        event.getEntity().setPlanPaymentDate(new Date(timeSource.currentTimeMillis() + 24 * 60 * 60 * 1000));
        event.getEntity().setStatus(procPropertyService.getNewStatus());
        event.getEntity().setAuthor(userSession.getUser());

        Company company = userPropertyService.getDefaultCompany();

        if (company != null) {
            event.getEntity().setCompany(company);
            event.getEntity().setBusiness(company.getBusiness());
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
    }

    private void refreshForm(Business thisBusiness, Company company) {
        companiesDl.setParameter("business", thisBusiness);
        companiesDl.load();
        accountsDl.setParameter("company", company);
        accountsDl.load();
    }
}