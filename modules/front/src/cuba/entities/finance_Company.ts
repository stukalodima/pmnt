import { StandardEntity } from "./base/sys$StandardEntity";
import { Business } from "./finance_Business";
export class Company extends StandardEntity {
  static NAME = "finance_Company";
  name?: string | null;
  business?: Business | null;
}
export type CompanyViewName = "_base" | "_local" | "_minimal";
export type CompanyView<V extends CompanyViewName> = V extends "_base"
  ? Pick<Company, "id" | "name">
  : V extends "_local"
  ? Pick<Company, "id" | "name">
  : V extends "_minimal"
  ? Pick<Company, "id" | "name">
  : never;
