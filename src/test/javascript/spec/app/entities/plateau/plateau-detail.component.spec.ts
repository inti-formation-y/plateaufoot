import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlateaufootTestModule } from '../../../test.module';
import { PlateauDetailComponent } from 'app/entities/plateau/plateau-detail.component';
import { Plateau } from 'app/shared/model/plateau.model';

describe('Component Tests', () => {
  describe('Plateau Management Detail Component', () => {
    let comp: PlateauDetailComponent;
    let fixture: ComponentFixture<PlateauDetailComponent>;
    const route = ({ data: of({ plateau: new Plateau(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlateaufootTestModule],
        declarations: [PlateauDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlateauDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlateauDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load plateau on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.plateau).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
