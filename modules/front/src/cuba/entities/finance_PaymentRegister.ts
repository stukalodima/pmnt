import { StandardEntity } from "./base/sys$StandardEntity";
import { Business } from "./finance_Business";
import { PaymentRegisterDetail } from "./finance_PaymentRegisterDetail";
export class PaymentRegister extends StandardEntity {
  static NAME = "finance_PaymentRegister";
  onDate?: any | null;
  business?: Business | null;
  paymentRegisters?: PaymentRegisterDetail[] | null;
}
export type PaymentRegisterViewName = "_base" | "_local" | "_minimal";
export type PaymentRegisterView<
  V extends PaymentRegisterViewName
> = V extends "_base"
  ? Pick<PaymentRegister, "id" | "onDate" | "business">
  : V extends "_local"
  ? Pick<PaymentRegister, "id" | "onDate">
  : V extends "_minimal"
  ? Pick<PaymentRegister, "id" | "onDate" | "business">
  : never;
