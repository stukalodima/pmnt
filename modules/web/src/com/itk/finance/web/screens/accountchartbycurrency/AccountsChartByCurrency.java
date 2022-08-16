package com.itk.finance.web.screens.accountchartbycurrency;

import com.haulmont.charts.gui.components.charts.Chart;
import com.haulmont.charts.gui.components.charts.PieChart;
import com.haulmont.charts.gui.data.ListDataProvider;
import com.haulmont.charts.gui.data.MapDataItem;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.web.screens.accountchart.AccountChart;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@UiController("finance_AccountsChartByCurrency")
@UiDescriptor("accounts-chart-by-currency.xml")
public class AccountsChartByCurrency extends Screen {
    @Inject
    private DateField<Date> onDate;
    @Inject
    private TimeSource timeSource;
    @Inject
    private KeyValueCollectionLoader accountsRemainsByBankDl;
    @Inject
    private Table gtAccountsRemains;
    @Inject
    private Messages messages;
    @Inject
    private KeyValueCollectionContainer accountsRemainsByBankDc;
    @Inject
    private CheckBox blockMoneyField;

    @Install(to = "gtAccountsRemains", subject = "styleProvider")
    private String gtAccountsRemainsStyleProvider(Entity entity, String property) {
        if (property == null) {
            return null;
        } else {
            if (property.equals("today")) {
                return "remains-on-date";
            }
        }
        return null;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        onDate.setValue(timeSource.currentTimestamp());
        blockMoneyField.setValue(true);
        updateDate();
//        pie3dChart.
    }

    @Subscribe("blockMoneyField")
    public void onBlockMoneyFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.isUserOriginated()) {
            updateDate();
        }
    }

    private void updateDate() {
        accountsRemainsByBankDl.setParameter("onDate", onDate.getValue());
        if (Boolean.TRUE.equals(blockMoneyField.getValue())) {
            accountsRemainsByBankDl.removeParameter("stan");
        } else {
            accountsRemainsByBankDl.setParameter("stan", "Нормальний");
        }
        accountsRemainsByBankDl.load();
        accountsRemainsByBankDc.getMutableItems().forEach(e->{
            e.setValue("today", Math.round((Double)e.getValue("today")));
            e.setValue("summ", Math.round((Double)e.getValue("summ")));
        });
        setColumnName(onDate.getValue());
    }

    @Subscribe("onDate")
    public void onOnDateValueChange(HasValue.ValueChangeEvent<Date> event) {
        updateDate();
    }

    private void setColumnName(Date columnDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date beforeColumnDate = addDays(columnDate, -1);
        gtAccountsRemains.getColumn("before")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsChartByCurrency.class, "gtAccountsRemains.before") +
                        "<br>" + dateFormat.format(beforeColumnDate) + " (UAH)" + "</div"
                );
        gtAccountsRemains.getColumn("today")
                .setCaption("<div style=\"text-align: right; color: green;\">" +
                        messages.getMessage(AccountsChartByCurrency.class, "gtAccountsRemains.today") +
                        "<br>" + dateFormat.format(columnDate) + " (UAH)" + "</div"
                );
        gtAccountsRemains.getColumn("todayUAH")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsChartByCurrency.class, "gtAccountsRemains.todayUAH") +
                        "<br>" + dateFormat.format(columnDate) + " (UAH)" + "</div"
                );
        gtAccountsRemains.getColumn("todayUSD")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsChartByCurrency.class, "gtAccountsRemains.todayUSD") +
                        "<br>" + dateFormat.format(columnDate) + " (USD)" + "</div"
                );
        gtAccountsRemains.getColumn("todayEUR")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsChartByCurrency.class, "gtAccountsRemains.todayEUR") +
                        "<br>" + dateFormat.format(columnDate) + " (EUR)" + "</div"
                );
    }

    @Subscribe("pie3dChart")
    public void onPie3dChartLegendItemShow(Chart.LegendItemShowEvent event) {

    }



    public static Date addDays(Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}