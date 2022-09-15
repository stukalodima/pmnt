package com.itk.finance.web.screens.reparationobject;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.itk.finance.entity.*;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;

import java.util.*;

import static java.util.Objects.isNull;

@UiController("finance_ReparationObject.browse")
@UiDescriptor("reparation-object-browse.xml")
@LookupComponent("reparationObjectsTable")
public class ReparationObjectBrowse extends StandardLookup<ReparationObject> {
    @Inject
    private CollectionLoader<PropertyType> propertyTypeDl;
    @Inject
    private LookupPickerField<Business> businessFilterField;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private CollectionLoader<Business> businessDl;
    @Inject
    private LookupPickerField<Company> companyFilterField;
    @Inject
    private CollectionLoader<Company> companyDl;
    @Inject
    private RadioButtonGroup<UUID> propertyTypeList;
    @Inject
    private CollectionLoader<ReparationObject> reparationObjectsDl;
    @Inject
    private CollectionLoader<ReparationObjectState> stateDl;
    @Inject
    private LookupField<ReparationObjectState> stateField;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionContainer<PropertyType> propertyTypeDc;
    @Inject
    private Messages messages;

    private PropertyType propertyTypeLocal;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        businessDl.load();
        stateDl.load();
        propertyTypeDl.load();

        businessFilterField.setValue(userPropertyService.getDefaultBusiness());
        companyFilterField.setValue(userPropertyService.getDefaultCompany());

        refreshCompanyDl();

        UUID id = UUID.randomUUID();
        Map<String, UUID> propertyTypeMap = new LinkedHashMap<>();
        propertyTypeMap.put(messages.getMessage(ReparationObjectBrowse.class,"propertyTypeAll.caption"), id);
        propertyTypeDc.getItems().forEach(propertyType -> propertyTypeMap.put(propertyType.getName(), propertyType.getId()));
        propertyTypeList.setOptionsMap(propertyTypeMap);
        propertyTypeList.setValue(id);
    }

    @Subscribe("businessFilterField")
    public void onBusinessFilterFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        companyFilterField.clear();
        if (event.isUserOriginated()) {
            refreshCompanyDl();
            refreshReparationObjectDl();
        }
    }

    @Subscribe("companyFilterField")
    public void onCompanyFilterFieldValueChange(HasValue.ValueChangeEvent<Company> event) {
        if (event.isUserOriginated()) {
            refreshReparationObjectDl();
        }
    }

    @Subscribe("propertyTypeList")
    public void onPropertyTypeListValueChange(HasValue.ValueChangeEvent<PropertyType> event) {
        Optional<PropertyType> propertyType = dataManager.load(PropertyType.class)
                .query("e.id = :id")
                .parameter("id", Objects.requireNonNull(event.getValue()))
                .optional();
        propertyTypeLocal = propertyType.orElse(null);
            refreshReparationObjectDl();
    }

    @Subscribe("stateField")
    public void onStateFieldValueChange(HasValue.ValueChangeEvent<ReparationObjectState> event) {
        refreshReparationObjectDl();
    }

    @SuppressWarnings("DuplicatedCode")
    private void refreshReparationObjectDl() {
        if (propertyTypeList.getValue() != null) {
            reparationObjectsDl.setParameter("propertyType", propertyTypeLocal);
        } else {
            reparationObjectsDl.removeParameter("propertyType");
        }
        if (businessFilterField.getValue() != null) {
            reparationObjectsDl.setParameter("business", businessFilterField.getValue());
        } else {
            reparationObjectsDl.removeParameter("business");
        }
        if (companyFilterField.getValue() != null) {
            reparationObjectsDl.setParameter("company", companyFilterField.getValue());
            if (businessFilterField.getValue() == null) {
                View view = new View(Company.class);
                view.addProperty("business");
                Company companyWithBusiness = dataManager.reload(companyFilterField.getValue(), view);
                reparationObjectsDl.setParameter("business", companyWithBusiness.getBusiness());
            }
        } else {
            reparationObjectsDl.removeParameter("company");
        }
        if (stateField.getValue() != null) {
            reparationObjectsDl.setParameter("state", stateField.getValue());
        } else {
            reparationObjectsDl.removeParameter("state");
        }
        reparationObjectsDl.load();
    }

    @Install(to = "reparationObjectsTable.create", subject = "initializer")
    private void reparationObjectsTableCreateInitializer(ReparationObject reparationObject) {
        reparationObject.setBusiness(isNull(businessFilterField.getValue())
                ? userPropertyService.getDefaultBusiness()
                : businessFilterField.getValue()
        );
        reparationObject.setCompany(isNull(companyFilterField.getValue())
                ? userPropertyService.getDefaultCompany()
                : companyFilterField.getValue()
        );
        reparationObject.setReparationObjectState(stateField.getValue());
        reparationObject.setPropertyType(propertyTypeLocal);
    }

    private void refreshCompanyDl() {
        if (businessFilterField.getValue() != null) {
            companyDl.setParameter("business", businessFilterField.getValue());
        } else {
            companyDl.removeParameter("business");
        }
        companyDl.load();
    }

}