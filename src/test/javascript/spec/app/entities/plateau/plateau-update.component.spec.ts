import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PlateaufootTestModule } from '../../../test.module';
import { PlateauUpdateComponent } from 'app/entities/plateau/plateau-update.component';
import { PlateauService } from 'app/entities/plateau/plateau.service';
import { Plateau } from 'app/shared/model/plateau.model';

describe('Component Tests', () => {
  describe('Plateau Management Update Component', () => {
    let comp: PlateauUpdateComponent;
    let fixture: ComponentFixture<PlateauUpdateComponent>;
    let service: PlateauService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlateaufootTestModule],
        declarations: [PlateauUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlateauUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlateauUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlateauService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Plateau(123);
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
        const entity = new Plateau();
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
