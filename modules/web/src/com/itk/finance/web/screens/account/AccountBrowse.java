package com.itk.finance.web.screens.account;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Account;
import com.itk.finance.service.AccountsService;

import javax.inject.Inject;
import java.io.IOException;

@UiController("finance_Account.browse")
@UiDescriptor("account-browse.xml")
@LookupComponent("accountsTable")
@LoadDataBeforeShow
public class AccountBrowse extends StandardLookup<Account> {
    @Inject
    private AccountsService accountsService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;
    @Inject
    private CollectionLoader<Account> accountsDl;

    @Subscribe("accountsTable.getAccountList")
    public void onAccountsTableGetAccountList(Action.ActionPerformedEvent event) {
        try {
            accountsService.getCompanyAccountsListFromExternal();
        } catch (IOException e) {
            dialogs.createMessageDialog()
                    .withCaption(messages.getMessage(AccountBrowse.class, "messages.getBankListError.caption"))
                    .withMessage(messages.getMessage(AccountBrowse.class, "messages.getBankListError.text")
                            + "\n" + e.getMessage())
                    .show();
        }
        accountsDl.load();
    }
}