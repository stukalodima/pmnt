package com.itk.finance.web.screens.reparationobject;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ReparationObject;

@UiController("finance_ReparationObject.edit")
@UiDescriptor("reparation-object-edit.xml")
@EditedEntityContainer("reparationObjectDc")
@LoadDataBeforeShow
public class ReparationObjectEdit extends StandardEditor<ReparationObject> {
}