import { IActivity } from 'app/shared/model/activity.model';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IPerimeter {
  id?: number;
  namePerimeter?: string;
  descPerimeter?: string | null;
  activity?: IActivity;
  employees?: IEmployee[] | null;
}

export const defaultValue: Readonly<IPerimeter> = {};
