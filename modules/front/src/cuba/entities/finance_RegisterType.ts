import { StandardEntity } from "./base/sys$StandardEntity";
import { RegisterTypeDetail } from "./finance_RegisterTypeDetail";
export class RegisterType extends StandardEntity {
  static NAME = "finance_RegisterType";
  number?: number | null;
  name?: string | null;
  registerTypeDetails?: RegisterTypeDetail[] | null;
}
export type RegisterTypeViewName = "_base" | "_local" | "_minimal";
export type RegisterTypeView<V extends RegisterTypeViewName> = V extends "_base"
  ? Pick<RegisterType, "id" | "name" | "number">
  : V extends "_local"
  ? Pick<RegisterType, "id" | "number" | "name">
  : V extends "_minimal"
  ? Pick<RegisterType, "id" | "name">
  : never;
