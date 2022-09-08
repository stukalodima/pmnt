package com.itk.finance.web.screens.reparationobject;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ReparationObject;

@UiController("finance_ReparationObject.browse")
@UiDescriptor("reparation-object-browse.xml")
@LookupComponent("reparationObjectsTable")
@LoadDataBeforeShow
public class ReparationObjectBrowse extends StandardLookup<ReparationObject> {
}