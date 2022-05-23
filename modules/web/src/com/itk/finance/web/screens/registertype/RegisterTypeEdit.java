package com.itk.finance.web.screens.registertype;

import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.RegisterType;

import javax.inject.Inject;
import java.util.Objects;

@UiController("finance_RegisterType.edit")
@UiDescriptor("register-type-edit.xml")
@EditedEntityContainer("registerTypeDc")
@LoadDataBeforeShow
public class RegisterTypeEdit extends StandardEditor<RegisterType> {
    @Inject
    private TextField<String> conditionField;
    @Inject
    private TextField<Double> conditionValueField;

    @Subscribe("useConditionField")
    public void onUseConditionFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        boolean visible = !Objects.isNull(event.getValue()) && event.getValue();
        conditionField.setVisible(visible);
        conditionValueField.setVisible(visible);
    }
}