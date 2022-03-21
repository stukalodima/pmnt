import { StandardEntity } from "./base/sys$StandardEntity";
export class CashFlowItem extends StandardEntity {
  static NAME = "finance_CashFlowItem";
  name?: string | null;
}
export type CashFlowItemViewName = "_base" | "_local" | "_minimal";
export type CashFlowItemView<V extends CashFlowItemViewName> = V extends "_base"
  ? Pick<CashFlowItem, "id" | "name">
  : V extends "_local"
  ? Pick<CashFlowItem, "id" | "name">
  : V extends "_minimal"
  ? Pick<CashFlowItem, "id" | "name">
  : never;
