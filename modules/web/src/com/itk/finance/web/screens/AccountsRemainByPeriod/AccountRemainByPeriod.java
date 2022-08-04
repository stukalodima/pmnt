package com.itk.finance.web.screens.AccountsRemainByPeriod;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.itk.finance.entity.AccountRemains;
import com.itk.finance.entity.Business;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.Date;
import java.util.Objects;

@UiController("finance_AccountRemainByPeriod")
@UiDescriptor("account-remain-by-period.xml")
public class AccountRemainByPeriod extends Screen {
    @Inject
    private CollectionLoader<AccountRemains> accountRemainsesDl;
    @Inject
    private LookupField<Business> business;
    @Inject
    private CollectionLoader<Business> businessesDl;
    @Inject
    private GroupTable<AccountRemains> accountRemainsesTable;
    @Inject
    private Messages messages;
    @Inject
    private DateField<Date> startDate;
    @Inject
    private DateField<Date> endDate;
    @Inject
    private UserPropertyService userPropertyService;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        setColumnName();
        business.setValue(userPropertyService.getDefaultBusiness());
        reloadData(business.getValue(), startDate.getValue(), endDate.getValue());
    }

    @Subscribe("startDate")
    public void onStartDateValueChange(HasValue.ValueChangeEvent<Date> event) {
        reloadData(business.getValue(), startDate.getValue(), endDate.getValue());
    }

    @Subscribe("endDate")
    public void onEndDateValueChange(HasValue.ValueChangeEvent<Date> event) {
        reloadData(business.getValue(), startDate.getValue(), endDate.getValue());
    }

    private void reloadData(Business business, Date startDate, Date endDate) {
        if (!Objects.isNull(business)) {
            accountRemainsesDl.setParameter("business", business);
        } else {
            accountRemainsesDl.removeParameter("business");
        }
        if (!Objects.isNull(startDate)) {
            accountRemainsesDl.setParameter("startDate", startDate);
        } else {
            accountRemainsesDl.removeParameter("startDate");
        }
        if (!Objects.isNull(endDate)) {
            accountRemainsesDl.setParameter("endDate", endDate);
        } else {
            accountRemainsesDl.removeParameter("endDate");
        }
        accountRemainsesDl.load();
        businessesDl.load();
    }

    @Subscribe("business")
    public void onBusinessValueChange(HasValue.ValueChangeEvent<Business> event) {
        reloadData(event.getValue(), startDate.getValue(), endDate.getValue());
        if (!Objects.isNull(event.getValue())) {
            accountRemainsesDl.setParameter("business", business);
        } else {
            accountRemainsesDl.removeParameter("business");
        }
    }

    private void setColumnName() {
        accountRemainsesTable.getColumn("summEqualsUAH")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountRemainByPeriod.class, "summEqualsUAH.caption") +
                        "<br>" + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summInUAH")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountRemainByPeriod.class, "summInUAH.caption") +
                        "<br>" + " (UAH)" + "</div"
                );
        accountRemainsesTable.getColumn("summInUSD")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountRemainByPeriod.class, "summInUSD.caption") +
                        "<br>" + " (USD)" + "</div"
                );
        accountRemainsesTable.getColumn("summInEUR")
                .setCaption("<div style=\"text-align: right\">" +
                        messages.getMessage(AccountRemainByPeriod.class, "summInEUR.caption") +
                        "<br>" + " (EUR)" + "</div"
                );
    }
}