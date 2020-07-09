import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TableOfPrizesTestModule } from '../../../test.module';
import { ImageUpdateComponent } from 'app/entities/image/image-update.component';
import { ImageService } from 'app/entities/image/image.service';
import { Image } from 'app/shared/model/image.model';

describe('Component Tests', () => {
  describe('Image Management Update Component', () => {
    let comp: ImageUpdateComponent;
    let fixture: ComponentFixture<ImageUpdateComponent>;
    let service: ImageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TableOfPrizesTestModule],
        declarations: [ImageUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ImageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ImageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Image(123, '', '', '');
        const spy = spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.createViewFromModel(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalled();
        expect(JSON.stringify(spy.calls.mostRecent().args[0])).toBe(JSON.stringify(entity));
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Image(undefined, '', '', '');
        const spy = spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.createViewFromModel(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalled();
        expect(JSON.stringify(spy.calls.mostRecent().args[0])).toBe(JSON.stringify(entity, (_, v) => (v === undefined ? null : v)));
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
