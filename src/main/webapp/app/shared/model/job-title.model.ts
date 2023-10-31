export interface IJobTitle {
  id?: number;
  nameJobTitle?: string;
  descJobTitle?: string | null;
}

export const defaultValue: Readonly<IJobTitle> = {};
