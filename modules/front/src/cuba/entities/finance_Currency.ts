import { StandardEntity } from "./base/sys$StandardEntity";
export class Currency extends StandardEntity {
  static NAME = "finance_Currency";
  code?: string | null;
  shortName?: string | null;
  name?: string | null;
}
export type CurrencyViewName = "_base" | "_local" | "_minimal";
export type CurrencyView<V extends CurrencyViewName> = V extends "_base"
  ? Pick<Currency, "id" | "name" | "code" | "shortName">
  : V extends "_local"
  ? Pick<Currency, "id" | "code" | "shortName" | "name">
  : V extends "_minimal"
  ? Pick<Currency, "id" | "name" | "code">
  : never;
