import { StandardEntity } from "./base/sys$StandardEntity";
import { User } from "./base/sec$User";
export class ManagementCompany extends StandardEntity {
  static NAME = "finance_ManagementCompany";
  shortName?: string | null;
  name?: string | null;
  finControler?: User | null;
  finDirector?: User | null;
}
export type ManagementCompanyViewName = "_base" | "_local" | "_minimal";
export type ManagementCompanyView<
  V extends ManagementCompanyViewName
> = V extends "_base"
  ? Pick<ManagementCompany, "id" | "shortName" | "name">
  : V extends "_local"
  ? Pick<ManagementCompany, "id" | "shortName" | "name">
  : V extends "_minimal"
  ? Pick<ManagementCompany, "id" | "shortName">
  : never;
