package com.itk.finance.web.screens.paymentclaim;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.PaymentClaim;
import com.itk.finance.service.PaymentClaimService;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;

@UiController("finance_PaymentClaim.browse")
@UiDescriptor("payment-claim-browse.xml")
@LookupComponent("paymentClaimsTable")
@LoadDataBeforeShow
public class PaymentClaimBrowse extends StandardLookup<PaymentClaim> {
    @Inject
    private PaymentClaimService paymentClaimService;
    @Inject
    private CollectionLoader<PaymentClaim> paymentClaimsDl;
    @Inject
    private Notifications notifications;
    @Inject
    private Messages messages;

    @Subscribe("paymentClaimsTable.fillPaymentClaim")
    public void onPaymentClaimsTableFillPaymentClaim(Action.ActionPerformedEvent event) {
        try {
            paymentClaimService.getPaymentClaimListfromExternal();
        } catch (IOException e) {
            notifications.create()
                    .withCaption(messages.getMessage(PaymentClaimBrowse.class, "message.restCall.error.caption"))
                    .withContentMode(ContentMode.HTML)
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription(
                            messages.getMessage(PaymentClaimBrowse.class, "message.restCall.error.text")
                                    +"\n"
                                    + e.getMessage()
                    )
                    .show();
        } catch (ParseException e) {
            notifications.create()
                    .withCaption(messages.getMessage(PaymentClaimBrowse.class, "message.parseDate.error.caption"))
                    .withContentMode(ContentMode.HTML)
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription(
                            messages.getMessage(PaymentClaimBrowse.class, "message.parseDate.error.text")
                                    +"\n"
                                    + e.getMessage()
                    )
                    .show();
        }
        paymentClaimsDl.load();
    }

}