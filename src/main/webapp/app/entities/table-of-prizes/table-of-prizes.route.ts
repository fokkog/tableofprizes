import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITableOfPrizes, TableOfPrizes } from 'app/shared/model/table-of-prizes.model';
import { TableOfPrizesService } from './table-of-prizes.service';
import { TableOfPrizesComponent } from './table-of-prizes.component';
import { TableOfPrizesDetailComponent } from './table-of-prizes-detail.component';
import { TableOfPrizesUpdateComponent } from './table-of-prizes-update.component';

@Injectable({ providedIn: 'root' })
export class TableOfPrizesResolve implements Resolve<ITableOfPrizes> {
  constructor(private service: TableOfPrizesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITableOfPrizes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((tableOfPrizes: HttpResponse<TableOfPrizes>) => {
          if (tableOfPrizes.body) {
            return of(tableOfPrizes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TableOfPrizes());
  }
}

export const tableOfPrizesRoute: Routes = [
  {
    path: '',
    component: TableOfPrizesComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'tableOfPrizesApp.tableOfPrizes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TableOfPrizesDetailComponent,
    resolve: {
      tableOfPrizes: TableOfPrizesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'tableOfPrizesApp.tableOfPrizes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TableOfPrizesUpdateComponent,
    resolve: {
      tableOfPrizes: TableOfPrizesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'tableOfPrizesApp.tableOfPrizes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TableOfPrizesUpdateComponent,
    resolve: {
      tableOfPrizes: TableOfPrizesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'tableOfPrizesApp.tableOfPrizes.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
