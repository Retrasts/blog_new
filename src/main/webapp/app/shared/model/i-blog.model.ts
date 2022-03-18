export interface IIBlog {
  id?: number;
  createUserId?: number | null;
  title?: string | null;
  label?: number | null;
  classify?: number | null;
  content?: string | null;
  likes?: number | null;
  replynumber?: number | null;
  createTime?: Date | null;
  updateTime?: Date | null;
}

export class IBlog implements IIBlog {
  constructor(
    public id?: number,
    public createUserId?: number | null,
    public title?: string | null,
    public label?: number | null,
    public classify?: number | null,
    public content?: string | null,
    public likes?: number | null,
    public replynumber?: number | null,
    public createTime?: Date | null,
    public updateTime?: Date | null
  ) {}
}
