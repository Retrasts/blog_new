export interface IIComment {
  id?: number;
  createTime?: Date | null;
  createUserId?: number | null;
  blogId?: number | null;
  content?: string | null;
  likes?: number | null;
  parentId?: number | null;
}

export class IComment implements IIComment {
  constructor(
    public id?: number,
    public createTime?: Date | null,
    public createUserId?: number | null,
    public blogId?: number | null,
    public content?: string | null,
    public likes?: number | null,
    public parentId?: number | null
  ) {}
}
