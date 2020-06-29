import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITableOfPrizes } from 'app/shared/model/table-of-prizes.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TableOfPrizesService } from './table-of-prizes.service';
import { TableOfPrizesDeleteDialogComponent } from './table-of-prizes-delete-dialog.component';

@Component({
  selector: 'jhi-table-of-prizes',
  templateUrl: './table-of-prizes.component.html',
})
export class TableOfPrizesComponent implements OnInit, OnDestroy {
  tableOfPrizes?: ITableOfPrizes[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected tableOfPrizesService: TableOfPrizesService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.tableOfPrizesService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ITableOfPrizes[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.handleBackNavigation();
    this.registerChangeInTableOfPrizes();
  }

  handleBackNavigation(): void {
    this.activatedRoute.queryParamMap.subscribe((params: ParamMap) => {
      const prevPage = params.get('page');
      const prevSort = params.get('sort');
      const prevSortSplit = prevSort?.split(',');
      if (prevSortSplit) {
        this.predicate = prevSortSplit[0];
        this.ascending = prevSortSplit[1] === 'asc';
      }
      if (prevPage && +prevPage !== this.page) {
        this.ngbPaginationPage = +prevPage;
        this.loadPage(+prevPage);
      } else {
        this.loadPage(this.page);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITableOfPrizes): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTableOfPrizes(): void {
    this.eventSubscriber = this.eventManager.subscribe('tableOfPrizesListModification', () => this.loadPage());
  }

  delete(tableOfPrizes: ITableOfPrizes): void {
    const modalRef = this.modalService.open(TableOfPrizesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tableOfPrizes = tableOfPrizes;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ITableOfPrizes[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/table-of-prizes'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
      },
    });
    this.tableOfPrizes = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
