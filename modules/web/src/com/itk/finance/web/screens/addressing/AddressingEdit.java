package com.itk.finance.web.screens.addressing;

import com.haulmont.bpm.entity.ProcDefinition;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Addressing;
import com.itk.finance.entity.AddressingDetail;
import com.itk.finance.entity.Company;
import com.itk.finance.service.ProcPropertyService;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UiController("finance_Addressing.edit")
@UiDescriptor("addressing-edit.xml")
@EditedEntityContainer("addressingDc")
@LoadDataBeforeShow
public class AddressingEdit extends StandardEditor<Addressing> {
    @Inject
    private LookupPickerField<Company> companyField;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private DataManager dataManager;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenValidation screenValidation;
    @Inject
    private CollectionPropertyContainer<AddressingDetail> addressingDetailDc;
    @Inject
    private Table<AddressingDetail> addressingDetailTable;
    @Inject
    private ProcPropertyService procPropertyService;

    @Subscribe("useCompanyField")
    public void onUseCompanyFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        setEditableByCompany(event.getValue());
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        setEditableByCompany(getEditedEntity().getUseCompany());
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<Addressing> event) {
        event.getEntity().setUseCompany(false);
        event.getEntity().setBussines(userPropertyService.getDefaultBusiness());
    }

    private void setEditableByCompany(Boolean editable) {
        companyField.setEditable(editable);
        companyField.setRequired(editable);
    }

    private void fillRoles() {
        List<ProcRole> procRoles = procPropertyService.getProcRolesOnDefinition(getEditedEntity().getProcDefinition());

        List<ProcRole> currentProcRoles = new ArrayList<>();
        for(AddressingDetail currentAddressingDetail : addressingDetailDc.getMutableItems()){
            if(procRoles.contains(currentAddressingDetail.getProcRole())){
                currentProcRoles.add(currentAddressingDetail.getProcRole());
            }
            else {
                dataContext.remove(currentAddressingDetail);
                addressingDetailDc.getMutableItems().remove(currentAddressingDetail);
            }
        }

        for (ProcRole procRole : procRoles) {
            if (currentProcRoles.contains(procRole)){
                continue;
            }
            AddressingDetail addressingDetail = dataManager.create(AddressingDetail.class);
            addressingDetail.setAddressing(getEditedEntity());
            addressingDetail.setProcRole(procRole);
            addressingDetail = dataContext.merge(addressingDetail);
            addressingDetailDc.getMutableItems().add(addressingDetail);
        }
    }

    private void clearRoles() {
        for (AddressingDetail addressingDetail :
                addressingDetailDc.getMutableItems()) {
            dataContext.remove(addressingDetail);
        }
        addressingDetailDc.getMutableItems().clear();
    }

    @Subscribe("addressingDetailTableFillRolesBtn")
    public void onAddressingDetailTableFillRolesBtnClick(Button.ClickEvent event) {
        ValidationErrors errors = validateUiComponents();
        if (errors.isEmpty()) {
            if (Objects.requireNonNull(addressingDetailTable.getItems()).size() > 0) {
                dialogs
                        .createOptionDialog()
                        .withActions(
                                new DialogAction(DialogAction.Type.YES).withHandler(e -> fillRoles()),
                                new DialogAction(DialogAction.Type.NO)
                        )
                        .withCaption(messages.getMessage(AddressingEdit.class, "addressingEdit.msgFillCaption"))
                        .withMessage(messages.getMessage(AddressingEdit.class, "addressingEdit.msgFillText"))
                        .show();
            } else {
                fillRoles();
            }
        } else {
            screenValidation.showValidationErrors(this, errors);
        }
    }

    @Subscribe("procDefinitionField")
    public void onProcDefinitionFieldValueChange(HasValue.ValueChangeEvent<ProcDefinition> event) {
        if (event.isUserOriginated()) {
            if (Objects.requireNonNull(addressingDetailTable.getItems()).size() > 0) {
                dialogs
                        .createOptionDialog()
                        .withActions(
                                new DialogAction(DialogAction.Type.YES).withHandler(e -> clearRoles()),
                                new DialogAction(DialogAction.Type.NO).withHandler(e -> getEditedEntity().setProcDefinition(event.getPrevValue()))
                        )
                        .withCaption(messages.getMessage(AddressingEdit.class, "addressingEdit.msgChangeProcDefinitionCaption"))
                        .withMessage(messages.getMessage(AddressingEdit.class, "addressingEdit.msgFillText"))
                        .show();
            } else {
                if (!Objects.isNull(event.getValue())) {
                    fillRoles();
                }
            }
        }
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        boolean userNotFill = false;
        for (AddressingDetail addressingDetail :
                getEditedEntity().getAddressingDetail()) {
            if (Objects.isNull(addressingDetail.getUser()) && !addressingDetail.getAutoDetect()){
                userNotFill = true;
                break;
            }
        }
        if (userNotFill) {
            dialogs
                    .createMessageDialog()
                    .withCaption(messages.getMessage(AddressingEdit.class,"addressingEdit.msgCommitErrorCaption"))
                    .withMessage(messages.getMessage(AddressingEdit.class,"addressingEdit.msgCommitErrorText"))
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
            event.preventCommit();
        }
    }

}