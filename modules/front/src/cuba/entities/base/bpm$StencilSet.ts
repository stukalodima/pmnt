import { StandardEntity } from "./sys$StandardEntity";
export class StencilSet extends StandardEntity {
  static NAME = "bpm$StencilSet";
  name?: string | null;
  jsonData?: string | null;
}
export type StencilSetViewName = "_base" | "_local" | "_minimal";
export type StencilSetView<V extends StencilSetViewName> = V extends "_base"
  ? Pick<StencilSet, "id" | "name" | "jsonData">
  : V extends "_local"
  ? Pick<StencilSet, "id" | "name" | "jsonData">
  : never;
