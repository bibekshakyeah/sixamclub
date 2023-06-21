import { IOceanModel } from 'app/shared/model/ocean-model.model';

export interface IOceanFacet {
  id?: number;
  name?: string;
  value?: number;
  oceanModel?: IOceanModel | null;
}

export const defaultValue: Readonly<IOceanFacet> = {};
