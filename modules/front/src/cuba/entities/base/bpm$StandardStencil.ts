import { Stencil } from "./bpm$Stencil";
export class StandardStencil extends Stencil {
  static NAME = "bpm$StandardStencil";
}
export type StandardStencilViewName = "_base" | "_local" | "_minimal";
export type StandardStencilView<
  V extends StandardStencilViewName
> = V extends "_base"
  ? Pick<StandardStencil, "id" | "title">
  : V extends "_minimal"
  ? Pick<StandardStencil, "id" | "title">
  : never;
