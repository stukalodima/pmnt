package com.itk.finance.web.screens.registertype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.RegisterType;

@UiController("finance_RegisterType.browse")
@UiDescriptor("register-type-browse.xml")
@LookupComponent("registerTypesTable")
@LoadDataBeforeShow
public class RegisterTypeBrowse extends StandardLookup<RegisterType> {
}