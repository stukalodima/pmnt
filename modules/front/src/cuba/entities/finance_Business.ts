import { StandardEntity } from "./base/sys$StandardEntity";
export class Business extends StandardEntity {
  static NAME = "finance_Business";
  name?: string | null;
}
export type BusinessViewName = "_base" | "_local" | "_minimal";
export type BusinessView<V extends BusinessViewName> = V extends "_base"
  ? Pick<Business, "id" | "name">
  : V extends "_local"
  ? Pick<Business, "id" | "name">
  : V extends "_minimal"
  ? Pick<Business, "id" | "name">
  : never;
