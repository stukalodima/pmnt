package com.itk.finance.web.screens.registertypedetail;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.RegisterTypeDetail;
import com.itk.finance.web.screens.registertype.RegisterTypeEdit;

import javax.inject.Inject;
import java.util.Objects;

@UiController("finance_RegisterTypeDetail.edit")
@UiDescriptor("register-type-detail-edit.xml")
@EditedEntityContainer("registerTypeDetailDc")
@LoadDataBeforeShow
public class RegisterTypeDetailEdit extends StandardEditor<RegisterTypeDetail> {
    @Inject
    private TextField<String> conditionField;
    @Inject
    private TextField<Double> conditionValueField;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;

    @Subscribe("useConditionField")
    public void onUseConditionFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.isUserOriginated()) {
            if (!Objects.isNull(event.getValue()) && event.getValue()
                    && !Objects.isNull(getEditedEntity().getRegisterType().getUseCondition()) && getEditedEntity().getRegisterType().getUseCondition()) {
                dialogs.createMessageDialog()
                        .withCaption(messages.getMessage(RegisterTypeEdit.class, "msgConditionCaption"))
                        .withMessage(messages.getMessage(RegisterTypeEdit.class, "msgConditionText"))
                        .show();
                getEditedEntity().setUseCondition(event.getPrevValue());
                return;
            } else {
                getEditedEntity().setCondition(null);
                getEditedEntity().setConditionValue(null);
            }
        }
        boolean visible = !Objects.isNull(event.getValue()) && event.getValue();
        conditionField.setVisible(visible);
        conditionValueField.setVisible(visible);
    }
}