package com.itk.finance.web.screens.userproperty;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.UserProperty;

@UiController("finance_UserProperty.browse")
@UiDescriptor("user-property-browse.xml")
@LookupComponent("userPropertiesTable")
@LoadDataBeforeShow
public class UserPropertyBrowse extends StandardLookup<UserProperty> {
}