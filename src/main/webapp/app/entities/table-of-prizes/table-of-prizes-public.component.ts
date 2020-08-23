import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITableOfPrizes } from 'app/shared/model/table-of-prizes.model';
import { TableOfPrizesService } from './table-of-prizes.service';

@Component({
  selector: 'jhi-table-of-prizes-public',
  templateUrl: './table-of-prizes-public.component.html',
})
export class TableOfPrizesPublicComponent implements OnInit {
  tableOfPrizes: ITableOfPrizes | null = null;

  constructor(private activatedRoute: ActivatedRoute, private service: TableOfPrizesService) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(() => {
      const id = this.activatedRoute.snapshot.params['id'];
      if (id) {
        this.service.view(id).subscribe(res => {
          this.tableOfPrizes = res.body;
        });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
