import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReferent } from 'app/shared/model/referent.model';
import { ReferentService } from './referent.service';
import { ReferentDeleteDialogComponent } from './referent-delete-dialog.component';

@Component({
  selector: 'jhi-referent',
  templateUrl: './referent.component.html'
})
export class ReferentComponent implements OnInit, OnDestroy {
  referents?: IReferent[];
  eventSubscriber?: Subscription;

  constructor(protected referentService: ReferentService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.referentService.query().subscribe((res: HttpResponse<IReferent[]>) => (this.referents = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInReferents();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IReferent): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInReferents(): void {
    this.eventSubscriber = this.eventManager.subscribe('referentListModification', () => this.loadAll());
  }

  delete(referent: IReferent): void {
    const modalRef = this.modalService.open(ReferentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.referent = referent;
  }
}
