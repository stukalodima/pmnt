import { StandardEntity } from "./sys$StandardEntity";
export class ProcModel extends StandardEntity {
  static NAME = "bpm$ProcModel";
  name?: string | null;
  actModelId?: string | null;
  description?: string | null;
}
export type ProcModelViewName = "_base" | "_local" | "_minimal";
export type ProcModelView<V extends ProcModelViewName> = V extends "_base"
  ? Pick<ProcModel, "id" | "name" | "actModelId" | "description">
  : V extends "_local"
  ? Pick<ProcModel, "id" | "name" | "actModelId" | "description">
  : V extends "_minimal"
  ? Pick<ProcModel, "id" | "name">
  : never;
