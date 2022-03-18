import { IIRole } from '@/shared/model/i-role.model';
import { IIUser } from '@/shared/model/i-user.model';

export interface IIUserRole {
  id?: number;
  role?: IIRole | null;
  user?: IIUser | null;
}

export class IUserRole implements IIUserRole {
  constructor(public id?: number, public role?: IIRole | null, public user?: IIUser | null) {}
}
