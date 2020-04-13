import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPlateau, Plateau } from 'app/shared/model/plateau.model';
import { PlateauService } from './plateau.service';
import { IReferent } from 'app/shared/model/referent.model';
import { ReferentService } from 'app/entities/referent/referent.service';

@Component({
  selector: 'jhi-plateau-update',
  templateUrl: './plateau-update.component.html'
})
export class PlateauUpdateComponent implements OnInit {
  isSaving = false;
  referents: IReferent[] = [];
  dateDebutDp: any;
  dateFinDp: any;

  editForm = this.fb.group({
    id: [],
    dateDebut: [],
    dateFin: [],
    heureDebut: [],
    heureFin: [],
    adresse: [],
    nbrEquipe: [],
    referent: []
  });

  constructor(
    protected plateauService: PlateauService,
    protected referentService: ReferentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plateau }) => {
      this.updateForm(plateau);

      this.referentService.query().subscribe((res: HttpResponse<IReferent[]>) => (this.referents = res.body || []));
    });
  }

  updateForm(plateau: IPlateau): void {
    this.editForm.patchValue({
      id: plateau.id,
      dateDebut: plateau.dateDebut,
      dateFin: plateau.dateFin,
      heureDebut: plateau.heureDebut,
      heureFin: plateau.heureFin,
      adresse: plateau.adresse,
      nbrEquipe: plateau.nbrEquipe,
      referent: plateau.referent
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plateau = this.createFromForm();
    if (plateau.id !== undefined) {
      this.subscribeToSaveResponse(this.plateauService.update(plateau));
    } else {
      this.subscribeToSaveResponse(this.plateauService.create(plateau));
    }
  }

  private createFromForm(): IPlateau {
    return {
      ...new Plateau(),
      id: this.editForm.get(['id'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
      heureDebut: this.editForm.get(['heureDebut'])!.value,
      heureFin: this.editForm.get(['heureFin'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      nbrEquipe: this.editForm.get(['nbrEquipe'])!.value,
      referent: this.editForm.get(['referent'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlateau>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IReferent): any {
    return item.id;
  }
}
