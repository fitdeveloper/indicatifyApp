import { IPerimeter } from 'app/shared/model/perimeter.model';

export interface IActivity {
  id?: number;
  nameActivity?: string;
  descActivity?: string | null;
  perimeters?: IPerimeter[] | null;
}

export const defaultValue: Readonly<IActivity> = {};
