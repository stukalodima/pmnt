import { StandardEntity } from "./base/sys$StandardEntity";
import { Account } from "./finance_Account";
export class AccountRemains extends StandardEntity {
  static NAME = "finance_AccountRemains";
  onDate?: any | null;
  account?: Account | null;
  summBefor?: any | null;
  summ?: any | null;
}
export type AccountRemainsViewName = "_base" | "_local" | "_minimal";
export type AccountRemainsView<
  V extends AccountRemainsViewName
> = V extends "_base"
  ? Pick<AccountRemains, "id" | "onDate" | "summBefor" | "summ">
  : V extends "_local"
  ? Pick<AccountRemains, "id" | "onDate" | "summBefor" | "summ">
  : never;
