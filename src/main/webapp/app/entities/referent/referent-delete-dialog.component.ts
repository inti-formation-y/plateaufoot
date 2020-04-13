import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReferent } from 'app/shared/model/referent.model';
import { ReferentService } from './referent.service';

@Component({
  templateUrl: './referent-delete-dialog.component.html'
})
export class ReferentDeleteDialogComponent {
  referent?: IReferent;

  constructor(protected referentService: ReferentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.referentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('referentListModification');
      this.activeModal.close();
    });
  }
}
