package com.itk.finance.web.screens.partition;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Partition;

@UiController("finance_Partition.edit")
@UiDescriptor("partition-edit.xml")
@EditedEntityContainer("partitionDc")
@LoadDataBeforeShow
public class PartitionEdit extends StandardEditor<Partition> {
}