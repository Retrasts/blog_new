export interface IIClassify {
  id?: number;
  name?: string | null;
  alias?: string | null;
  description?: string | null;
  parentId?: number | null;
}

export class IClassify implements IIClassify {
  constructor(
    public id?: number,
    public name?: string | null,
    public alias?: string | null,
    public description?: string | null,
    public parentId?: number | null
  ) {}
}
