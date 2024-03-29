package com.itk.finance.core;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.entity.User;
import com.itk.finance.entity.PaymentRegister;
import com.itk.finance.entity.PaymentRegisterDetail;
import com.itk.finance.entity.PaymentRegisterDetailStatusEnum;
import com.itk.finance.service.EmailService;
import com.itk.finance.service.ProcPropertyService;
import com.itk.finance.service.UserPropertyService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component(ApprovalHelperBean.NAME)
public class ApprovalHelperBean {
    public static final String NAME = "finance_ApprovalHelperBean";
    public static final String MAIL_REJECTED_LATER_EMAIL_CAPTION = "mail.rejectedLaterEmailCaption";
    public static final String MAIL_REJECTED_LATER_EMAIL_BODY = "mail.rejectedLaterEmailBody";
    public static final String DD_MM_YYYY = "dd.MM.yyyy";
    public static final String MAIL_FINISH_LATER_EMAIL_CAPTION = "mail.finishLaterEmailCaption";
    public static final String MAIL_FINISH_LATER_EMAIL_BODY = "mail.finishLaterEmailBody";
    @Inject
    private ProcPropertyService procPropertyService;
    @Inject
    private Persistence persistence;
    @Inject
    private Messages messages;
    @Inject
    private EmailService emailService;
    @Inject
    private GlobalConfig globalConfig;
    @Inject
    private UserPropertyService userPropertyService;

    @SuppressWarnings("unused")
    public void updateStateRegister(UUID entityId, String state) {
        procPropertyService.updateStateRegister(entityId, state);
    }

    @SuppressWarnings("unused")
    public void rejectPaymentRegister(UUID entityId, String state) throws IOException, EmailException {
        PaymentRegister paymentRegister = persistence.getEntityManager().find(PaymentRegister.class, entityId);
        if (Objects.isNull(paymentRegister)) {
            return;
        }
        procPropertyService.updateStateRegister(entityId, state);
        changeStatusToRejected(paymentRegister);
        sendRejectedLater(paymentRegister);
    }

    @SuppressWarnings("unused")
    public void updateStateRegisterAndSendFinishLater(UUID entityId, String state) throws IOException, EmailException {
        procPropertyService.updateStateRegister(entityId, state);

        PaymentRegister paymentRegister = persistence.getEntityManager().find(PaymentRegister.class, entityId);
        if (Objects.isNull(paymentRegister)) {
            return;
        }

        procPropertyService.updateStateRegister(entityId, state);

        sendFinishLater(paymentRegister);
    }

    private void sendRejectedLater(PaymentRegister paymentRegister) throws IOException, EmailException {
        Map<String, Object[]> tableData = getTableDataByStatus(paymentRegister, PaymentRegisterDetailStatusEnum.REJECTED);
        String attachmentFileName = createExcelFile(
                messages.getMessage(ApprovalHelperBean.class, "file.Name.Rejected"),
                paymentRegister,
                tableData
        );

        String addressList = getAddressListBase(paymentRegister).toString();
        ArrayList<String> attachmentList = new ArrayList<>();
        attachmentList.add(attachmentFileName);

        sendEmailByStatus(paymentRegister, addressList, MAIL_REJECTED_LATER_EMAIL_CAPTION,
                MAIL_REJECTED_LATER_EMAIL_BODY, attachmentList);
        attachmentList.clear();
    }

    private void sendEmailByStatus(PaymentRegister paymentRegister, String addressList, String keyCaption, String keyEmailBody, List<String> attachmentFileName) throws IOException, EmailException {
        if (!addressList.isEmpty()) {
            EmailAttachment[] emailAttachments = new EmailAttachment[attachmentFileName.size()];
            int index = 0;
            for (String fileName : attachmentFileName) {
                byte[] fileInputStream = Files.readAllBytes(Paths.get(globalConfig.getTempDir() + "/" + fileName));
                emailAttachments[index] = new EmailAttachment(fileInputStream, fileName);
                index++;
            }

            emailService.sendEmail(addressList,
                    messages.getMessage(ApprovalHelperBean.class, keyCaption) + " "
                            + getRegisterNameForLater(paymentRegister),
                    messages.getMessage(ApprovalHelperBean.class, keyEmailBody),
                    emailAttachments);
        }
    }

    private StringBuilder getAddressListBase(PaymentRegister paymentRegister) {
        StringBuilder addressList = new StringBuilder();

        if (!Objects.isNull(paymentRegister.getAuthor().getEmail()) && !userPropertyService.dontSendEmailByApprovalResult(paymentRegister.getAuthor())) {
            addUserAddress(addressList, paymentRegister.getAuthor());
        }
        if (!Objects.isNull(paymentRegister.getBusiness().getFinDirector().getEmail())
                && !userPropertyService.dontSendEmailByApprovalResult(paymentRegister.getBusiness().getFinDirector())) {
            addUserAddress(addressList, paymentRegister.getBusiness().getFinDirector());
        }

        return addressList;
    }

    private Map<String, Object[]> getTableDataByStatus(PaymentRegister paymentRegister, PaymentRegisterDetailStatusEnum statusEnum) {

        Map<String, Object[]> tableData = new TreeMap<>();

        tableData.put("1", getStringArrayTableHeader());

        int approvedIndex = 2;
        List<PaymentRegisterDetail> detailList = paymentRegister.getPaymentRegisters()
                .stream()
                .sorted(Comparator.comparing(o -> o.getPaymentClaim().getCompany().getName()))
                .sorted(Comparator.comparing(ob -> ob.getPaymentClaim().getAccount().getIban()))
                .collect(Collectors.toList());

        for (PaymentRegisterDetail e : detailList) {
            if (e.getApproved().equals(statusEnum)) {
                tableData.put(String.format("%d", approvedIndex++), getStringArrayPaymentClaim(e));
            }
        }

        return tableData;
    }

    private String createExcelFile(String sheetName, PaymentRegister paymentRegister, Map<String, Object[]> tableData) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont font = workbook.createFont();
        font.setBold(true);

        Color COLOR_light_gray  = new java.awt.Color(232, 232, 232);

        XSSFCellStyle styleHeader = workbook.createCellStyle();

        styleHeader.setFont(font);
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //noinspection deprecation
        styleHeader.setFillForegroundColor(new XSSFColor(COLOR_light_gray));
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle styleCell = workbook.createCellStyle();
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);
        styleCell.setBorderTop(BorderStyle.THIN);

        // spreadsheet object
        XSSFSheet spreadSheet = workbook.createSheet(sheetName);

        fillSpreadsheet(tableData, spreadSheet, styleHeader, styleCell);

        for (int i = 1; i <= 11; i++) {
            spreadSheet.autoSizeColumn(i);
        }

        DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);

        FileOutputStream out;
        String fileName = sheetName + messages.getMessage(ApprovalHelperBean.class, "fileName.Register") +
                paymentRegister.getNumber() +
                messages.getMessage(ApprovalHelperBean.class, "fileName.DateFrom") +
                dateFormat.format(paymentRegister.getOnDate()) +
                messages.getMessage(ApprovalHelperBean.class, "fileName.extension");
        out = new FileOutputStream(globalConfig.getTempDir() + "/" + fileName);
        workbook.write(out);
        out.close();

        return fileName;
    }

    private void changeStatusToRejected(PaymentRegister paymentRegister) {
        try (Transaction transaction = persistence.getTransaction()) {
            paymentRegister.getPaymentRegisters().forEach(e -> e.setApproved(PaymentRegisterDetailStatusEnum.REJECTED));
            transaction.commit();
        }
    }

    private String getRegisterNameForLater(PaymentRegister paymentRegister) {
        DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
        return paymentRegister.getNumber().toString() +
                " " +
                messages.getMessage(ApprovalHelperBean.class, "mail.captionFrom") +
                " " +
                dateFormat.format(paymentRegister.getOnDate()) +
                " " +
                messages.getMessage(ApprovalHelperBean.class, "mail.captionBN") +
                " " +
                paymentRegister.getBusiness().getName() +
                " " +
                messages.getMessage(ApprovalHelperBean.class, "mail.captionRegisterType") +
                " " +
                paymentRegister.getRegisterType().getName();
    }

    private void sendFinishLater(PaymentRegister paymentRegister) throws IOException, EmailException {
        Map<String, Object[]> tableDataApprove = getTableDataByStatus(paymentRegister, PaymentRegisterDetailStatusEnum.APPROVED);
        String attachmentFileNameApprove = createExcelFile(
                messages.getMessage(ApprovalHelperBean.class, "file.Name.Approved"),
                paymentRegister,
                tableDataApprove
        );

        Map<String, Object[]> tableDataTerminate = getTableDataByStatus(paymentRegister, PaymentRegisterDetailStatusEnum.TERMINATED);
        String attachmentFileNameTerminate = createExcelFile(
                messages.getMessage(ApprovalHelperBean.class, "file.Name.Determinate"),
                paymentRegister,
                tableDataTerminate
        );

        Map<String, Object[]> tableDataRejected = getTableDataByStatus(paymentRegister, PaymentRegisterDetailStatusEnum.REJECTED);
        String attachmentFileNameRejected = createExcelFile(
                messages.getMessage(ApprovalHelperBean.class, "file.Name.Rejected"),
                paymentRegister,
                tableDataRejected
        );

        String addressList = getAddressListBase(paymentRegister).toString();

        List<String> stringList = new ArrayList<>();
        if (tableDataApprove.size() > 1) {
            stringList.add(attachmentFileNameApprove);
        }
        if (tableDataTerminate.size() > 1) {
            stringList.add(attachmentFileNameTerminate);
        }
        if (tableDataRejected.size() > 1) {
            stringList.add(attachmentFileNameRejected);
        }

        sendEmailByStatus(paymentRegister, addressList, MAIL_FINISH_LATER_EMAIL_CAPTION,
                MAIL_FINISH_LATER_EMAIL_BODY, stringList);

        StringBuilder addressListUser = new StringBuilder();
        paymentRegister.getBusiness().getOperatorList().forEach(e -> addUserAddress(addressListUser, e.getUser()));
        paymentRegister.getBusiness().getControllerList().forEach(e -> addUserAddress(addressListUser, e.getUser()));

        stringList.clear();
        stringList.add(attachmentFileNameApprove);

        sendEmailByStatus(paymentRegister, addressListUser.toString(), MAIL_FINISH_LATER_EMAIL_CAPTION,
                MAIL_FINISH_LATER_EMAIL_BODY, stringList);
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

    private void fillSpreadsheet(Map<String, Object[]> mapData, XSSFSheet spreadsheetData, XSSFCellStyle styleHeader, XSSFCellStyle cellStyle) {
        XSSFRow row;
        Set<String> keyId = mapData.keySet();
        int rowId = 0;
        for (String key : keyId) {
            row = spreadsheetData.createRow(rowId++);
            Object[] objectArr = mapData.get(key);
            int cellId = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellId++);
                if (key.equals("1")) {
                    cell.setCellStyle(styleHeader);
                } else {
                    cell.setCellStyle(cellStyle);
                }
                if (!Objects.isNull(obj) && obj.getClass() == Double.class) {
                    cell.setCellValue((Double) obj);
                } else {
                    cell.setCellValue((String) obj);
                }
            }
        }
    }

    private Object[] getStringArrayTableHeader() {
        return new Object[]{
                "Статус", "Организация", "Контрагент", "Расчетный счет", "Сумма", "Валюта", "Плановая дата платежа",
                "Назначение платежа", "Статья ДДС", "Тип оплаты", "Комментарий заявки", "Комментарий согласования"
        };
    }

    private Object[] getStringArrayPaymentClaim(PaymentRegisterDetail e) {
        DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator(',');
        return new Object[]{
                messages.getMessage(PaymentRegisterDetailStatusEnum.class, "PaymentRegisterDetailStatusEnum." + e.getApproved().toString()),
                e.getPaymentClaim().getCompany().getShortName(),
                e.getPaymentClaim().getClient().getShortName(),
                e.getPaymentClaim().getAccount().getIban(),
                e.getPaymentClaim().getSumm(),
                e.getPaymentClaim().getCurrency().getShortName(),
                dateFormat.format(e.getPaymentClaim().getPlanPaymentDate()),
                e.getPaymentClaim().getPaymentPurpose(),
                e.getPaymentClaim().getCashFlowItem().getName(),
                e.getPaymentClaim().getPaymentType().getName(),
                e.getPaymentClaim().getComment(),
                e.getComment()
        };
    }
}