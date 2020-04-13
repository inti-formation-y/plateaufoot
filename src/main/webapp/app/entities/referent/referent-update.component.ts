import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IReferent, Referent } from 'app/shared/model/referent.model';
import { ReferentService } from './referent.service';

@Component({
  selector: 'jhi-referent-update',
  templateUrl: './referent-update.component.html'
})
export class ReferentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    licence: [],
    telephone: [],
    email: []
  });

  constructor(protected referentService: ReferentService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ referent }) => {
      this.updateForm(referent);
    });
  }

  updateForm(referent: IReferent): void {
    this.editForm.patchValue({
      id: referent.id,
      nom: referent.nom,
      prenom: referent.prenom,
      licence: referent.licence,
      telephone: referent.telephone,
      email: referent.email
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const referent = this.createFromForm();
    if (referent.id !== undefined) {
      this.subscribeToSaveResponse(this.referentService.update(referent));
    } else {
      this.subscribeToSaveResponse(this.referentService.create(referent));
    }
  }

  private createFromForm(): IReferent {
    return {
      ...new Referent(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      licence: this.editForm.get(['licence'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      email: this.editForm.get(['email'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReferent>>): void {
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
}
