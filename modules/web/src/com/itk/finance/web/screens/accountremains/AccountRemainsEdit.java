package com.itk.finance.web.screens.accountremains;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.itk.finance.entity.*;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UiController("finance_AccountRemains.edit")
@UiDescriptor("account-remains-edit.xml")
@EditedEntityContainer("accountRemainsDc")
@LoadDataBeforeShow
public class AccountRemainsEdit extends StandardEditor<AccountRemains> {
    @Inject
    private TimeSource timeSource;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private DataManager dataManager;
    @Inject
    private Metadata metadata;

    @Subscribe
    public void onInitEntity(InitEntityEvent<AccountRemains> event) {
        event.getEntity().setOnDate(timeSource.currentTimestamp());
        event.getEntity().setBussines(userPropertyService.getDefaultBusiness());
        if (!Objects.isNull(userPropertyService.getDefaultBusiness())) {

            List<Account> accountList = dataManager.load(Account.class)
                    .query("select e from finance_Account e where e.company.business = :business ")
                    .parameter("business", userPropertyService.getDefaultBusiness())
                    .view("account-all-property")
                    .list();

            List<AccountRemainsDetail> accountRemainsDetails = new ArrayList<>();

            for (Account account : accountList) {
                AccountRemainsDetail accountRemainsDetail = metadata.create(AccountRemainsDetail.class);
                accountRemainsDetail.setAccountRemains(event.getEntity());
                accountRemainsDetail.setCompany(account.getCompany());
                accountRemainsDetail.setAccount(account);

                accountRemainsDetails.add(accountRemainsDetail);
            }
            event.getEntity().setAccounts(accountRemainsDetails);
        }
    }
}