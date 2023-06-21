import { IContent } from 'app/shared/model/content.model';

export interface ICategory {
  id?: number;
  name?: string;
  contents?: IContent[] | null;
}

export const defaultValue: Readonly<ICategory> = {};
