import { Stencil } from "./bpm$Stencil";
export class GroupStencil extends Stencil {
  static NAME = "bpm$GroupStencil";
}
export type GroupStencilViewName = "_base" | "_local" | "_minimal";
export type GroupStencilView<V extends GroupStencilViewName> = V extends "_base"
  ? Pick<GroupStencil, "id" | "title">
  : V extends "_minimal"
  ? Pick<GroupStencil, "id" | "title">
  : never;
