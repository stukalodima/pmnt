package com.itk.finance.service;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.itk.finance.entity.*;
import org.springframework.stereotype.Service;

@Service(ReparationFileService.NAME)
public class ReparationFileServiceBean implements ReparationFileService {

    @Override
    public String getFileName(Business business, Company company, Partition partition, PropertyType propertyType,
                              DocumentType documentType, ReparationObject reparationObject, String fileName, String extension) {
        DataManager dataManager = AppBeans.get(DataManager.class);
        String busName = business == null
                ? null
                : dataManager.reload(business, ViewBuilder.of(Business.class).add("nameForFile").build()).getNameForFile();
        String comName = company == null
                ? null
                : dataManager.reload(company, ViewBuilder.of(Company.class).add("nameForFile").build()).getNameForFile();
        String parName = partition == null
                ? null
                : dataManager.reload(partition, ViewBuilder.of(Partition.class).add("nameForFile").build()).getNameForFile();
        String propName = propertyType == null
                ? null
                : dataManager.reload(propertyType, ViewBuilder.of(PropertyType.class).add("nameForFile").build()).getNameForFile();
        String docName = documentType == null
                ? null
                : dataManager.reload(documentType, ViewBuilder.of(DocumentType.class).add("nameForFile").build()).getNameForFile();
        String objName = reparationObject == null
                ? "З"
                : dataManager.reload(reparationObject, ViewBuilder.of(ReparationObject.class).add("nameForFile").build()).getNameForFile();

        String separatorString = "_";
        String newFileName = "";
        if (busName != null) {
            newFileName = String.join(separatorString, newFileName, busName);
        }
        if (comName != null) {
            newFileName = String.join(separatorString, newFileName, comName);
        }
        if (parName != null) {
            newFileName = String.join(separatorString, newFileName, parName);
        }
        if (propName != null) {
            newFileName = String.join(separatorString, newFileName, propName);
        }
        if (docName != null) {
            newFileName = String.join(separatorString, newFileName, docName);
        }
        newFileName = String.join(separatorString, newFileName, objName, fileName);
        newFileName = newFileName + "." + extension;
        if (newFileName.startsWith(separatorString)) {
            newFileName = newFileName.replaceFirst(separatorString, "");
        }
        return newFileName;
    }
}