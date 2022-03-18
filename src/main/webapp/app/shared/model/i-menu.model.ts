import { IIRoleMenu } from '@/shared/model/i-role-menu.model';

export interface IIMenu {
  id?: number;
  url?: string | null;
  menuName?: string | null;
  parentId?: number | null;
  createTime?: Date | null;
  updateTime?: Date | null;
  createUserId?: number | null;
  updateUserId?: number | null;
  roles?: IIRoleMenu[] | null;
}

export class IMenu implements IIMenu {
  constructor(
    public id?: number,
    public url?: string | null,
    public menuName?: string | null,
    public parentId?: number | null,
    public createTime?: Date | null,
    public updateTime?: Date | null,
    public createUserId?: number | null,
    public updateUserId?: number | null,
    public roles?: IIRoleMenu[] | null
  ) {}
}
