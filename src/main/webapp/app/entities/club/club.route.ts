import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClub, Club } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { ClubComponent } from './club.component';
import { ClubDetailComponent } from './club-detail.component';
import { ClubUpdateComponent } from './club-update.component';

@Injectable({ providedIn: 'root' })
export class ClubResolve implements Resolve<IClub> {
  constructor(private service: ClubService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClub> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((club: HttpResponse<Club>) => {
          if (club.body) {
            return of(club.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Club());
  }
}

export const clubRoute: Routes = [
  {
    path: '',
    component: ClubComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.club.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClubDetailComponent,
    resolve: {
      club: ClubResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.club.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClubUpdateComponent,
    resolve: {
      club: ClubResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.club.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClubUpdateComponent,
    resolve: {
      club: ClubResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateaufootApp.club.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
