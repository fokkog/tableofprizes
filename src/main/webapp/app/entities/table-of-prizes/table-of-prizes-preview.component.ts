import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ITableOfPrizes } from 'app/shared/model/table-of-prizes.model';

@Component({
  selector: 'jhi-table-of-prizes-preview',
  templateUrl: './table-of-prizes-preview.component.html',
})
export class TableOfPrizesPreviewComponent implements OnInit {
  tableOfPrizes: ITableOfPrizes | null = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.tableOfPrizes = history.state.tableOfPrizes;
    if (!this.tableOfPrizes) {
      this.router.navigate(['/']);
    }
  }

  previousState(): void {
    window.history.back();
  }
}
