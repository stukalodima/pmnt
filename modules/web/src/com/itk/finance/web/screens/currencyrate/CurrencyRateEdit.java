package com.itk.finance.web.screens.currencyrate;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CurrencyRate;

@UiController("finance_CurrencyRate.edit")
@UiDescriptor("currency-rate-edit.xml")
@EditedEntityContainer("currencyRateDc")
@LoadDataBeforeShow
public class CurrencyRateEdit extends StandardEditor<CurrencyRate> {
}