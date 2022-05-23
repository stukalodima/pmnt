package com.itk.finance.web.screens.accountremains;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Account;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Currency;
import com.itk.finance.entity.CurrencyRate;
import com.itk.finance.service.AccountsService;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@UiController("finance_AccountRemains.edit")
@UiDescriptor("account-remains-edit.xml")
@EditedEntityContainer("accountRemainsDc")
@LoadDataBeforeShow
public class AccountRemainsEdit extends StandardEditor<AccountRemains> {
    @Inject
    private TimeSource timeSource;
    @Inject
    private AccountsService accountsService;
    @Inject
    private InstanceContainer<AccountRemains> accountRemainsDc;

    private void getBeforSummValue(AccountRemains entity, Account account, Date onDate) {
            entity.setSummBefor(accountsService.getBeforSummValue(account, onDate));
    }

    @Subscribe("onDateField")
    public void onOnDateFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (event.isUserOriginated()) {
            getBeforSummValue(getEditedEntity(), getEditedEntity().getAccount(), event.getValue());
        }
    }

    @Subscribe("accountField")
    public void onAccountFieldValueChange(HasValue.ValueChangeEvent<Account> event) {
        if (event.isUserOriginated()) {
            getBeforSummValue(getEditedEntity(), event.getValue(), getEditedEntity().getOnDate());
        }
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (Objects.isNull(getEditedEntity().getSummInUAH()) || Objects.isNull(getEditedEntity().getSummInUSD()) || Objects.isNull(getEditedEntity().getSumInEUR())) {
            getEditedEntity().fillSummPreCommit();
            event.getDataContext().setModified(getEditedEntity(), true);
        }
        event.resume();
    }

    @Subscribe("summField")
    public void onSummFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        if (event.isUserOriginated()) {
            getEditedEntity().fillSummPreCommit();
            accountRemainsDc.setItem(getEditedEntity());
        }
    }
}