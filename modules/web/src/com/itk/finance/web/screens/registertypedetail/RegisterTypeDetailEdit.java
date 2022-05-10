package com.itk.finance.web.screens.registertypedetail;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.RegisterTypeDetail;

@UiController("finance_RegisterTypeDetail.edit")
@UiDescriptor("register-type-detail-edit.xml")
@EditedEntityContainer("registerTypeDetailDc")
@LoadDataBeforeShow
public class RegisterTypeDetailEdit extends StandardEditor<RegisterTypeDetail> {
}