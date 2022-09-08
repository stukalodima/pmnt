package com.itk.finance.web.screens.reparationobjectstate;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ReparationObjectState;

@UiController("finance_ReparationObjectState.edit")
@UiDescriptor("reparation-object-state-edit.xml")
@EditedEntityContainer("reparationObjectStateDc")
@LoadDataBeforeShow
public class ReparationObjectStateEdit extends StandardEditor<ReparationObjectState> {
}