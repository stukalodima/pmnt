package com.itk.finance.web.screens.reparationfilebyreparationobject;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.app.core.file.FileDownloadHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.itk.finance.entity.*;
import com.itk.finance.service.ReparationFileService;
import com.itk.finance.service.UserPropertyService;
import com.itk.finance.web.screens.reparationfile.ReparationFileBrowse;
import com.itk.finance.web.screens.reparationfilebyreparationobject.action.AddReparationObjectsAction;

import javax.inject.Inject;
import java.util.*;

@UiController("finance_ReparationFileByReparationObject.browse")
@UiDescriptor("reparation-file-by-reparation-object-browse.xml")
@LookupComponent("reparationFileByReparationObjectsTable")
public class ReparationFileByReparationObjectBrowse extends StandardLookup<ReparationFileByReparationObject> {

    @Inject
    private LookupField<Business> businessFilterField;
    @Inject
    private LookupField<Company> companyFilterField;
    @Inject
    private CollectionLoader<ReparationFileByReparationObject> formDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private FileMultiUploadField multiUploadField;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private Messages messages;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private Tree<ReparationFilesStructure> reparationFilesStructureTree;
    @Inject
    private Notifications notifications;

    private CommitContext commitContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private CollectionLoader<ReparationFilesStructure> reparationFilesStructureDl;
    @Inject
    private CollectionContainer<ReparationFilesStructure> reparationFilesStructureDc;
    @Inject
    private CollectionLoader<Company> companyDl;
    private ReparationFilesStructure structureElement;
    @Inject
    private CollectionLoader<Business> businessDl;
    @Inject
    private GroupTable<ReparationFileByReparationObject> reparationFileByReparationObjectsTable;
    @Inject
    private CollectionLoader<ReparationObjectState> stateDl;
    @Inject
    private LookupField<ReparationObjectState> stateFilterField;
    @Inject
    private ReparationFileService reparationFileService;

    @Subscribe
    public void onInit(InitEvent event) {
        multiUploadField.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            commitContext = new CommitContext();
            List<ReparationFile> reparationFileList = new ArrayList<>();
            for (Map.Entry<UUID, String> entry : multiUploadField.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileDescriptor fd = fileUploadingAPI.getFileDescriptor(fileId, fileName);
                if (fd != null) {
                    try {
                        fileUploadingAPI.putFileIntoStorage(fileId, fd);
                    } catch (FileStorageException e) {
                        throw new RuntimeException(messages.getMessage(ReparationFileBrowse.class, "reparationFileBrowse.errorUploadToStorage"), e);
                    }
                    commitContext.addInstanceToCommit(fd);
                    ReparationFile reparationFile = dataManager.create(ReparationFile.class);
                    initNewEntityReparationFile(reparationFile, fd);
                    commitContext.addInstanceToCommit(reparationFile);
                    reparationFileList.add(reparationFile);
                }
            }
            AddReparationObjectToFile screen = screenBuilders.screen(this)
                    .withScreenClass(AddReparationObjectToFile.class)
                    .withAfterCloseListener(closeEvent -> {
                        if (closeEvent.getCloseAction() instanceof AddReparationObjectsAction) {
                            List<ReparationObject> reparationObjectList = ((AddReparationObjectsAction) closeEvent.getCloseAction())
                                    .getReparationObjects();
                            if (reparationObjectList.isEmpty()) {
                                multiUploadField.clearUploads();
                                return;
                            }
                            reparationObjectList
                                    .forEach(reparationObject -> reparationFileList.forEach(entity -> {
                                        ReparationFileByReparationObject reparationFileByReparationObject = dataManager.create(ReparationFileByReparationObject.class);
                                        initNewEntityReparationFileByReparationObjects(reparationFileByReparationObject, entity, reparationObject, reparationObjectList.size()>0);
                                        commitContext.addInstanceToCommit(reparationFileByReparationObject);
                                    }));

                            dataManager.commit(commitContext);
                            formDl.load();
                            multiUploadField.clearUploads();
                        } else {
                            multiUploadField.clearUploads();
                        }
                    })
                    .build();
            screen.setBusiness(businessFilterField.getValue());
            screen.setCompany(companyFilterField.getValue());
            screen.setPropertyType(structureElement.getPropertyType());
            screen.show();
        });

        multiUploadField.addFileUploadStartListener(fileUploadStartEvent -> notifications.create().withType(Notifications.NotificationType.ERROR).show());

        multiUploadField.addFileUploadErrorListener(queueFileUploadErrorEvent -> notifications.create()
                .withCaption(messages.getMessage(ReparationFileBrowse.class, "reparationFileBrowse.errorUpload"))
                .show());
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        FileDownloadHelper.initGeneratedColumn(reparationFileByReparationObjectsTable, "reparationFile.document");
        reparationFilesStructureDl.load();
        formDl.load();

        businessFilterField.setValue(userPropertyService.getDefaultBusiness());
        businessDl.load();
        companyFilterField.setValue(userPropertyService.getDefaultCompany());
        if (!reparationFilesStructureDc.getItems().isEmpty()) {
            reparationFilesStructureTree.setSelected(reparationFilesStructureDc.getItems().get(0));
            refreshReparationFileDl();
        }
        stateDl.load();
    }

    @Subscribe("businessFilterField")
    public void onBusinessFilterFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        companyFilterField.clear();
        refreshCompanyDc();
//        ReparationFilesStructure structureElement = reparationFilesStructureTree.getSingleSelected();
        refreshReparationFileDl();
    }

    @Subscribe("companyFilterField")
    public void onCompanyFilterFieldValueChange(HasValue.ValueChangeEvent<Company> event) {
        if (event.isUserOriginated()) {
            refreshReparationFileDl();
        }
    }

    @Subscribe("reparationFilesStructureTree")
    public void onReparationFilesStructureTreeSelection(Tree.SelectionEvent<ReparationFilesStructure> event) {
        structureElement = event.getSource().getSingleSelected();
        refreshReparationFileDl();
    }


    //other procedure
    private void refreshCompanyDc() {
        companyDl.setParameter("business", businessFilterField.getValue());
        companyDl.load();
    }

    private void initNewEntityReparationFileByReparationObjects(ReparationFileByReparationObject reparationFileByReparationObject, ReparationFile reparationFile, ReparationObject reparationObject, boolean multiObject) {
        reparationFileByReparationObject.setReparationFile(reparationFile);
        reparationFileByReparationObject.setReparationObject(reparationObject);
        reparationFileByReparationObject.setBusiness(businessFilterField.getValue());
        reparationFileByReparationObject.setCompany(companyFilterField.getValue());
        reparationFileByReparationObject.setPartition(structureElement.getPartition());
        reparationFileByReparationObject.setPropertyType(structureElement.getPropertyType());
        reparationFileByReparationObject.setDocumentType(structureElement.getDocumentType());

        reparationFileByReparationObject.getReparationFile().getDocument().setName(reparationFileService.getFileName(
                reparationFile.getBusiness(),
                reparationFile.getCompany(),
                reparationFile.getPartition(),
                reparationFile.getPropertyType(),
                reparationFile.getDocumentType(),
                multiObject
                        ? null
                        : reparationObject,
        reparationFile.getFileName(),
                reparationFile.getDocument().getExtension()
        ));
    }

    private void initNewEntityReparationFile(ReparationFile reparationFile, FileDescriptor fileDescriptor) {
        ReparationFilesStructure structure = reparationFilesStructureTree.getSingleSelected();
        reparationFile.setBusiness(
                businessFilterField.getValue() == null
                        ? userPropertyService.getDefaultBusiness()
                        : businessFilterField.getValue()
        );
        reparationFile.setCompany(
                companyFilterField.getValue() == null
                        ? userPropertyService.getDefaultCompany()
                        : companyFilterField.getValue()
        );
        if (structure != null) {
            reparationFile.setPartition(structure.getPartition());
            reparationFile.setPropertyType(structure.getPropertyType());
            reparationFile.setDocumentType(structure.getDocumentType());
        }
        if (fileDescriptor != null) {
            reparationFile.setDocument(fileDescriptor);
            reparationFile.setFileName(fileDescriptor.getName().replaceFirst(".".concat(fileDescriptor.getExtension()), ""));
        }
    }

    private void refreshReparationFileDl() {
        if (businessFilterField.getValue() != null) {
            formDl.setParameter("business", businessFilterField.getValue());
        } else {
            formDl.removeParameter("business");
        }
        if (companyFilterField.getValue() != null) {
            formDl.setParameter("company", companyFilterField.getValue());
        } else {
            formDl.removeParameter("company");
        }
        if (structureElement != null) {
            structureElement = dataManager.reload(structureElement, "reparationFilesStructure-all-property-exclude-pid");
            if (structureElement.getPartition() != null) {
                formDl.setParameter("partition", structureElement.getPartition());
                multiUploadField.setEnabled(true);
            } else {
                formDl.removeParameter("partition");
                multiUploadField.setEnabled(false);
            }
            if (structureElement.getPropertyType() != null) {
                formDl.setParameter("propertyType", structureElement.getPropertyType());
            } else {
                formDl.removeParameter("propertyType");
            }
            if (structureElement.getDocumentType() != null) {
                formDl.setParameter("documentType", structureElement.getDocumentType());
            } else {
                formDl.removeParameter("documentType");
            }
            if (stateFilterField.getValue() != null) {
                formDl.setParameter("state", stateFilterField.getValue());
            } else {
                formDl.removeParameter("state");
            }
        } else {
            formDl.removeParameter("partition");
            formDl.removeParameter("propertyType");
            formDl.removeParameter("documentType");
        }
        formDl.load();
    }

    @Subscribe("reparationFileByReparationObjectsTable.edit")
    public void onReparationFileByReparationObjectsTableEdit(Action.ActionPerformedEvent event) {
        ReparationFile file = Objects.requireNonNull(reparationFileByReparationObjectsTable.getSingleSelected())
                .getReparationFile();
        screenBuilders.editor(ReparationFile.class,this)
                .editEntity(file)
                .withLaunchMode(OpenMode.DIALOG)
                .build()
                .show();
    }

    @Subscribe("stateFilterField")
    public void onStateFilterFieldValueChange(HasValue.ValueChangeEvent<ReparationObjectState> event) {
        if (event.isUserOriginated()) {
            refreshReparationFileDl();
        }
    }
}