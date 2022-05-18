package com.itk.finance.web.screens.addressing;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Addressing;

@UiController("finance_Addressing.browse")
@UiDescriptor("addressing-browse.xml")
@LookupComponent("addressingsTable")
@LoadDataBeforeShow
public class AddressingBrowse extends StandardLookup<Addressing> {
}