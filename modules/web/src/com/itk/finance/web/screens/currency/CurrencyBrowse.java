package com.itk.finance.web.screens.currency;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Currency;
import com.itk.finance.service.CurrencyService;

import javax.inject.Inject;
import java.io.IOException;

@UiController("finance_Currency.browse")
@UiDescriptor("currency-browse.xml")
@LookupComponent("currenciesTable")
@LoadDataBeforeShow
public class CurrencyBrowse extends StandardLookup<Currency> {
    @Inject
    private CurrencyService currencyService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;
    @Inject
    private CollectionLoader<Currency> currenciesDl;

    @Subscribe("currenciesTable.getCurrencyList")
    public void onCurrenciesTableGetCurrencyList(Action.ActionPerformedEvent event) {
        try {
            currencyService.getCurrencyListFromExternal();
        } catch (IOException e) {
            dialogs.createMessageDialog()
                    .withCaption(messages.getMessage(CurrencyBrowse.class, "messages.getCurrencyListError.caption"))
                    .withMessage(messages.getMessage(CurrencyBrowse.class, "messages.getCurrencyListError.text")
                            + "\n" + e.getMessage())
                    .show();
        }
        currenciesDl.load();
    }
}