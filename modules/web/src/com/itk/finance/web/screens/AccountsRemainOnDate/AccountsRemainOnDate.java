package com.itk.finance.web.screens.AccountsRemainOnDate;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.itk.finance.entity.*;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@UiController("finance_AccountsRemainOnDate")
@UiDescriptor("accounts-remain-on-date.xml")
public class AccountsRemainOnDate extends Screen {
    @Inject
    private CollectionLoader<AccountRemains> accountRemainsesDl;
    @Inject
    private DateField<Date> onDate;
    @Inject
    private TimeSource timeSource;
    @Inject
    private LookupField<Business> business;
    @Inject
    private LookupField<Company> company;
    @Inject
    private LookupField<Currency> currency;
    @Inject
    private CollectionLoader<Business> businessesDl;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private CollectionLoader<Currency> currenciesDl;
    @Inject
    private GroupTable<AccountRemains> accountRemainsesTable;
    @Inject
    private Messages messages;
    @Inject
    private CollectionLoader<AccountType> accountsTypeDl;
    @Inject
    private PopupView popupView;
    @Inject
    private CheckBoxGroup<AccountType> accountsTypeListBox;
    @Inject
    private TextField<String> accTypeStr;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        onDate.setValue(timeSource.currentTimestamp());
        setColumnName(onDate.getValue());
        reloadData(business.getValue(), company.getValue(), currency.getValue(), null, accountsTypeListBox.getValue());
        accountsTypeDl.load();
    }

    @Subscribe("showPopup")
    public void onShowPopupClick(Button.ClickEvent event) {
        popupView.setPopupVisible(!popupView.isPopupVisible());
    }

    @Subscribe("accountsTypeListBox")
    public void onAccountsTypeListBoxValueChange(HasValue.ValueChangeEvent<Collection<AccountType>> event) {
        Collection<AccountType> listAccType = accountsTypeListBox.getValue();
        String listStr = "";
        if (listAccType != null) {
            for (AccountType accountType : listAccType) {
                listStr = String.join(";", accountType.getName(), listStr);
            }
        } else {
            listStr = messages.getMessage(AccountsRemainOnDate.class, "accTypeStr");
        }
        accTypeStr.setValue(listStr);
        accTypeStr.setDescription(listStr);
        reloadData(business.getValue(), company.getValue(), currency.getValue(), onDate.getValue(), accountsTypeListBox.getValue());
    }

    private void reloadData(Business business, Company company, Currency currency, Date date, Collection<AccountType> listAccType) {
        if (!Objects.isNull(business)) {
            accountRemainsesDl.setParameter("business", business);
        } else {
            accountRemainsesDl.removeParameter("business");
        }
        if (!Objects.isNull(company)) {
            accountRemainsesDl.setParameter("company", company);
        } else {
            accountRemainsesDl.removeParameter("company");
        }
        if (!Objects.isNull(currency)) {
            accountRemainsesDl.setParameter("currency", currency);
        } else {
            accountRemainsesDl.removeParameter("currency");
        }
        if (!Objects.isNull(date)) {
            accountRemainsesDl.setParameter("onDate", date);
        } else {
            accountRemainsesDl.setParameter("onDate", timeSource.currentTimestamp());
        }
        if (listAccType != null) {
            accountRemainsesDl.setParameter("accTypeList", listAccType);
        } else {
            accountRemainsesDl.removeParameter("accTypeList");
        }
        accountRemainsesDl.load();
        businessesDl.load();
        companiesDl.load();
        currenciesDl.load();
    }

    @Subscribe("onDate")
    public void onOnDateValueChange(HasValue.ValueChangeEvent<Date> event) {
        setColumnName(event.getValue());
        reloadData(business.getValue(), company.getValue(), currency.getValue(), event.getValue(), accountsTypeListBox.getValue());
    }

    @Subscribe("business")
    public void onBusinessValueChange(HasValue.ValueChangeEvent<Business> event) {
        reloadData(event.getValue(), company.getValue(), currency.getValue(), onDate.getValue(), accountsTypeListBox.getValue());
        if (!Objects.isNull(event.getValue())) {
            accountRemainsesDl.setParameter("business", business);
        } else {
            accountRemainsesDl.removeParameter("business");
        }
    }

    @Subscribe("company")
    public void onCompanyValueChange(HasValue.ValueChangeEvent<Company> event) {
        reloadData(business.getValue(), event.getValue(), currency.getValue(), onDate.getValue(), accountsTypeListBox.getValue());
    }

    @Subscribe("currency")
    public void onCurrencyValueChange(HasValue.ValueChangeEvent<Currency> event) {
        reloadData(business.getValue(), company.getValue(), event.getValue(), onDate.getValue(), accountsTypeListBox.getValue());
    }

    private void setColumnName(Date columnDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date beforColumnDate = addDays(columnDate, -1);
        accountRemainsesTable.getColumn("summBefor")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summBefor.caption") +
                        "<br>" + dateFormat.format(beforColumnDate) + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summEqualsUAH")
                .setCaption("<div style=\"text-align: right; color: green;\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summEqualsUAH.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summInUAH")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summInUAH.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summInUSD")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summInUSD.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (USD)" + "</div"
                );
        accountRemainsesTable.getColumn("summInEUR")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountsRemainOnDate.class, "summInEUR.caption") +
                        "<br>" + dateFormat.format(columnDate) + " (EUR)" + "</div"
                );
    }

    public static Date addDays(Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    @Subscribe
    public void onInit(InitEvent event) {
        accountRemainsesTable.setStyleProvider(new GroupTable.GroupStyleProvider<AccountRemains>() {
            @Nullable
            @Override
            public String getStyleName(AccountRemains entity, @Nullable String property) {
                if (!Objects.isNull(property) && property.equals("summEqualsUAH")) {
                    return "remains-on-date";
                }
                return null;
            }

            @Nullable
            @Override
            public String getStyleName(GroupInfo info) {
                if (info.getProperty().equals("summEqualsUAH")) {
                    return "remains-on-date";
                }
                return null;
            }
        });
    }


}