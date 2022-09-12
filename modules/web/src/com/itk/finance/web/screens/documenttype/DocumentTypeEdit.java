package com.itk.finance.web.screens.documenttype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.DocumentType;

@UiController("finance_DocumentType.edit")
@UiDescriptor("document-type-edit.xml")
@EditedEntityContainer("documentTypeDc")
@LoadDataBeforeShow
public class DocumentTypeEdit extends StandardEditor<DocumentType> {
}