import { IIRole } from '@/shared/model/i-role.model';
import { IIMenu } from '@/shared/model/i-menu.model';

export interface IIRoleMenu {
  id?: number;
  role?: IIRole | null;
  menu?: IIMenu | null;
}

export class IRoleMenu implements IIRoleMenu {
  constructor(public id?: number, public role?: IIRole | null, public menu?: IIMenu | null) {}
}
