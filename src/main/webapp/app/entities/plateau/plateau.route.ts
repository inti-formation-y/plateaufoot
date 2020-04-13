import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPlateau, Plateau } from 'app/shared/model/plateau.model';
import { PlateauService } from './plateau.service';
import { PlateauComponent } from './plateau.component';
import { PlateauDetailComponent } from './plateau-detail.component';
import { PlateauUpdateComponent } from './plateau-update.component';

@Injectable({ providedIn: 'root' })
export class PlateauResolve implements Resolve<IPlateau> {
  constructor(private service: PlateauService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlateau> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((plateau: HttpResponse<Plateau>) => {
          if (plateau.body) {
            return of(plateau.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Plateau());
  }
}

export const plateauRoute: Routes = [
  {
    path: '',
    component: PlateauComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.plateau.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlateauDetailComponent,
    resolve: {
      plateau: PlateauResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.plateau.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlateauUpdateComponent,
    resolve: {
      plateau: PlateauResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.plateau.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlateauUpdateComponent,
    resolve: {
      plateau: PlateauResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.plateau.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
