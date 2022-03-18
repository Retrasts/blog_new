export interface IILabel {
  id?: number;
  name?: string | null;
  alias?: string | null;
  description?: string | null;
  parentId?: number | null;
}

export class ILabel implements IILabel {
  constructor(
    public id?: number,
    public name?: string | null,
    public alias?: string | null,
    public description?: string | null,
    public parentId?: number | null
  ) {}
}
