import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlateau } from 'app/shared/model/plateau.model';
import { PlateauService } from './plateau.service';

@Component({
  templateUrl: './plateau-delete-dialog.component.html'
})
export class PlateauDeleteDialogComponent {
  plateau?: IPlateau;

  constructor(protected plateauService: PlateauService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plateauService.delete(id).subscribe(() => {
      this.eventManager.broadcast('plateauListModification');
      this.activeModal.close();
    });
  }
}
