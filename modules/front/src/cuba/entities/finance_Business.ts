import { StandardEntity } from "./base/sys$StandardEntity";
import { User } from "./base/sec$User";
export class Business extends StandardEntity {
  static NAME = "finance_Business";
  name?: string | null;
  finControler?: User | null;
  finDirector?: User | null;
  genDirector?: User | null;
  finControlerSH?: User | null;
  finDirectorSH?: User | null;
  usePaymantClaim?: boolean | null;
  usePaymentClaimApproval?: boolean | null;
}
export type BusinessViewName =
  | "_base"
  | "_local"
  | "_minimal"
  | "business.getEdit";
export type BusinessView<V extends BusinessViewName> = V extends "_base"
  ? Pick<
      Business,
      "id" | "name" | "usePaymantClaim" | "usePaymentClaimApproval"
    >
  : V extends "_local"
  ? Pick<
      Business,
      "id" | "name" | "usePaymantClaim" | "usePaymentClaimApproval"
    >
  : V extends "_minimal"
  ? Pick<Business, "id" | "name">
  : V extends "business.getEdit"
  ? Pick<
      Business,
      | "id"
      | "name"
      | "usePaymantClaim"
      | "usePaymentClaimApproval"
      | "finControler"
      | "finDirector"
      | "genDirector"
    >
  : never;
