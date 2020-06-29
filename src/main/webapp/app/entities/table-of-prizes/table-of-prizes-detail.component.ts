import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITableOfPrizes } from 'app/shared/model/table-of-prizes.model';

@Component({
  selector: 'jhi-table-of-prizes-detail',
  templateUrl: './table-of-prizes-detail.component.html',
})
export class TableOfPrizesDetailComponent implements OnInit {
  tableOfPrizes: ITableOfPrizes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tableOfPrizes }) => (this.tableOfPrizes = tableOfPrizes));
  }

  previousState(): void {
    window.history.back();
  }
}
