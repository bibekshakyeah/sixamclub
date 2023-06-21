import dayjs from 'dayjs';
import { IContentMedia } from 'app/shared/model/content-media.model';
import { IContentType } from 'app/shared/model/content-type.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IContent {
  id?: number;
  title?: string;
  sourceUrl?: string | null;
  videoUrl?: string | null;
  contentText?: string | null;
  publishedDate?: string | null;
  contentMedias?: IContentMedia[] | null;
  contentType?: IContentType | null;
  categories?: ICategory[] | null;
}

export const defaultValue: Readonly<IContent> = {};
