package com.itk.finance.web.screens.reparationfile;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.ReparationFile;
import com.itk.finance.entity.ReparationFilesStructure;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

@UiController("finance_ReparationFile.browse")
@UiDescriptor("reparation-file-browse.xml")
@LookupComponent("reparationFilesTable")
@LoadDataBeforeShow
public class ReparationFileBrowse extends StandardLookup<ReparationFile> {
    @Inject
    private CollectionContainer<ReparationFilesStructure> reparationFilesStructureDc;
    @Inject
    private Tree<ReparationFilesStructure> reparationFilesStructureTree;
    @Inject
    private Icons icons;
    @Inject
    private CollectionLoader<ReparationFile> reparationFilesDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private Metadata metadata;
    @Inject
    private FileMultiUploadField multiUploadField;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private Notifications notifications;
    @Inject
    private LookupPickerField<Business> businessFilterField;
    @Inject
    private LookupPickerField<Company> companyFilterField;
    @Inject
    private CollectionLoader<Company> companyDl;
    @Inject
    private Messages messages;

    @Subscribe
    public void onInit(InitEvent event) {
        reparationFilesStructureTree.setIconProvider(reparationFilesStructure -> icons.get(CubaIcon.FOLDER_O));
        initReparationFilesDc();
        refreshCompanyDc(true);
        multiUploadField.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            CommitContext commitContext = new CommitContext();
            for (Map.Entry<UUID, String> entry : multiUploadField.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileDescriptor fd = fileUploadingAPI.getFileDescriptor(fileId, fileName);
                if (fd != null) {
                    try {
                        fileUploadingAPI.putFileIntoStorage(fileId, fd);
                    } catch (FileStorageException e) {
                        throw new RuntimeException(messages.getMessage(ReparationFileBrowse.class,"reparationFileBrowse.errorUploadToStorage"), e);
                    }
                    commitContext.addInstanceToCommit(fd);
                    ReparationFile reparationFile = metadata.create(ReparationFile.class);
                    initNewEntity(reparationFile, fd);
                    reparationFile.setBusiness(businessFilterField.getValue());
                    reparationFile.setCompany(companyFilterField.getValue());
                    commitContext.addInstanceToCommit(reparationFile);
                }
            }
            dataManager.commit(commitContext);
            reparationFilesDl.load();
            multiUploadField.clearUploads();
        });

        multiUploadField.addFileUploadErrorListener(queueFileUploadErrorEvent -> notifications.create()
                .withCaption(messages.getMessage(ReparationFileBrowse.class, "reparationFileBrowse.errorUpload"))
                .show());
    }

    private void refreshCompanyDc(boolean initForm) {
        if (Boolean.TRUE.equals(initForm)) {
            companyDl.setParameter("business", userPropertyService.getDefaultBusiness());
        } else {
            companyDl.setParameter("business", businessFilterField.getValue());
        }
        companyDl.load();
    }

    @Subscribe("businessFilterField")
    public void onBusinessFilterFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        if (event.isUserOriginated()) {
            companyFilterField.clear();
            refreshCompanyDc(false);
            ReparationFilesStructure structureElement = reparationFilesStructureTree.getSingleSelected();
            if (structureElement == null) {
                initReparationFilesDc();
            } else {
                refreshReparationFileDl(structureElement);
            }
        }
    }

    @Subscribe("companyFilterField")
    public void onCompanyFilterFieldValueChange(HasValue.ValueChangeEvent<Company> event) {
        if (event.isUserOriginated()) {
            ReparationFilesStructure structureElement = reparationFilesStructureTree.getSingleSelected();
            if (structureElement == null) {
                initReparationFilesDc();
            } else {
                refreshReparationFileDl(structureElement);
            }
        }
    }

    private void initReparationFilesDc() {
        reparationFilesDl.setParameter("business", businessFilterField.getValue());
        reparationFilesDl.setParameter("partition", companyFilterField.getValue());
        reparationFilesDl.setParameter("partition", null);
        reparationFilesDl.setParameter("propertyType", null);
        reparationFilesDl.setParameter("documentType", null);
        reparationFilesDl.load();
    }

    @Install(to = "reparationFilesTable.create", subject = "newEntitySupplier")
    private ReparationFile reparationFilesTableCreateNewEntitySupplier() {
        ReparationFile reparationFile = metadata.create(ReparationFile.class);
        initNewEntity(reparationFile, null);
        return reparationFile;
    }

    private void initNewEntity(ReparationFile reparationFile, FileDescriptor fileDescriptor) {
        ReparationFilesStructure structure = reparationFilesStructureTree.getSingleSelected();
        reparationFile.setBusiness(businessFilterField.getValue() == null ? userPropertyService.getDefaultBusiness() : businessFilterField.getValue());
        reparationFile.setCompany(companyFilterField.getValue() == null ? userPropertyService.getDefaultCompany() : companyFilterField.getValue());
        if (structure != null) {
            reparationFile.setPartition(structure.getPartition());
            reparationFile.setPropertyType(structure.getPropertyType());
            reparationFile.setDocumentType(structure.getDocumentType());
        }
        if (fileDescriptor != null) {
            reparationFile.setDocument(fileDescriptor);
        }
    }

    @Subscribe("reparationFilesStructureTree")
    public void onReparationFilesStructureTreeSelection(Tree.SelectionEvent<ReparationFilesStructure> event) {
        if (event.isUserOriginated()) {
            ReparationFilesStructure structureElement = event.getSource().getSingleSelected();
            if (structureElement == null) {
                initReparationFilesDc();
            } else {
                refreshReparationFileDl(structureElement);
            }
        }
    }

    private void refreshReparationFileDl(ReparationFilesStructure structureElement) {
        if (businessFilterField.getValue() != null) {
            reparationFilesDl.setParameter("business", businessFilterField.getValue());
        } else {
            reparationFilesDl.removeParameter("business");
        }
        if (companyFilterField.getValue() != null) {
            reparationFilesDl.setParameter("company", companyFilterField.getValue());
        } else {
            reparationFilesDl.removeParameter("company");
        }
        if (structureElement != null) {
            structureElement = dataManager.reload(structureElement, "reparationFilesStructure-all-property-exclude-pid");
            if (structureElement.getPartition() != null) {
                reparationFilesDl.setParameter("partition", structureElement.getPartition());
            } else {
                reparationFilesDl.removeParameter("partition");
            }
            if (structureElement.getPropertyType() != null) {
                reparationFilesDl.setParameter("propertyType", structureElement.getPropertyType());
            } else {
                reparationFilesDl.removeParameter("propertyType");
            }
            if (structureElement.getDocumentType() != null) {
                reparationFilesDl.setParameter("documentType", structureElement.getDocumentType());
            } else {
                reparationFilesDl.removeParameter("documentType");
            }
        } else {
            reparationFilesDl.removeParameter("partition");
            reparationFilesDl.removeParameter("propertyType");
            reparationFilesDl.removeParameter("documentType");
        }
        reparationFilesDl.load();
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        businessFilterField.setValue(userPropertyService.getDefaultBusiness());
        companyFilterField.setValue(userPropertyService.getDefaultCompany());
        if (!reparationFilesStructureDc.getItems().isEmpty()) {
            reparationFilesStructureTree.setSelected(reparationFilesStructureDc.getItems().get(0));
            refreshReparationFileDl(reparationFilesStructureDc.getItems().get(0));
        }
    }
}