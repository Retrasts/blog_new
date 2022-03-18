import { IIUserRole } from '@/shared/model/i-user-role.model';
import { IIRoleMenu } from '@/shared/model/i-role-menu.model';

export interface IIRole {
  id?: number;
  roleName?: string | null;
  remark?: string | null;
  createTime?: Date | null;
  updateTime?: Date | null;
  createUserId?: number | null;
  updateUserId?: number | null;
  users?: IIUserRole | null;
  menus?: IIRoleMenu | null;
}

export class IRole implements IIRole {
  constructor(
    public id?: number,
    public roleName?: string | null,
    public remark?: string | null,
    public createTime?: Date | null,
    public updateTime?: Date | null,
    public createUserId?: number | null,
    public updateUserId?: number | null,
    public users?: IIUserRole | null,
    public menus?: IIRoleMenu | null
  ) {}
}
