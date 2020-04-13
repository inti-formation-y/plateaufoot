import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PlateaufootTestModule } from '../../../test.module';
import { ClubDetailComponent } from 'app/entities/club/club-detail.component';
import { Club } from 'app/shared/model/club.model';

describe('Component Tests', () => {
  describe('Club Management Detail Component', () => {
    let comp: ClubDetailComponent;
    let fixture: ComponentFixture<ClubDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ club: new Club(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlateaufootTestModule],
        declarations: [ClubDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ClubDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClubDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load club on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.club).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
