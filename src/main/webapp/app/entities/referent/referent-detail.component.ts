import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReferent } from 'app/shared/model/referent.model';

@Component({
  selector: 'jhi-referent-detail',
  templateUrl: './referent-detail.component.html'
})
export class ReferentDetailComponent implements OnInit {
  referent: IReferent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ referent }) => (this.referent = referent));
  }

  previousState(): void {
    window.history.back();
  }
}
