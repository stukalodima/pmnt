package com.itk.finance.service;

import com.itk.finance.entity.*;

public interface ReparationFileService {
    String NAME = "finance_ReparationFileService";

    String getFileName(Business business, Company company, Partition partition, PropertyType propertyType,
                       DocumentType documentType, ReparationObject reparationObject, String fileName, String extension);
}