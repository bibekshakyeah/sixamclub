import { IContent } from 'app/shared/model/content.model';

export interface IContentMedia {
  id?: number;
  path?: string;
  type?: string;
  content?: IContent | null;
}

export const defaultValue: Readonly<IContentMedia> = {};
