import { StandardEntity } from "./base/sys$StandardEntity";
import { Business } from "./finance_Business";
import { Company } from "./finance_Company";
import { Client } from "./finance_Client";
import { Account } from "./finance_Account";
import { Currency } from "./finance_Currency";
import { CashFlowItem } from "./finance_CashFlowItem";
import { CashFlowItemBusiness } from "./finance_CashFlowItemBusiness";
import { PaymentType } from "./finance_PaymentType";
import { User } from "./base/sec$User";
import { ClaimStatusEnum } from "../enums/enums";
export class PaymentClaim extends StandardEntity {
  static NAME = "finance_PaymentClaim";
  number?: any | null;
  onDate?: any | null;
  business?: Business | null;
  company?: Company | null;
  client?: Client | null;
  account?: Account | null;
  currency?: Currency | null;
  summ?: any | null;
  planPaymentDate?: any | null;
  paymentPurpose?: string | null;
  cashFlowItem?: CashFlowItem | null;
  cashFlowItemBusiness?: CashFlowItemBusiness | null;
  paymentType?: PaymentType | null;
  comment?: string | null;
  author?: User | null;
  status?: ClaimStatusEnum | null;
}
export type PaymentClaimViewName =
  | "_base"
  | "_local"
  | "_minimal"
  | "paymentClaim.getEdit";
export type PaymentClaimView<V extends PaymentClaimViewName> = V extends "_base"
  ? Pick<
      PaymentClaim,
      | "id"
      | "onDate"
      | "client"
      | "summ"
      | "number"
      | "planPaymentDate"
      | "paymentPurpose"
      | "comment"
      | "status"
    >
  : V extends "_local"
  ? Pick<
      PaymentClaim,
      | "id"
      | "number"
      | "onDate"
      | "summ"
      | "planPaymentDate"
      | "paymentPurpose"
      | "comment"
      | "status"
    >
  : V extends "_minimal"
  ? Pick<PaymentClaim, "id" | "onDate" | "client" | "summ">
  : V extends "paymentClaim.getEdit"
  ? Pick<
      PaymentClaim,
      | "id"
      | "number"
      | "onDate"
      | "summ"
      | "planPaymentDate"
      | "paymentPurpose"
      | "comment"
      | "status"
      | "business"
      | "company"
      | "client"
      | "cashFlowItem"
      | "paymentType"
      | "author"
      | "cashFlowItemBusiness"
    >
  : never;
