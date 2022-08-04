package com.itk.finance.web.screens.currencyratechartbyperiod;

import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.itk.finance.entity.CurrencyRate;

import javax.inject.Inject;

@UiController("finance_CurrencyRateByPeriod")
@UiDescriptor("currency-rate-by-period.xml")
public class CurrencyRateByPeriod extends Screen {

    @Inject
    private CollectionLoader<CurrencyRate> dateValueUSDLoader;
    @Inject
    private CollectionLoader<CurrencyRate> dateValueRUBLoader;
    @Inject
    private CollectionLoader<CurrencyRate> dateValueEURLoader;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        dateValueUSDLoader.load();
        dateValueEURLoader.load();
        dateValueRUBLoader.load();
    }
}