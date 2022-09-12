package com.itk.finance.web.screens.documenttype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.DocumentType;

@UiController("finance_DocumentType.browse")
@UiDescriptor("document-type-browse.xml")
@LookupComponent("documentTypesTable")
@LoadDataBeforeShow
public class DocumentTypeBrowse extends StandardLookup<DocumentType> {
}