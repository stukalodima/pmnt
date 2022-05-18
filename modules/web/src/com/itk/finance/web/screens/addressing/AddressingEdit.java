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
        if (Objects.requireNonNull(addressingDetailTable.getItems()).size() > 0) {
            clearRoles();
        }
        List<ProcRole> procRoles = dataManager.load(ProcRole.class)
                .query("select e from bpm$ProcRole e where e.procDefinition = :procDefinition")
                .parameter("procDefinition", getEditedEntity().getProcDefinition())
                .view("_minimal")
                .list();
        List<AddressingDetail> listDetail= new ArrayList<>();
        for (ProcRole procRole : procRoles) {
            AddressingDetail addressingDetail = dataManager.create(AddressingDetail.class);
            addressingDetail.setAddressing(getEditedEntity());
            addressingDetail.setProcRole(procRole);
            addressingDetail = dataContext.merge(addressingDetail);
            listDetail.add(addressingDetail);
        }
        getEditedEntity().setAddressingDetail(listDetail);
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
            if (Objects.isNull(addressingDetail.getUser())){
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