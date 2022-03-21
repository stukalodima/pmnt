import { StandardEntity } from "./base/sys$StandardEntity";
import { Business } from "./finance_Business";
import { Company } from "./finance_Company";
import { Client } from "./finance_Client";
import { CashFlowItem } from "./finance_CashFlowItem";
import { PaymentType } from "./finance_PaymentType";
import { ClaimStatusEnum } from "../enums/enums";
export class PaymentClaim extends StandardEntity {
  static NAME = "finance_PaymentClaim";
  onDate?: any | null;
  business?: Business | null;
  company?: Company | null;
  client?: Client | null;
  summ?: any | null;
  planPaymentDate?: any | null;
  paymentPurpose?: string | null;
  cashFlowItem?: CashFlowItem | null;
  paymentType?: PaymentType | null;
  comment?: string | null;
  status?: ClaimStatusEnum | null;
}
export type PaymentClaimViewName = "_base" | "_local" | "_minimal";
export type PaymentClaimView<V extends PaymentClaimViewName> = V extends "_base"
  ? Pick<
      PaymentClaim,
      | "id"
      | "onDate"
      | "summ"
      | "planPaymentDate"
      | "paymentPurpose"
      | "comment"
      | "status"
    >
  : V extends "_local"
  ? Pick<
      PaymentClaim,
      | "id"
      | "onDate"
      | "summ"
      | "planPaymentDate"
      | "paymentPurpose"
      | "comment"
      | "status"
    >
  : never;
