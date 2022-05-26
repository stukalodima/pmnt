package com.itk.finance.web.screens.registertype;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.RegisterType;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@UiController("finance_RegisterType.edit")
@UiDescriptor("register-type-edit.xml")
@EditedEntityContainer("registerTypeDc")
@LoadDataBeforeShow
public class RegisterTypeEdit extends StandardEditor<RegisterType> {
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
            if (!Objects.isNull(event.getValue()) && event.getValue()) {
                AtomicBoolean error = new AtomicBoolean(false);
                getEditedEntity().getRegisterTypeDetails().forEach(e -> {
                    if (!Objects.isNull(e.getUseCondition()) && e.getUseCondition() && !error.get()) {
                        dialogs.createMessageDialog()
                                .withCaption(messages.getMessage(RegisterTypeEdit.class, "msgConditionCaption"))
                                .withMessage(messages.getMessage(RegisterTypeEdit.class, "msgConditionText"))
                                .show();
                        getEditedEntity().setUseCondition(event.getPrevValue());
                        error.set(true);
                    }
                });
                if (error.get()) {
                    return;
                }
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