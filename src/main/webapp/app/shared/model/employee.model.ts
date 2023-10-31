import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IJobTitle } from 'app/shared/model/job-title.model';
import { ILevel } from 'app/shared/model/level.model';
import { IKnowledgeDomain } from 'app/shared/model/knowledge-domain.model';
import { IPerimeter } from 'app/shared/model/perimeter.model';
import { ISite } from 'app/shared/model/site.model';
import { GenderEnum } from 'app/shared/model/enumerations/gender-enum.model';

export interface IEmployee {
  id?: number;
  firstnameEmployee?: string;
  lastnameEmployee?: string;
  matriculationNumberEmployee?: string;
  dateOfBirthEmployee?: string;
  emailEmployee?: string;
  phoneEmployee?: string | null;
  hireDateEmployee?: string | null;
  genderEmployee?: GenderEnum;
  descEmployee?: string | null;
  user?: IUser;
  superiorEmployee?: IEmployee | null;
  jobTitle?: IJobTitle;
  level?: ILevel;
  knowledgeDomains?: IKnowledgeDomain[] | null;
  perimeters?: IPerimeter[] | null;
  site?: ISite;
  subordinateEmployees?: IEmployee[] | null;
}

export const defaultValue: Readonly<IEmployee> = {};
