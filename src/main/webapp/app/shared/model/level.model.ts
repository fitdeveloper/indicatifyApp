import { LevelEnum } from 'app/shared/model/enumerations/level-enum.model';

export interface ILevel {
  id?: number;
  nameLevel?: string;
  valueLevel?: LevelEnum;
  descLevel?: string | null;
}

export const defaultValue: Readonly<ILevel> = {};
