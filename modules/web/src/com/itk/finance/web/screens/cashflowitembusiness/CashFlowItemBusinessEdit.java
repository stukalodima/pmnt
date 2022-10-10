package com.itk.finance.web.screens.cashflowitembusiness;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UiController("finance_CashFlowItemBusiness.edit")
@UiDescriptor("cash-flow-item-business-edit.xml")
@EditedEntityContainer("cashFlowItemBusinessDc")
@LoadDataBeforeShow
public class CashFlowItemBusinessEdit extends StandardEditor<CashFlowItemBusiness> {
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private LookupPickerField<Company> companyField;
    @Inject
    private HBoxLayout cashFlowItemBusinessAlternativeValuesBox;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataManager dataManager;
    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionPropertyContainer<CashFlowItemBusinessAlternativeValues> cashFlowItemBusinessAlternativeValuesDc;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;
    @Inject
    private LookupPickerField<CashFlowItem> cashFlowItemField;

    @Subscribe
    public void onInitEntity(InitEntityEvent<CashFlowItemBusiness> event) {
        event.getEntity().setBusiness(userPropertyService.getDefaultBusiness());
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        setParameterCompanyDl();
        setEditableOnAlternativeValues();
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        if (event.isUserOriginated()) {
            if (getEditedEntity().getCompany() != null && !getEditedEntity().getCompany().getBusiness().equals(getEditedEntity().getBusiness())) {
                companyField.clear();
            }
            setParameterCompanyDl();
        }
    }

    @Subscribe("companyField")
    public void onCompanyFieldValueChange(HasValue.ValueChangeEvent<Company> event) {
        if (event.isUserOriginated()) {
            if (getEditedEntity().getCompany() != null && getEditedEntity().getBusiness() == null) {
                getEditedEntity().setBusiness(getEditedEntity().getCompany().getBusiness());
            }
        }
    }

    private void setParameterCompanyDl() {
        companiesDl.setParameter("business", getEditedEntity().getBusiness());
        companiesDl.load();
    }

    @Subscribe("checkCashFlowItemField")
    public void onCheckCashFlowItemFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (!getEditedEntity().getCheckCashFlowItem() && Objects.requireNonNull(cashFlowItemBusinessAlternativeValuesDc.getItems()).size() > 0) {
            dialogs.createOptionDialog()
                    .withActions(new DialogAction(DialogAction.Type.YES).withHandler(e -> {
                        clearAlternativeValues();
                    }), new DialogAction(DialogAction.Type.NO).withHandler(e -> {
                        getEditedEntity().setCheckCashFlowItem(true);
                    }))
                    .withCaption(messages.getMessage(CashFlowItemBusinessEdit.class, "cashFlowItemBusinessEdit.msgCheckCashFlowCaption"))
                    .withMessage(messages.getMessage(CashFlowItemBusinessEdit.class, "cashFlowItemBusinessEdit.msgCheckCashFlowText"))
                    .show();
        }
        setEditableOnAlternativeValues();
    }

    private void setEditableOnAlternativeValues() {
        if (Boolean.TRUE.equals(getEditedEntity().getCheckCashFlowItem())) {
            cashFlowItemBusinessAlternativeValuesBox.setEnabled(true);
            cashFlowItemField.setCaption(messages.getMessage(CashFlowItemBusinessEdit.class, "cashFlowItemBusinessEdit.manyCashFlowItemFieldCaption"));
        } else {
            cashFlowItemBusinessAlternativeValuesBox.setEnabled(false);
            cashFlowItemField.setCaption(messages.getMessage(CashFlowItemBusinessEdit.class, "cashFlowItemBusinessEdit.oneCashFlowItemFieldCaption"));
        }
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (Boolean.TRUE.equals(getEditedEntity().getCheckCashFlowItem())) {
            if (Objects.requireNonNull(cashFlowItemBusinessAlternativeValuesDc.getItems()).size() == 0) {
                getEditedEntity().setCheckCashFlowItem(false);
            }
        }
    }

    @Subscribe("addAlternativeValuesBtn")
    public void onAddAlternativeValuesBtnClick(Button.ClickEvent event) {
        addAlternativeValues();
    }

    public void clearAlternativeValues() {
        cashFlowItemBusinessAlternativeValuesDc.getMutableItems().forEach(e -> dataContext.remove(e));
        cashFlowItemBusinessAlternativeValuesDc.getMutableItems().clear();
    }

    public void addAlternativeValues() {
        List<CashFlowItem> currentCashFlowList = new ArrayList<>();
        if(!Objects.isNull(getEditedEntity().getCashFlowItem())){
            currentCashFlowList.add(getEditedEntity().getCashFlowItem());
        }
        cashFlowItemBusinessAlternativeValuesDc.getItems().forEach(e -> currentCashFlowList.add(e.getCashFlowItem()));

        screenBuilders.lookup(CashFlowItem.class, this).withLaunchMode(OpenMode.DIALOG).withSelectHandler(cashFlowItems -> {
            for (CashFlowItem cashFlowItem : cashFlowItems) {
                if (currentCashFlowList.contains(cashFlowItem)) {
                    continue;
                }
                CashFlowItemBusinessAlternativeValues cashFlowItemBusinessAlternativeValue = dataManager.create(CashFlowItemBusinessAlternativeValues.class);
                cashFlowItemBusinessAlternativeValue.setCashFlowItemBusiness(getEditedEntity());
                cashFlowItemBusinessAlternativeValue.setCashFlowItem(cashFlowItem);
                cashFlowItemBusinessAlternativeValue = dataContext.merge(cashFlowItemBusinessAlternativeValue);
                cashFlowItemBusinessAlternativeValuesDc.getMutableItems().add(cashFlowItemBusinessAlternativeValue);
            }
        }).build().show();
    }
}