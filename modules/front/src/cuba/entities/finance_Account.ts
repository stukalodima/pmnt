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
}
export type AccountViewName = "_base" | "_local" | "_minimal";
export type AccountView<V extends AccountViewName> = V extends "_base"
  ? Pick<Account, "id" | "name" | "currency" | "iban">
  : V extends "_local"
  ? Pick<Account, "id" | "name" | "iban">
  : V extends "_minimal"
  ? Pick<Account, "id" | "name" | "currency">
  : never;
