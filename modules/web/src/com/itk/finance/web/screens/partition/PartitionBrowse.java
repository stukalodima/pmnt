package com.itk.finance.web.screens.partition;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Partition;

@UiController("finance_Partition.browse")
@UiDescriptor("partition-browse.xml")
@LookupComponent("partitionsTable")
@LoadDataBeforeShow
public class PartitionBrowse extends StandardLookup<Partition> {
}