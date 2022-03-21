import { StandardEntity } from "./base/sys$StandardEntity";
export class PaymentType extends StandardEntity {
  static NAME = "finance_PaymentType";
  name?: string | null;
}
export type PaymentTypeViewName = "_base" | "_local" | "_minimal";
export type PaymentTypeView<V extends PaymentTypeViewName> = V extends "_base"
  ? Pick<PaymentType, "id" | "name">
  : V extends "_local"
  ? Pick<PaymentType, "id" | "name">
  : V extends "_minimal"
  ? Pick<PaymentType, "id" | "name">
  : never;
