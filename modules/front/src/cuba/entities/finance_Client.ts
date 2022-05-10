import { StandardEntity } from "./base/sys$StandardEntity";
import { ClientTypeEnum } from "../enums/enums";
export class Client extends StandardEntity {
  static NAME = "finance_Client";
  shortName?: string | null;
  name?: string | null;
  clientType?: ClientTypeEnum | null;
  edrpou?: string | null;
  address?: string | null;
  kved?: string | null;
  boss?: string | null;
  stan?: string | null;
}
export type ClientViewName = "_base" | "_local" | "_minimal";
export type ClientView<V extends ClientViewName> = V extends "_base"
  ? Pick<
      Client,
      | "id"
      | "shortName"
      | "edrpou"
      | "name"
      | "clientType"
      | "address"
      | "kved"
      | "boss"
      | "stan"
    >
  : V extends "_local"
  ? Pick<
      Client,
      | "id"
      | "shortName"
      | "name"
      | "clientType"
      | "edrpou"
      | "address"
      | "kved"
      | "boss"
      | "stan"
    >
  : V extends "_minimal"
  ? Pick<Client, "id" | "shortName" | "edrpou">
  : never;
