import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlateau } from 'app/shared/model/plateau.model';

@Component({
  selector: 'jhi-plateau-detail',
  templateUrl: './plateau-detail.component.html'
})
export class PlateauDetailComponent implements OnInit {
  plateau: IPlateau | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plateau }) => (this.plateau = plateau));
  }

  previousState(): void {
    window.history.back();
  }
}
