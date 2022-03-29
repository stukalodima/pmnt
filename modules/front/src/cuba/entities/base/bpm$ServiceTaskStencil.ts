import { Stencil } from "./bpm$Stencil";
import { FileDescriptor } from "./sys$FileDescriptor";
import { StencilMethodArg } from "./bpm$StencilMethodArg";
export class ServiceTaskStencil extends Stencil {
  static NAME = "bpm$ServiceTaskStencil";
  beanName?: string | null;
  methodName?: string | null;
  iconFileId?: any | null;
  iconFile?: FileDescriptor | null;
  methodArgs?: StencilMethodArg[] | null;
}
export type ServiceTaskStencilViewName = "_base" | "_local" | "_minimal";
export type ServiceTaskStencilView<
  V extends ServiceTaskStencilViewName
> = V extends "_base"
  ? Pick<ServiceTaskStencil, "id" | "title">
  : V extends "_minimal"
  ? Pick<ServiceTaskStencil, "id" | "title">
  : never;
