package com.itk.finance.web.screens.reparationfile;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.*;
import com.itk.finance.service.ReparationFileService;
import com.itk.finance.web.screens.reparationfilebyreparationobject.AddReparationObjectToFile;
import com.itk.finance.web.screens.reparationfilebyreparationobject.action.AddReparationObjectsAction;

import javax.inject.Inject;
import java.util.Objects;

import static java.util.Objects.isNull;

@UiController("finance_ReparationFile.edit")
@UiDescriptor("reparation-file-edit.xml")
@EditedEntityContainer("reparationFileDc")
public class ReparationFileEdit extends StandardEditor<ReparationFile> {
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private SuggestionPickerField<ReparationObject> reparationObjectField;
    @Inject
    private DataManager dataManager;
    @Inject
    private FileUploadField documentField;
    @Inject
    private Messages messages;
    @Inject
    private CollectionLoader<ReparationFileByReparationObject> reparationObjectsDl;
    @Inject
    private InstanceLoader<ReparationFile> reparationFileDl;
    @Inject
    private CollectionLoader<Business> businessesDl;
    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionContainer<ReparationFileByReparationObject> reparationObjectsDc;
    @Inject
    private Notifications notifications;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private ReparationFileService reparationFileService;

    @Subscribe
    public void onInit(InitEvent event) {
        reparationObjectField.setSearchExecutor((searchString, searchParams) -> dataManager.load(ReparationObject.class)
                .query("select c from finance_ReparationObject c " +
                        "where c.propertyType = :propertyType " +
                        "   and (" +
                        "       c.name like :name " +
                        "       or c.invNumber like :name " +
                        "   )" +
                        "order by c.name")
                .parameter("name", "%" + searchString + "%")
                .parameter("propertyType", getEditedEntity().getPropertyType())
                .view("_local")
                .list());
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        reparationFileDl.load();
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        businessesDl.load();
        setCompanyParameter();
        reparationObjectsDl.setParameter("reparationFile", getEditedEntity());
        reparationObjectsDl.load();
        if (PersistenceHelper.isNew(getEditedEntity())) {
            documentField.setUploadButtonCaption(messages.getMessage(ReparationFileEdit.class, "documentField.newUpload.caption"));
        } else {
            documentField.setUploadButtonCaption(messages.getMessage(ReparationFileEdit.class, "documentField.editUpload.caption"));
        }
    }

    private void setCompanyParameter() {
        companiesDl.setParameter("business", getEditedEntity().getBusiness());
        companiesDl.load();
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        if (getEditedEntity().getCompany() != null && !getEditedEntity().getCompany().getBusiness().equals(event.getValue())) {
            getEditedEntity().setCompany(null);
        }
        if (event.isUserOriginated()) {
            setCompanyParameter();
        }
    }

    @Subscribe("reparationObjectField.addReparationObject")
    public void onReparationObjectFieldAddReparationObject(Action.ActionPerformedEvent event) {
        if (isNull(reparationObjectField.getValue())) {
            notifications.create()
                    .withContentMode(ContentMode.TEXT)
                    .withType(Notifications.NotificationType.ERROR)
                    .withCaption(messages.getMessage(ReparationFileEdit.class, "notifications.nullReparationObject.caption"))
                    .withDescription(messages.getMessage(ReparationFileEdit.class, "notifications.nullReparationObject.text"))
                    .show();
            return;
        } else {
            if (reparationObjectsDc.getItems()
                    .stream()
                    .anyMatch(reparationFileByReparationObject -> reparationFileByReparationObject
                            .getReparationObject()
                            .equals(reparationObjectField.getValue())
                    )) {
                return;
            }
        }

        addReparationObjectToFile(reparationObjectField.getValue());
    }

    private static View getViewToReparationObject() {
        View view = new View(ReparationObject.class);
        view.addProperty("name");
        view.addProperty("invNumber");
        view.addProperty("reparationObjectState");
        return view;
    }

    private void addReparationObjectToFile(ReparationObject selectedReparationObject) {
        selectedReparationObject = dataManager.reload(Objects.requireNonNull(selectedReparationObject), getViewToReparationObject());
        ReparationFileByReparationObject reparationFileByReparationObject = dataManager.create(ReparationFileByReparationObject.class);
        reparationFileByReparationObject.setBusiness(getEditedEntity().getBusiness());
        reparationFileByReparationObject.setCompany(getEditedEntity().getCompany());
        reparationFileByReparationObject.setPartition(getEditedEntity().getPartition());
        reparationFileByReparationObject.setPropertyType(getEditedEntity().getPropertyType());
        reparationFileByReparationObject.setDocumentType(getEditedEntity().getDocumentType());
        reparationFileByReparationObject.setReparationFile(getEditedEntity());
        reparationFileByReparationObject.setReparationObject(selectedReparationObject);
        reparationFileByReparationObject = dataContext.merge(reparationFileByReparationObject);
        reparationObjectsDc.getMutableItems().add(reparationFileByReparationObject);
    }

    @Subscribe("reparationObjectField.createReparationObject")
    public void onReparationObjectFieldCreateReparationObject(Action.ActionPerformedEvent event) {
        ReparationObject newReparationObject = dataManager.create(ReparationObject.class);
        Screen screen = screenBuilders.editor(ReparationObject.class, this)
                .newEntity(newReparationObject)
                .withInitializer(reparationObject -> {
                    reparationObject.setBusiness(getEditedEntity().getBusiness());
                    reparationObject.setCompany(getEditedEntity().getCompany());
                    reparationObject.setPropertyType(getEditedEntity().getPropertyType());
                }).withLaunchMode(OpenMode.DIALOG)
                .build();
        screen.addAfterCloseListener(afterCloseEvent -> addReparationObjectToFile(newReparationObject));
        screen.show();
    }

    @Subscribe("reparationObjectsTable.addMany")
    public void onReparationObjectsTableAddMany(Action.ActionPerformedEvent event) {
        AddReparationObjectToFile screen = screenBuilders.screen(this)
                .withScreenClass(AddReparationObjectToFile.class)
                .withLaunchMode(OpenMode.DIALOG)
                .withAfterCloseListener(afterScreenCloseEvent -> {
                    if (afterScreenCloseEvent.getCloseAction() instanceof AddReparationObjectsAction) {
                        ((AddReparationObjectsAction) afterScreenCloseEvent.getCloseAction())
                                .getReparationObjects()
                                .forEach(this::addReparationObjectToFile);
                    }
                }).build();
        screen.setBusiness(getEditedEntity().getBusiness());
        screen.setCompany(getEditedEntity().getCompany());
        screen.setPartition(getEditedEntity().getPartition());
        screen.setDocumentType(getEditedEntity().getDocumentType());
        screen.setPropertyType(getEditedEntity().getPropertyType());
        screen.show();
    }

    @Subscribe("fileNameField")
    public void onFileNameFieldValueChange(HasValue.ValueChangeEvent<String> event) {
//        if (event.isUserOriginated()) {
//            getEditedEntity().getDocument().setName(
//                    reparationFileService.getFileName(
//                            getEditedEntity().getBusiness(),
//                            getEditedEntity().getCompany(),
//                            getEditedEntity().getPartition(),
//                            getEditedEntity().getPropertyType(),
//                            getEditedEntity().getDocumentType(),
//                            reparationObjectsDc.getItems().size() == 1
//                                    ? reparationObjectsDc.getItems().get(0).getReparationObject()
//                                    : null,
//                            getEditedEntity().getFileName(),
//                            getEditedEntity().getDocument().getExtension()
//                    )
//            );
//            getEditedEntity().setDocument(dataContext.merge(getEditedEntity().getDocument()));
//        }
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        getEditedEntity().getDocument().setName(
                reparationFileService.getFileName(
                        getEditedEntity().getBusiness(),
                        getEditedEntity().getCompany(),
                        getEditedEntity().getPartition(),
                        getEditedEntity().getPropertyType(),
                        getEditedEntity().getDocumentType(),
                        reparationObjectsDc.getItems().size() == 1
                                ? reparationObjectsDc.getItems().get(0).getReparationObject()
                                : null,
                        getEditedEntity().getFileName(),
                        getEditedEntity().getDocument().getExtension()
                )
        );
        getEditedEntity().setDocument(dataContext.merge(getEditedEntity().getDocument()));
    }

    @Subscribe
    public void onBeforeClose(BeforeCloseEvent event) {

    }

}