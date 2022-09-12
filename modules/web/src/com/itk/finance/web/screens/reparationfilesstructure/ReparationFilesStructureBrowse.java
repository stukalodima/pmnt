package com.itk.finance.web.screens.reparationfilesstructure;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ReparationFilesStructure;

import javax.inject.Inject;

@UiController("finance_ReparationFilesStructure.browse")
@UiDescriptor("reparation-files-structure-browse.xml")
@LookupComponent("reparationFilesStructuresTable")
@LoadDataBeforeShow
public class ReparationFilesStructureBrowse extends StandardLookup<ReparationFilesStructure> {
    @Inject
    private Metadata metadata;
    @Inject
    private TreeTable<ReparationFilesStructure> reparationFilesStructuresTable;
    @Inject
    private DataManager dataManager;

    @Install(to = "reparationFilesStructuresTable.create", subject = "newEntitySupplier")
    private ReparationFilesStructure reparationFilesStructuresTableCreateNewEntitySupplier() {
        ReparationFilesStructure filesStructure = metadata.create(ReparationFilesStructure.class);
        ReparationFilesStructure selected = reparationFilesStructuresTable.getSingleSelected();
        if (selected != null && selected.getPid() != null) {
            ReparationFilesStructure pid = dataManager.reload(selected.getPid(), "reparationFilesStructure-all-property");
            filesStructure.setPid(pid);
            filesStructure.setPartition(pid.getPartition());
            filesStructure.setPropertyType(pid.getPropertyType());
            filesStructure.setDocumentType(pid.getDocumentType());
        }
        return filesStructure;
    }

}