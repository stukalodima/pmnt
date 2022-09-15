package com.itk.finance.web.screens.reparationobject;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.app.core.file.FileDownloadHelper;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.RadioButtonGroup;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;

import javax.inject.Inject;
import java.util.*;

@UiController("finance_ReparationObject.edit")
@UiDescriptor("reparation-object-edit.xml")
@EditedEntityContainer("reparationObjectDc")
public class ReparationObjectEdit extends StandardEditor<ReparationObject> {
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionLoader<Partition> partitionDl;
    @Inject
    private CollectionContainer<Partition> partitionDc;
    @Inject
    private RadioButtonGroup<UUID> partitionList;
    @Inject
    private Messages messages;
    @Inject
    private CollectionLoader<ReparationFileByReparationObject> fileDl;
    @Inject
    private CollectionLoader<ReparationObjectState> reparationObjectStateDl;
    @Inject
    private InstanceLoader<ReparationObject> reparationObjectDl;
    @Inject
    private CollectionLoader<PropertyType> propertyTypesDl;
    @Inject
    private CollectionLoader<Business> businessesDl;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private LookupField<Company> companyField;
    @Inject
    private LookupField<PropertyType> propertyTypeField;
    @Inject
    private GroupTable<ReparationFileByReparationObject> fileGroupTable;

    @Subscribe
    public void onInit(InitEvent event) {
        FileDownloadHelper.initGeneratedColumn(fileGroupTable, "reparationFile.document");
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (PersistenceHelper.isNew(getEditedEntity())) {
            propertyTypesDl.load();
        } else {
            propertyTypeField.setEditable(false);
        }
        reparationObjectStateDl.load();
        businessesDl.load();
        partitionDl.load();
        Map<String, UUID> partitionMap = new LinkedHashMap<>();
        UUID emptyPartition = UUID.randomUUID();
        partitionMap.put(messages.getMessage(ReparationObjectEdit.class, "partitionAll.caption"), emptyPartition);
        partitionDc.getItems().forEach(partition -> partitionMap.put(partition.getName(), partition.getId()));
        partitionList.setOptionsMap(partitionMap);
        partitionList.setValue(emptyPartition);
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        reparationObjectDl.load();
    }

    @Subscribe("partitionList")
    public void onPartitionListValueChange(HasValue.ValueChangeEvent<UUID> event) {
        UUID id = event.getValue();
        Optional<Partition> partition = dataManager.load(Partition.class)
                .query("e.id = :id")
                .parameter("id", Objects.requireNonNull(id))
                .optional();
        if (partition.isPresent()) {
            fileDl.setParameter("partition", partition.get());
        } else {
            fileDl.removeParameter("partition");
        }
        fileDl.setParameter("reparationObject", getEditedEntity());
        fileDl.load();
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        companyField.clear();
        if (event.getValue() != null) {
            companiesDl.setParameter("business", event.getValue());
        } else {
            companiesDl.removeParameter("business");
        }
        companiesDl.load();
    }

}