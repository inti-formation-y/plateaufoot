import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategorie } from 'app/shared/model/categorie.model';
import { CategorieService } from './categorie.service';
import { CategorieDeleteDialogComponent } from './categorie-delete-dialog.component';

@Component({
  selector: 'jhi-categorie',
  templateUrl: './categorie.component.html'
})
export class CategorieComponent implements OnInit, OnDestroy {
  categories?: ICategorie[];
  eventSubscriber?: Subscription;

  constructor(protected categorieService: CategorieService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.categorieService.query().subscribe((res: HttpResponse<ICategorie[]>) => (this.categories = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCategories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICategorie): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('categorieListModification', () => this.loadAll());
  }

  delete(categorie: ICategorie): void {
    const modalRef = this.modalService.open(CategorieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categorie = categorie;
  }
}
