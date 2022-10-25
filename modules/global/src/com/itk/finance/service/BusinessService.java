package com.itk.finance.service;

import com.itk.finance.entity.Business;

import java.util.List;

public interface BusinessService {
    String NAME = "finance_BusinessService";

    List<Business> getGlBusiness();
}