import { StandardEntity } from "./base/sys$StandardEntity";
import { PaymentRegisterDetailStatusEnum } from "../enums/enums";
import { Company } from "./finance_Company";
import { Client } from "./finance_Client";
import { CashFlowItem } from "./finance_CashFlowItem";
import { PaymentType } from "./finance_PaymentType";
import { PaymentClaim } from "./finance_PaymentClaim";
import { PaymentRegister } from "./finance_PaymentRegister";
export class PaymentRegisterDetail extends StandardEntity {
  static NAME = "finance_PaymentRegisterDetail";
  approved?: PaymentRegisterDetailStatusEnum | null;
  company?: Company | null;
  client?: Client | null;
  summ?: any | null;
  paymentPurpose?: string | null;
  cashFlowItem?: CashFlowItem | null;
  paymentType?: PaymentType | null;
  comment?: string | null;
  paymentClaim?: PaymentClaim | null;
  paymentRegister?: PaymentRegister | null;
}
export type PaymentRegisterDetailViewName = "_base" | "_local" | "_minimal";
export type PaymentRegisterDetailView<
  V extends PaymentRegisterDetailViewName
> = V extends "_base"
  ? Pick<
      PaymentRegisterDetail,
      "id" | "client" | "summ" | "approved" | "paymentPurpose" | "comment"
    >
  : V extends "_local"
  ? Pick<
      PaymentRegisterDetail,
      "id" | "approved" | "summ" | "paymentPurpose" | "comment"
    >
  : V extends "_minimal"
  ? Pick<PaymentRegisterDetail, "id" | "client" | "summ">
  : never;
