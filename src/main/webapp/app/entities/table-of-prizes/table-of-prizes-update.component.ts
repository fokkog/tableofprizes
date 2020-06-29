import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITableOfPrizes, TableOfPrizes } from 'app/shared/model/table-of-prizes.model';
import { TableOfPrizesService } from './table-of-prizes.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-table-of-prizes-update',
  templateUrl: './table-of-prizes-update.component.html',
})
export class TableOfPrizesUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    user: [null, Validators.required],
  });

  constructor(
    protected tableOfPrizesService: TableOfPrizesService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tableOfPrizes }) => {
      this.updateForm(tableOfPrizes);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(tableOfPrizes: ITableOfPrizes): void {
    this.editForm.patchValue({
      id: tableOfPrizes.id,
      name: tableOfPrizes.name,
      user: tableOfPrizes.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tableOfPrizes = this.createFromForm();
    if (tableOfPrizes.id !== undefined) {
      this.subscribeToSaveResponse(this.tableOfPrizesService.update(tableOfPrizes));
    } else {
      this.subscribeToSaveResponse(this.tableOfPrizesService.create(tableOfPrizes));
    }
  }

  private createFromForm(): ITableOfPrizes {
    return {
      ...new TableOfPrizes(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITableOfPrizes>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
