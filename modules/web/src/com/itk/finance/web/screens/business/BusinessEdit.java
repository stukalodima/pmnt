package com.itk.finance.web.screens.business;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.BusinessControllers;
import com.itk.finance.entity.BusinessOperators;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;

@UiController("finance_Business.edit")
@UiDescriptor("business-edit.xml")
@EditedEntityContainer("businessDc")
@LoadDataBeforeShow
public class BusinessEdit extends StandardEditor<Business> {
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private DataGrid<BusinessOperators> businessOperatorsTable;
    @Inject
    private Notifications notifications;
    @Inject
    private Messages messages;
    @Inject
    private Metadata metadata;
    @Inject
    private CollectionContainer<BusinessOperators> businessOperatorsDc;
    @Inject
    private DataGrid<BusinessControllers> businessControllersTable;
    @Inject
    private CollectionContainer<BusinessControllers> businessControllersDc;
    @Inject
    private DataContext dataContext;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Business> event) {
        event.getEntity().setManagementCompany(userPropertyService.getDefaultManagementCompany());
    }

    @Subscribe("businessOperatorsTable.create")
    public void onBusinessOperatorsTableCreate(Action.ActionPerformedEvent event) {
        if (businessOperatorsTable.isEditorActive()) {
            notifications.create().withCaption(messages.getMessage(BusinessEdit.class, "")).show();
            return;
        }
        BusinessOperators businessOperators = metadata.create(BusinessOperators.class);
        businessOperators.setBusiness(getEditedEntity());
        businessOperators = dataContext.merge(businessOperators);
        businessOperatorsDc.getMutableItems().add(businessOperators);
        businessOperatorsTable.edit(businessOperators);
    }

    @Subscribe("businessOperatorsTable.edit")
    public void onBusinessOperatorsTableEdit(Action.ActionPerformedEvent event) {
        if (businessOperatorsTable.isEditorActive()) {
            notifications.create().withCaption(messages.getMessage(BusinessEdit.class, "")).show();
            return;
        }
        businessOperatorsTable.edit(businessOperatorsDc.getItem());
    }

    @Subscribe("businessControllersTable.create")
    public void onBusinessControllersTableCreate(Action.ActionPerformedEvent event) {
        if (businessControllersTable.isEditorActive()) {
            notifications.create().withCaption(messages.getMessage(BusinessEdit.class, "")).show();
            return;
        }
        BusinessControllers businessControllers = metadata.create(BusinessControllers.class);
        businessControllers.setBusiness(getEditedEntity());
        businessControllers = dataContext.merge(businessControllers);
        businessControllersDc.getMutableItems().add(businessControllers);
        businessControllersTable.edit(businessControllers);
    }

    @Subscribe("businessControllersTable.edit")
    public void onBusinessControllersTableEdit(Action.ActionPerformedEvent event) {
        if (businessControllersTable.isEditorActive()) {
            notifications.create().withCaption(messages.getMessage(BusinessEdit.class, "")).show();
            return;
        }
        businessControllersTable.edit(businessControllersDc.getItem());
    }

}