package com.itk.finance.web.screens.currencyrate;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CurrencyRate;

@UiController("finance_CurrencyRate.browse")
@UiDescriptor("currency-rate-browse.xml")
@LookupComponent("currencyRatesTable")
@LoadDataBeforeShow
public class CurrencyRateBrowse extends StandardLookup<CurrencyRate> {
}