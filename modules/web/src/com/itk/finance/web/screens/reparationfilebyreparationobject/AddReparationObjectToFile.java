package com.itk.finance.web.screens.reparationfilebyreparationobject;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;
import com.itk.finance.web.screens.reparationfilebyreparationobject.action.AddReparationObjectsAction;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("finance_AddReparationObjectToFile")
@UiDescriptor("add-reparation-object-to-file.xml")
public class AddReparationObjectToFile extends Screen {
    @Inject
    private GroupTable<ReparationObject> reparationObjectsTable;
    private Business business;

    private Company company;

    private Partition partition;

    private DocumentType documentType;

    private PropertyType propertyType;

    @Inject
    private CollectionLoader<ReparationObject> reparationObjectsDl;
    @Inject
    private Label<String> businessLabel;
    @Inject
    private Label<String> businessValue;
    @Inject
    private Label<String> companyLabel;
    @Inject
    private Label<String> companyValue;
    @Inject
    private Label<String> propertyTypeLabel;
    @Inject
    private Label<String> propertyTypeValue;
    @Inject
    private Messages messages;
    @Inject
    private Label<String> partitionLabel;
    @Inject
    private Label<String> partitionValue;
    @Inject
    private Label<String> documentLabel;
    @Inject
    private Label<String> documentValue;
    @Inject
    private Filter filter;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        refreshReparationObjectDl();
        businessLabel.setValue(messages.getMessage(ReparationFile.class, "ReparationFile.business"));
        if (business != null) {
            businessValue.setValue(business.getName());
        } else {
            businessValue.setValue(messages.getMessage(AddReparationObjectToFile.class, "emptyValue"));
        }
        companyLabel.setValue(messages.getMessage(ReparationFile.class, "ReparationFile.company"));
        if (company != null) {
            companyValue.setValue(company.getShortName());
        } else {
            companyValue.setValue(messages.getMessage(AddReparationObjectToFile.class, "emptyValue"));
        }
        partitionLabel.setValue(messages.getMessage(ReparationFile.class, "ReparationFile.partition"));
        if (partition != null) {
            partitionValue.setValue(partition.getName());
        } else {
            partitionValue.setValue(messages.getMessage(AddReparationObjectToFile.class, "emptyValue"));
        }
        documentLabel.setValue(messages.getMessage(ReparationFile.class, "ReparationFile.documentType"));
        if (documentType != null) {
            documentValue.setValue(documentType.getName());
        } else {
            documentValue.setValue(messages.getMessage(AddReparationObjectToFile.class, "emptyValue"));
        }
        propertyTypeLabel.setValue(messages.getMessage(ReparationFile.class, "ReparationFile.propertyType"));
        if (propertyType != null) {
            propertyTypeValue.setValue(propertyType.getName());
        } else {
            propertyTypeValue.setValue(messages.getMessage(AddReparationObjectToFile.class, "emptyValue"));
        }
        filter.setVisible(Boolean.TRUE.equals(partition.getUseObject()));
        reparationObjectsTable.setVisible(Boolean.TRUE.equals(partition.getUseObject()));
    }

    @Install(to = "reparationObjectsTable.create", subject = "initializer")
    private void reparationObjectsTableCreateInitializer(ReparationObject reparationObject) {
        reparationObject.setBusiness(business);
        reparationObject.setCompany(company);
        reparationObject.setPropertyType(propertyType);
    }

    private void refreshReparationObjectDl() {
        if (business != null) {
            reparationObjectsDl.setParameter("business", business);
        } else {
            reparationObjectsDl.removeParameter("business");
        }
        if (company != null) {
            reparationObjectsDl.setParameter("company", company);
        } else {
            reparationObjectsDl.removeParameter("company");
        }
        if (propertyType != null) {
            reparationObjectsDl.setParameter("propertyType", propertyType);
        } else {
            reparationObjectsDl.removeParameter("propertyType");
        }
        reparationObjectsDl.load();
    }

    @Subscribe("lookupSelectAction")
    public void onLookupSelectActionClick(Button.ClickEvent event) {
        List<ReparationObject> reparationObjectList = new ArrayList<>(reparationObjectsTable.getSelected());
        AddReparationObjectsAction action = new AddReparationObjectsAction("lookupSelectAction", reparationObjectList);
        close(action);
    }

    @Subscribe("lookupCancelAction")
    public void onLookupCancelActionClick(Button.ClickEvent event) {
        closeWithDefaultAction();
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}