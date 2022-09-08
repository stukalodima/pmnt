package com.itk.finance.web.screens.propertytype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PropertyType;

@UiController("finance_PropertyType.browse")
@UiDescriptor("property-type-browse.xml")
@LookupComponent("propertyTypesTable")
@LoadDataBeforeShow
public class PropertyTypeBrowse extends StandardLookup<PropertyType> {
}