package com.itk.finance.web.screens.registertype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.RegisterType;

@UiController("finance_RegisterType.edit")
@UiDescriptor("register-type-edit.xml")
@EditedEntityContainer("registerTypeDc")
@LoadDataBeforeShow
public class RegisterTypeEdit extends StandardEditor<RegisterType> {
}