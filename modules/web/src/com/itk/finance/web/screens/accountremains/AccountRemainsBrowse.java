package com.itk.finance.web.screens.accountremains;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountRemains;
import de.diedavids.cuba.dataimport.web.WithImportWizard;

import javax.inject.Inject;
import java.util.List;

@UiController("finance_AccountRemains.browse")
@UiDescriptor("account-remains-browse.xml")
@LookupComponent("accountRemainsesTable")
@LoadDataBeforeShow
public class AccountRemainsBrowse extends StandardLookup<AccountRemains> implements WithImportWizard {

    @Inject
    private GroupTable<AccountRemains> accountRemainsesTable;
    @Inject
    private CollectionContainer<AccountRemains> accountRemainsesDc;
    @Inject
    private ButtonsPanel buttonsPanel;
    @Inject
    private DataManager dataManager;

    @Override
    public ListComponent getListComponent() {
        return accountRemainsesTable;
    }

    @Override
    public CollectionContainer getCollectionContainer() {
        return accountRemainsesDc;
    }

    @Override
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    @Subscribe("accountRemainsesTable.cheakSumma")
    public void onAccountRemainsesTableCheakSumma(Action.ActionPerformedEvent event) {
        List<AccountRemains> accountRemainsList = dataManager.load(AccountRemains.class)
                .query("select e from finance_AccountRemains e order by e.onDate")
                .view("accountRemains-all-property")
                .list();
        accountRemainsList.forEach(AccountRemains::fillSummPreCommit);
        dataManager.commit(new CommitContext(accountRemainsList));
    }
}