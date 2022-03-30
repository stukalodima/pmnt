import { StandardEntity } from "./base/sys$StandardEntity";
import { Company } from "./finance_Company";
import { CashFlowItem } from "./finance_CashFlowItem";
export class CashFlowItemBusiness extends StandardEntity {
  static NAME = "finance_CashFlowItemBusiness";
  name?: string | null;
  company?: Company | null;
  cashFlowItem?: CashFlowItem | null;
}
export type CashFlowItemBusinessViewName =
  | "_base"
  | "_local"
  | "_minimal"
  | "cashFlowItemBusiness-all-property";
export type CashFlowItemBusinessView<
  V extends CashFlowItemBusinessViewName
> = V extends "_base"
  ? Pick<CashFlowItemBusiness, "id" | "name">
  : V extends "_local"
  ? Pick<CashFlowItemBusiness, "id" | "name">
  : V extends "_minimal"
  ? Pick<CashFlowItemBusiness, "id" | "name">
  : V extends "cashFlowItemBusiness-all-property"
  ? Pick<CashFlowItemBusiness, "id" | "name" | "company" | "cashFlowItem">
  : never;
