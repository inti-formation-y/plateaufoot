import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlateaufootTestModule } from '../../../test.module';
import { ReferentDetailComponent } from 'app/entities/referent/referent-detail.component';
import { Referent } from 'app/shared/model/referent.model';

describe('Component Tests', () => {
  describe('Referent Management Detail Component', () => {
    let comp: ReferentDetailComponent;
    let fixture: ComponentFixture<ReferentDetailComponent>;
    const route = ({ data: of({ referent: new Referent(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlateaufootTestModule],
        declarations: [ReferentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ReferentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReferentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load referent on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.referent).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
