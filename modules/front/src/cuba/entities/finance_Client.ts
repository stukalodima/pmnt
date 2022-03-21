import { StandardEntity } from "./base/sys$StandardEntity";
export class Client extends StandardEntity {
  static NAME = "finance_Client";
  name?: string | null;
}
export type ClientViewName = "_base" | "_local" | "_minimal";
export type ClientView<V extends ClientViewName> = V extends "_base"
  ? Pick<Client, "id" | "name">
  : V extends "_local"
  ? Pick<Client, "id" | "name">
  : V extends "_minimal"
  ? Pick<Client, "id" | "name">
  : never;
