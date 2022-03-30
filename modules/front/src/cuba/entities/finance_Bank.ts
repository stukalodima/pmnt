import { StandardEntity } from "./base/sys$StandardEntity";
export class Bank extends StandardEntity {
  static NAME = "finance_Bank";
  name?: string | null;
  mfo?: string | null;
  edrpou?: string | null;
}
export type BankViewName = "_base" | "_local" | "_minimal";
export type BankView<V extends BankViewName> = V extends "_base"
  ? Pick<Bank, "id" | "name" | "mfo" | "edrpou">
  : V extends "_local"
  ? Pick<Bank, "id" | "name" | "mfo" | "edrpou">
  : V extends "_minimal"
  ? Pick<Bank, "id" | "name" | "mfo">
  : never;
