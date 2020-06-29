import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITableOfPrizes } from 'app/shared/model/table-of-prizes.model';

type EntityResponseType = HttpResponse<ITableOfPrizes>;
type EntityArrayResponseType = HttpResponse<ITableOfPrizes[]>;

@Injectable({ providedIn: 'root' })
export class TableOfPrizesService {
  public resourceUrl = SERVER_API_URL + 'api/table-of-prizes';

  constructor(protected http: HttpClient) {}

  create(tableOfPrizes: ITableOfPrizes): Observable<EntityResponseType> {
    return this.http.post<ITableOfPrizes>(this.resourceUrl, tableOfPrizes, { observe: 'response' });
  }

  update(tableOfPrizes: ITableOfPrizes): Observable<EntityResponseType> {
    return this.http.put<ITableOfPrizes>(this.resourceUrl, tableOfPrizes, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITableOfPrizes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITableOfPrizes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
