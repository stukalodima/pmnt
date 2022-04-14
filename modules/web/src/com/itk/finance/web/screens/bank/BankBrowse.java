package com.itk.finance.web.screens.bank;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Bank;
import com.itk.finance.service.BankService;
import com.itk.finance.web.screens.company.CompanyBrowse;

import javax.inject.Inject;
import java.io.IOException;

@UiController("finance_Bank.browse")
@UiDescriptor("bank-browse.xml")
@LookupComponent("banksTable")
@LoadDataBeforeShow
public class BankBrowse extends StandardLookup<Bank> {
    @Inject
    private BankService bankService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;
    @Inject
    private CollectionLoader<Bank> banksDl;

    @Subscribe("banksTable.getBankList")
    public void onBanksTableGetBankList(Action.ActionPerformedEvent event) {
        try {
            bankService.getBankListFromExternal();
        } catch (IOException e) {
            dialogs.createMessageDialog()
                    .withCaption(messages.getMessage(BankBrowse.class, "messages.getBankListError.caption"))
                    .withMessage(messages.getMessage(BankBrowse.class, "messages.getBankListError.text")
                            + "\n" + e.getMessage())
                    .show();
        }
        banksDl.load();
    }
}