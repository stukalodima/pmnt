package com.itk.finance.web.screens.registertypedetail;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.RegisterTypeDetail;

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
    private TextField<String> conditionValueField;

    @Subscribe("useConditionField")
    public void onUseConditionFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        boolean visible = !Objects.isNull(event.getValue()) && event.getValue();
        conditionField.setVisible(visible);
        conditionValueField.setVisible(visible);
    }
}