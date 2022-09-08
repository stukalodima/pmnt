package com.itk.finance.web.screens.propertytype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PropertyType;

@UiController("finance_PropertyType.edit")
@UiDescriptor("property-type-edit.xml")
@EditedEntityContainer("propertyTypeDc")
@LoadDataBeforeShow
public class PropertyTypeEdit extends StandardEditor<PropertyType> {
}