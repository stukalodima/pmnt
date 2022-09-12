package com.itk.finance.web.screens.reparationobjectstate;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ReparationObjectState;

@UiController("finance_ReparationObjectState.browse")
@UiDescriptor("reparation-object-state-browse.xml")
@LookupComponent("reparationObjectStatesTable")
@LoadDataBeforeShow
public class ReparationObjectStateBrowse extends StandardLookup<ReparationObjectState> {
}