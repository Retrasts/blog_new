import { IIUserRole } from '@/shared/model/i-user-role.model';

export interface IIUser {
  id?: number;
  ip?: string | null;
  username?: string | null;
  nikename?: string | null;
  password?: string | null;
  sex?: number | null;
  emaile?: string | null;
  avatar?: string | null;
  createTime?: Date | null;
  updateTime?: Date | null;
  createUserId?: number | null;
  updateUserId?: number | null;
  birthday?: Date | null;
  company?: string | null;
  phone?: number | null;
  roles?: IIUserRole[] | null;
}

export class IUser implements IIUser {
  constructor(
    public id?: number,
    public ip?: string | null,
    public username?: string | null,
    public nikename?: string | null,
    public password?: string | null,
    public sex?: number | null,
    public emaile?: string | null,
    public avatar?: string | null,
    public createTime?: Date | null,
    public updateTime?: Date | null,
    public createUserId?: number | null,
    public updateUserId?: number | null,
    public birthday?: Date | null,
    public company?: string | null,
    public phone?: number | null,
    public roles?: IIUserRole[] | null
  ) {}
}
