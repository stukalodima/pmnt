import { StandardEntity } from "./base/sys$StandardEntity";
import { Business } from "./finance_Business";
export class Company extends StandardEntity {
  static NAME = "finance_Company";
  shortName?: string | null;
  name?: string | null;
  edrpou?: string | null;
  business?: Business | null;
}
export type CompanyViewName =
  | "_base"
  | "_local"
  | "_minimal"
  | "company-all-property";
export type CompanyView<V extends CompanyViewName> = V extends "_base"
  ? Pick<Company, "id" | "shortName" | "name" | "edrpou">
  : V extends "_local"
  ? Pick<Company, "id" | "shortName" | "name" | "edrpou">
  : V extends "_minimal"
  ? Pick<Company, "id" | "shortName">
  : V extends "company-all-property"
  ? Pick<Company, "id" | "shortName" | "name" | "edrpou" | "business">
  : never;
