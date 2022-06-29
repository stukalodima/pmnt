package com.itk.finance.web.screens.addressingdetail;

import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.Form;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AddressingDetail;

import javax.inject.Inject;
import java.util.Objects;

@UiController("finance_AddressingDetail.edit")
@UiDescriptor("addressing-detail-edit.xml")
@EditedEntityContainer("addressingDetailDc")
@LoadDataBeforeShow
public class AddressingDetailEdit extends StandardEditor<AddressingDetail> {
    @Inject
    private CollectionLoader<ProcRole> procRolesDl;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;
    @Inject
    private Form form;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if (Objects.isNull(getEditedEntity().getAddressing().getProcDefinition())) {
            dialogs
                    .createMessageDialog()
                    .withCaption(messages.getMessage(AddressingDetailEdit.class, "addressingDetailEdit.msgErrorCaption"))
                    .withMessage(messages.getMessage(AddressingDetailEdit.class, "addressingDetailEdit.msgErrorText"))
                    .show();
            form.setEditable(false);
        }
        procRolesDl.setParameter("procDefinition", getEditedEntity().getAddressing().getProcDefinition());
        procRolesDl.load();
    }
}