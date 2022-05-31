package com.itk.finance.core;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.entity.PaymentRegisterDetail;
import com.itk.finance.entity.PaymentRegisterDetailStatusEnum;
import com.itk.finance.service.EmailService;
import com.itk.finance.service.ProcPropertyService;
import com.itk.finance.service.UserPropertyService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(ApprovalHelperBean.NAME)
public class ApprovalHelperBean {
    public static final String NAME = "finance_ApprovalHelperBean";

    @Inject
    private ProcPropertyService procPropertyService;
    @Inject
    private Persistence persistence;
    @Inject
    private Messages messages;
    @Inject
    private EmailService emailService;
    @Inject
    private TimeSource timeSource;
    @Inject
    private GlobalConfig globalConfig;
    @Inject
    private UserPropertyService userPropertyService;

    public void updateStateRegister(UUID entityId, String state) {
        procPropertyService.updateStateRegister(entityId, state);
    }

    public void updateStateRegisterAndSendFinishLater(UUID entityId, String state) throws IOException, EmailException {
        Map<String, Object[]> approvedData
                = new TreeMap<>();
        Map<String, Object[]> terminatedData
                = new TreeMap<>();
        Map<String, Object[]> rejectedData
                = new TreeMap<>();

        StringBuilder registerName = new StringBuilder();

        approvedData.put("1", getStringArrayTableHeader());
        terminatedData.put("1", getStringArrayTableHeader());
        rejectedData.put("1", getStringArrayTableHeader());

        int approvedIndex = 2;
        int terminatedIndex = 2;
        int rejectedIndex = 2;

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try (Transaction tx = persistence.getTransaction()) {
            PaymentRegister paymentRegister = persistence.getEntityManager().find(PaymentRegister.class, entityId);
            if (!Objects.isNull(paymentRegister)) {
                registerName
                        .append(paymentRegister.getNumber().toString())
                        .append(" от ")
                        .append(dateFormat.format(paymentRegister.getOnDate()))
                        .append(" Бизнес направление: ")
                        .append(paymentRegister.getBusiness().getName())
                        .append(" Тип реестра: ")
                        .append(paymentRegister.getRegisterType().getName());
                for (PaymentRegisterDetail e : paymentRegister.getPaymentRegisters()) {
                    if (e.getApproved().equals(PaymentRegisterDetailStatusEnum.APPROVED)) {
                        approvedData.put(String.format("%d", approvedIndex++), getStringArrayPaymentClaim(e));
                    } else if (e.getApproved().equals(PaymentRegisterDetailStatusEnum.TERMINATED)) {
                        terminatedData.put(String.format("%d", terminatedIndex++), getStringArrayPaymentClaim(e));
                    } else if (e.getApproved().equals(PaymentRegisterDetailStatusEnum.REJECTED)) {
                        rejectedData.put(String.format("%d", rejectedIndex++), getStringArrayPaymentClaim(e));
                    }
                }

                StringBuilder addressList = new StringBuilder();

                addUserAddress(addressList, paymentRegister.getAuthor());
                addUserAddress(addressList, paymentRegister.getBusiness().getFinDirector());

                paymentRegister.getBusiness().getOperatorList().forEach(e -> addUserAddress(addressList, e.getUser()));
                paymentRegister.getBusiness().getControllerList().forEach(e -> addUserAddress(addressList, e.getUser()));

                createEmailWithAttachment(addressList.toString(), approvedData, terminatedData, rejectedData, registerName);

                procPropertyService.updateStateRegister(entityId, state);
            }
            tx.commit();
        }
    }

    private void addUserAddress(StringBuilder addressList, User user) {
        if (!Objects.isNull(user) && !Objects.isNull(user.getEmail())) {
            if (!userPropertyService.dontSendEmailByApprovalResult(user)) {
                if (!user.getEmail().isEmpty()) {
                    addressList.append(user.getEmail()).append(";");
                }
            }
        }
    }

    @SuppressWarnings("all")
    private void createEmailWithAttachment(String addressList, Map<String, Object[]> approvedData, Map<String, Object[]> terminatedData, Map<String, Object[]> rejectedData, StringBuilder registerName) throws IOException, EmailException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont font = new XSSFFont();
        font.setBold(true);
        XSSFCellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setFont(font);
        // spreadsheet object
        XSSFSheet spreadsheetApproved
                = workbook.createSheet("Согласовано");
        XSSFSheet spreadsheetTerminated
                = workbook.createSheet("Отложено");
        XSSFSheet spreadsheetRejected
                = workbook.createSheet("Отклонено");

        fillSpreadsheet(approvedData, spreadsheetApproved, styleHeader);
        fillSpreadsheet(terminatedData, spreadsheetTerminated, styleHeader);
        fillSpreadsheet(rejectedData, spreadsheetRejected, styleHeader);

        FileOutputStream out;
        String fileName = "Реестр_на_оплату" + timeSource.currentTimeMillis() + ".xlsx";
        out = new FileOutputStream(
                globalConfig.getTempDir() + "/" + fileName);
        workbook.write(out);
        out.close();

        if (!addressList.isEmpty()) {
            FileInputStream fileInputStream = new FileInputStream(globalConfig.getTempDir() + "/" + fileName);
            emailService.sendEmail(addressList,
                    messages.getMessage(ApprovalHelperBean.class, "mail.finishLaterEmailCaption") + " " + registerName,
                    messages.getMessage(ApprovalHelperBean.class, "mail.finishLaterEmailBody"),
                    new EmailAttachment(fileInputStream.readAllBytes(), fileName));
        }
    }

    private void fillSpreadsheet(Map<String, Object[]> mapData, XSSFSheet spreadsheetData, XSSFCellStyle cellStyle) {
        XSSFRow row;
        Set<String> keyId = mapData.keySet();
        int rowId = 0;
        for (String key : keyId) {
            row = spreadsheetData.createRow(rowId++);
            if (key.equals("1")) {
                row.setRowStyle(cellStyle);
            }
            Object[] objectArr = mapData.get(key);
            int cellId = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue((String) obj);
            }
        }
    }

    private Object[] getStringArrayTableHeader() {
        return new Object[]{
                "Организация", "Контрагент", "Расчетный счет", "Сумма", "Валюта", "Плановая дата платежа", "Назначение платежа", "Статья ДДС",
                "Тип оплаты", "Комментарий"
        };
    }

    private Object[] getStringArrayPaymentClaim(PaymentRegisterDetail e) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return new Object[]{
                e.getPaymentClaim().getCompany().getShortName(),
                e.getPaymentClaim().getClient().getShortName(),
                e.getPaymentClaim().getAccount().getIban(),
                String.format("%,.2f", e.getPaymentClaim().getSumm()),
                e.getPaymentClaim().getCurrency().getShortName(),
                dateFormat.format(e.getPaymentClaim().getPlanPaymentDate()),
                e.getPaymentClaim().getPaymentPurpose(),
                e.getPaymentClaim().getCashFlowItem().getName(),
                e.getPaymentClaim().getPaymentType().getName(),
                e.getPaymentClaim().getComment()
        };
    }
}