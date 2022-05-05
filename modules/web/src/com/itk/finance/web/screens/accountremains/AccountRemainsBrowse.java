package com.itk.finance.web.screens.accountremains;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountRemains;
import de.diedavids.cuba.dataimport.web.WithImport;
import de.diedavids.cuba.dataimport.web.WithImportWizard;
import de.diedavids.cuba.dataimport.web.action.WithImportWizardExecution;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.Supplier;

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
}