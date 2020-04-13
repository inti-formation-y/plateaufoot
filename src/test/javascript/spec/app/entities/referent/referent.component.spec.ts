import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PlateaufootTestModule } from '../../../test.module';
import { ReferentComponent } from 'app/entities/referent/referent.component';
import { ReferentService } from 'app/entities/referent/referent.service';
import { Referent } from 'app/shared/model/referent.model';

describe('Component Tests', () => {
  describe('Referent Management Component', () => {
    let comp: ReferentComponent;
    let fixture: ComponentFixture<ReferentComponent>;
    let service: ReferentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlateaufootTestModule],
        declarations: [ReferentComponent]
      })
        .overrideTemplate(ReferentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReferentComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReferentService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Referent(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.referents && comp.referents[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
