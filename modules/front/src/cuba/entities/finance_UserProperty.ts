import { StandardEntity } from "./base/sys$StandardEntity";
import { User } from "./base/sec$User";
import { ManagementCompany } from "./finance_ManagementCompany";
import { Business } from "./finance_Business";
import { Company } from "./finance_Company";
export class UserProperty extends StandardEntity {
  static NAME = "finance_UserProperty";
  user?: User | null;
  managementCompany?: ManagementCompany | null;
  business?: Business | null;
  company?: Company | null;
}
export type UserPropertyViewName =
  | "_base"
  | "_local"
  | "_minimal"
  | "userProperty.get.edit";
export type UserPropertyView<V extends UserPropertyViewName> = V extends "_base"
  ? Pick<UserProperty, "id" | "user">
  : V extends "_minimal"
  ? Pick<UserProperty, "id" | "user">
  : V extends "userProperty.get.edit"
  ? Pick<
      UserProperty,
      "id" | "user" | "business" | "company" | "managementCompany"
    >
  : never;
