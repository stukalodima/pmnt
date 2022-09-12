package com.itk.finance.web.screens.reparationfilesstructure;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ReparationFilesStructure;

@UiController("finance_ReparationFilesStructure.edit")
@UiDescriptor("reparation-files-structure-edit.xml")
@EditedEntityContainer("reparationFilesStructureDc")
@LoadDataBeforeShow
public class ReparationFilesStructureEdit extends StandardEditor<ReparationFilesStructure> {
}