import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IReferent } from 'app/shared/model/referent.model';

type EntityResponseType = HttpResponse<IReferent>;
type EntityArrayResponseType = HttpResponse<IReferent[]>;

@Injectable({ providedIn: 'root' })
export class ReferentService {
  public resourceUrl = SERVER_API_URL + 'api/referents';

  constructor(protected http: HttpClient) {}

  create(referent: IReferent): Observable<EntityResponseType> {
    return this.http.post<IReferent>(this.resourceUrl, referent, { observe: 'response' });
  }

  update(referent: IReferent): Observable<EntityResponseType> {
    return this.http.put<IReferent>(this.resourceUrl, referent, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReferent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReferent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
