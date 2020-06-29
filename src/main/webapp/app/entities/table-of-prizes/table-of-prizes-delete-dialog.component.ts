import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITableOfPrizes } from 'app/shared/model/table-of-prizes.model';
import { TableOfPrizesService } from './table-of-prizes.service';

@Component({
  templateUrl: './table-of-prizes-delete-dialog.component.html',
})
export class TableOfPrizesDeleteDialogComponent {
  tableOfPrizes?: ITableOfPrizes;

  constructor(
    protected tableOfPrizesService: TableOfPrizesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tableOfPrizesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('tableOfPrizesListModification');
      this.activeModal.close();
    });
  }
}
