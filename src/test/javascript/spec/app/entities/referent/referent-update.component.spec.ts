import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PlateaufootTestModule } from '../../../test.module';
import { ReferentUpdateComponent } from 'app/entities/referent/referent-update.component';
import { ReferentService } from 'app/entities/referent/referent.service';
import { Referent } from 'app/shared/model/referent.model';

describe('Component Tests', () => {
  describe('Referent Management Update Component', () => {
    let comp: ReferentUpdateComponent;
    let fixture: ComponentFixture<ReferentUpdateComponent>;
    let service: ReferentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlateaufootTestModule],
        declarations: [ReferentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ReferentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReferentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReferentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Referent(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Referent();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
