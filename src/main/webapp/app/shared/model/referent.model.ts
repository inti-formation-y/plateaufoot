export interface IReferent {
  id?: number;
  nom?: string;
  prenom?: string;
  licence?: string;
  telephone?: string;
  email?: string;
}

export class Referent implements IReferent {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public licence?: string,
    public telephone?: string,
    public email?: string
  ) {}
}
