package com.itk.finance.web.screens.currency;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Currency;

@UiController("finance_Currency.edit")
@UiDescriptor("currency-edit.xml")
@EditedEntityContainer("currencyDc")
@LoadDataBeforeShow
public class CurrencyEdit extends StandardEditor<Currency> {
}