import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';

@Component({
  templateUrl: './club-delete-dialog.component.html'
})
export class ClubDeleteDialogComponent {
  club?: IClub;

  constructor(protected clubService: ClubService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clubService.delete(id).subscribe(() => {
      this.eventManager.broadcast('clubListModification');
      this.activeModal.close();
    });
  }
}
