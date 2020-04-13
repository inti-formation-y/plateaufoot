import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'club',
        loadChildren: () => import('./club/club.module').then(m => m.PlateaufootClubModule)
      },
      {
        path: 'stade',
        loadChildren: () => import('./stade/stade.module').then(m => m.PlateaufootStadeModule)
      },
      {
        path: 'categorie',
        loadChildren: () => import('./categorie/categorie.module').then(m => m.PlateaufootCategorieModule)
      },
      {
        path: 'referent',
        loadChildren: () => import('./referent/referent.module').then(m => m.PlateaufootReferentModule)
      },
      {
        path: 'plateau',
        loadChildren: () => import('./plateau/plateau.module').then(m => m.PlateaufootPlateauModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PlateaufootEntityModule {}
