import { Moment } from 'moment';
import { IReferent } from 'app/shared/model/referent.model';

export interface IPlateau {
  id?: number;
  dateDebut?: Moment;
  dateFin?: Moment;
  heureDebut?: string;
  heureFin?: string;
  adresse?: string;
  nbrEquipe?: number;
  referent?: IReferent;
}

export class Plateau implements IPlateau {
  constructor(
    public id?: number,
    public dateDebut?: Moment,
    public dateFin?: Moment,
    public heureDebut?: string,
    public heureFin?: string,
    public adresse?: string,
    public nbrEquipe?: number,
    public referent?: IReferent
  ) {}
}
