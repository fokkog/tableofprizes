import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TableOfPrizesTestModule } from '../../../test.module';
import { TableOfPrizesUpdateComponent } from 'app/entities/table-of-prizes/table-of-prizes-update.component';
import { TableOfPrizesService } from 'app/entities/table-of-prizes/table-of-prizes.service';
import { TableOfPrizes } from 'app/shared/model/table-of-prizes.model';

describe('Component Tests', () => {
  describe('TableOfPrizes Management Update Component', () => {
    let comp: TableOfPrizesUpdateComponent;
    let fixture: ComponentFixture<TableOfPrizesUpdateComponent>;
    let service: TableOfPrizesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TableOfPrizesTestModule],
        declarations: [TableOfPrizesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TableOfPrizesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TableOfPrizesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TableOfPrizesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TableOfPrizes(123);
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
        const entity = new TableOfPrizes();
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
