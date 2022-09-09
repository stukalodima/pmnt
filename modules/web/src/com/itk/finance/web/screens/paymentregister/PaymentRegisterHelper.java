package com.itk.finance.web.screens.paymentregister;

import com.haulmont.cuba.gui.data.GroupInfo;
import com.itk.finance.entity.PayStatusEnum;
import com.itk.finance.entity.PaymentRegisterDetailStatusEnum;

public class PaymentRegisterHelper {
    @SuppressWarnings("unchecked")
    public static String getGroupTableStyleByPayedStatus(@SuppressWarnings("rawtypes") GroupInfo info){
        PayStatusEnum payStatusEnum = (PayStatusEnum) info.getPropertyValue(info.getProperty());
        switch (payStatusEnum) {
            case PAYED:
                return "approved1";
            case PRE_PAYED:
                return "startProc1";
            case DISMISS:
                return "terminated1";
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static String getGroupTableStyleByApproveStatus(@SuppressWarnings("rawtypes") GroupInfo info) {
        PaymentRegisterDetailStatusEnum detailStatusEnum = (PaymentRegisterDetailStatusEnum) info.getPropertyValue(info.getProperty());
        switch (detailStatusEnum) {
            case REJECTED:
                return "terminated1";
            case TERMINATED:
                return "startProc1";
            default:
                return null;
        }
    }


}
