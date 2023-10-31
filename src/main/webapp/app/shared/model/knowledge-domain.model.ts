import { IEmployee } from 'app/shared/model/employee.model';

export interface IKnowledgeDomain {
  id?: number;
  nameKnowledgeDomain?: string;
  descKnowledgeDomain?: string | null;
  employees?: IEmployee[] | null;
}

export const defaultValue: Readonly<IKnowledgeDomain> = {};
