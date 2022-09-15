package com.itk.finance.web.screens.reparationfilebyreparationobject;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ReparationFileByReparationObject;

@UiController("finance_ReparationFileByReparationObject.edit")
@UiDescriptor("reparation-file-by-reparation-object-edit.xml")
@EditedEntityContainer("reparationFileByReparationObjectDc")
@LoadDataBeforeShow
public class ReparationFileByReparationObjectEdit extends StandardEditor<ReparationFileByReparationObject> {
}