import { StandardEntity } from "./base/sys$StandardEntity";
import { Company } from "./finance_Company";
import { Currency } from "./finance_Currency";
import { Bank } from "./finance_Bank";
export class Account extends StandardEntity {
  static NAME = "finance_Account";
  company?: Company | null;
  name?: string | null;
  currency?: Currency | null;
  iban?: string | null;
  bank?: Bank | null;
  startDate?: any | null;
  endDate?: any | null;
  lock?: boolean | null;
  close?: boolean | null;
}
export type AccountViewName =
  | "_base"
  | "_local"
  | "_minimal"
  | "account-all-property";
export type AccountView<V extends AccountViewName> = V extends "_base"
  ? Pick<
      Account,
      | "id"
      | "name"
      | "currency"
      | "iban"
      | "startDate"
      | "endDate"
      | "lock"
      | "close"
    >
  : V extends "_local"
  ? Pick<
      Account,
      "id" | "name" | "iban" | "startDate" | "endDate" | "lock" | "close"
    >
  : V extends "_minimal"
  ? Pick<Account, "id" | "name" | "currency">
  : V extends "account-all-property"
  ? Pick<
      Account,
      | "id"
      | "name"
      | "iban"
      | "startDate"
      | "endDate"
      | "lock"
      | "close"
      | "company"
      | "currency"
      | "bank"
    >
  : never;
