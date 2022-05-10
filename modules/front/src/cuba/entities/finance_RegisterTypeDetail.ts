import { StandardEntity } from "./base/sys$StandardEntity";
import { CashFlowItem } from "./finance_CashFlowItem";
import { RegisterType } from "./finance_RegisterType";
export class RegisterTypeDetail extends StandardEntity {
  static NAME = "finance_RegisterTypeDetail";
  cashFlowItem?: CashFlowItem | null;
  registerType?: RegisterType | null;
}
export type RegisterTypeDetailViewName = "_base" | "_local" | "_minimal";
export type RegisterTypeDetailView<
  V extends RegisterTypeDetailViewName
> = never;
