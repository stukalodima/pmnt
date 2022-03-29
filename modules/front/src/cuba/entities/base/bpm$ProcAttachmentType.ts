import { StandardEntity } from "./sys$StandardEntity";
export class ProcAttachmentType extends StandardEntity {
  static NAME = "bpm$ProcAttachmentType";
  name?: string | null;
  code?: string | null;
}
export type ProcAttachmentTypeViewName = "_base" | "_local" | "_minimal";
export type ProcAttachmentTypeView<
  V extends ProcAttachmentTypeViewName
> = V extends "_base"
  ? Pick<ProcAttachmentType, "id" | "name" | "code">
  : V extends "_local"
  ? Pick<ProcAttachmentType, "id" | "name" | "code">
  : V extends "_minimal"
  ? Pick<ProcAttachmentType, "id" | "name">
  : never;
