import { IEmployee } from 'app/shared/model/employee.model';

export interface ISite {
  id?: number;
  nameSite?: string;
  addressSite?: string;
  descSite?: string | null;
  employees?: IEmployee[] | null;
}

export const defaultValue: Readonly<ISite> = {};
