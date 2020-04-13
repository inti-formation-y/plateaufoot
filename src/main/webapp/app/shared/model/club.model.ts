import { IStade } from 'app/shared/model/stade.model';
import { ICategorie } from 'app/shared/model/categorie.model';

export interface IClub {
  id?: number;
  logoContentType?: string;
  logo?: any;
  nom?: string;
  adresse?: string;
  telephone?: string;
  email?: string;
  stades?: IStade[];
  categories?: ICategorie[];
}

export class Club implements IClub {
  constructor(
    public id?: number,
    public logoContentType?: string,
    public logo?: any,
    public nom?: string,
    public adresse?: string,
    public telephone?: string,
    public email?: string,
    public stades?: IStade[],
    public categories?: ICategorie[]
  ) {}
}
