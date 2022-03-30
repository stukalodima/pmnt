import { StandardEntity } from "./base/sys$StandardEntity";
import { Business } from "./finance_Business";
import { ClaimStatusEnum } from "../enums/enums";
import { User } from "./base/sec$User";
import { PaymentRegisterDetail } from "./finance_PaymentRegisterDetail";
export class PaymentRegister extends StandardEntity {
  static NAME = "finance_PaymentRegister";
  number?: any | null;
  onDate?: any | null;
  business?: Business | null;
  status?: ClaimStatusEnum | null;
  author?: User | null;
  paymentRegisters?: PaymentRegisterDetail[] | null;
}
export type PaymentRegisterViewName = "_base" | "_local" | "_minimal";
export type PaymentRegisterView<
  V extends PaymentRegisterViewName
> = V extends "_base"
  ? Pick<PaymentRegister, "id" | "onDate" | "business" | "number" | "status">
  : V extends "_local"
  ? Pick<PaymentRegister, "id" | "number" | "onDate" | "status">
  : V extends "_minimal"
  ? Pick<PaymentRegister, "id" | "onDate" | "business">
  : never;
