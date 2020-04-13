import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlateau } from 'app/shared/model/plateau.model';

type EntityResponseType = HttpResponse<IPlateau>;
type EntityArrayResponseType = HttpResponse<IPlateau[]>;

@Injectable({ providedIn: 'root' })
export class PlateauService {
  public resourceUrl = SERVER_API_URL + 'api/plateaus';

  constructor(protected http: HttpClient) {}

  create(plateau: IPlateau): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plateau);
    return this.http
      .post<IPlateau>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(plateau: IPlateau): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plateau);
    return this.http
      .put<IPlateau>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlateau>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlateau[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(plateau: IPlateau): IPlateau {
    const copy: IPlateau = Object.assign({}, plateau, {
      dateDebut: plateau.dateDebut && plateau.dateDebut.isValid() ? plateau.dateDebut.format(DATE_FORMAT) : undefined,
      dateFin: plateau.dateFin && plateau.dateFin.isValid() ? plateau.dateFin.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? moment(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? moment(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((plateau: IPlateau) => {
        plateau.dateDebut = plateau.dateDebut ? moment(plateau.dateDebut) : undefined;
        plateau.dateFin = plateau.dateFin ? moment(plateau.dateFin) : undefined;
      });
    }
    return res;
  }
}
