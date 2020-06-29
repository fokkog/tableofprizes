import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TableOfPrizesTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { TableOfPrizesDeleteDialogComponent } from 'app/entities/table-of-prizes/table-of-prizes-delete-dialog.component';
import { TableOfPrizesService } from 'app/entities/table-of-prizes/table-of-prizes.service';

describe('Component Tests', () => {
  describe('TableOfPrizes Management Delete Component', () => {
    let comp: TableOfPrizesDeleteDialogComponent;
    let fixture: ComponentFixture<TableOfPrizesDeleteDialogComponent>;
    let service: TableOfPrizesService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TableOfPrizesTestModule],
        declarations: [TableOfPrizesDeleteDialogComponent],
      })
        .overrideTemplate(TableOfPrizesDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TableOfPrizesDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TableOfPrizesService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
