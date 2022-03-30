import { StandardEntity } from "./base/sys$StandardEntity";
import { User } from "./base/sec$User";
import { ManagementCompany } from "./finance_ManagementCompany";
export class Business extends StandardEntity {
  static NAME = "finance_Business";
  name?: string | null;
  finDirector?: User | null;
  managementCompany?: ManagementCompany | null;
}
export type BusinessViewName =
  | "_base"
  | "_local"
  | "_minimal"
  | "business-all-property";
export type BusinessView<V extends BusinessViewName> = V extends "_base"
  ? Pick<Business, "id" | "name">
  : V extends "_local"
  ? Pick<Business, "id" | "name">
  : V extends "_minimal"
  ? Pick<Business, "id" | "name">
  : V extends "business-all-property"
  ? Pick<Business, "id" | "name" | "finDirector" | "managementCompany">
  : never;
