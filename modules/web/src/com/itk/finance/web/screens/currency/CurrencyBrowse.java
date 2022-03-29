package com.itk.finance.web.screens.currency;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Currency;

@UiController("finance_Currency.browse")
@UiDescriptor("currency-browse.xml")
@LookupComponent("currenciesTable")
@LoadDataBeforeShow
public class CurrencyBrowse extends StandardLookup<Currency> {
}