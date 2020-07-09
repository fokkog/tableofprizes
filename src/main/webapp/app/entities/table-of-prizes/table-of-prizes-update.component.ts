import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators, FormArray, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITableOfPrizes, TableOfPrizes } from 'app/shared/model/table-of-prizes.model';
import { TableOfPrizesService } from './table-of-prizes.service';
import { IPrize, Prize } from 'app/shared/model/prize.model';
import { ImageService } from '../image/image.service';
import { IImage } from 'app/shared/model/image.model';

@Component({
  selector: 'jhi-table-of-prizes-update',
  templateUrl: './table-of-prizes-update.component.html',
})
export class TableOfPrizesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({});

  images: IImage[] = [];

  constructor(
    protected tableOfPrizesService: TableOfPrizesService,
    protected imageService: ImageService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tableOfPrizes }) => {
      this.createViewFromModel(tableOfPrizes);
    });
    this.imageService.query().subscribe((res: HttpResponse<IImage[]>) => (this.images = res.body || []));
  }

  createViewFromModel(tableOfPrizes: ITableOfPrizes): void {
    const rows = this.fb.array([]);
    if (tableOfPrizes.prizes) {
      for (const prize of tableOfPrizes.prizes) {
        rows.push(this.createViewRowFromModelChild(prize));
      }
    }
    this.editForm = this.fb.group({
      id: [tableOfPrizes.id],
      name: [tableOfPrizes.name, [Validators.required, Validators.maxLength(100)]],
      userId: [tableOfPrizes.userId],
      prizes: rows,
    });
  }

  private createViewRowFromModelChild(prize: IPrize): FormGroup {
    return this.fb.group({
      id: [prize.id],
      quantity: [prize.quantity, [Validators.required, Validators.min(1)]],
      imageId: [prize.image?.id, [Validators.required]],
    });
  }

  getViewRows(): FormArray {
    return this.editForm.get('prizes') as FormArray;
  }

  addViewRow(): void {
    this.getViewRows().push(this.createViewRowFromModelChild(new Prize()));
  }

  deleteViewRow(index: number): void {
    this.getViewRows().removeAt(index);
  }

  private createModelFromView(): ITableOfPrizes {
    const prizes: IPrize[] = [];
    if (this.getViewRows()) {
      for (const row of this.getViewRows().controls) {
        prizes.push({
          ...new Prize(),
          id: row.get('id')!.value,
          quantity: row.get('quantity')!.value,
          image: this.findImageById(row.get('imageId')!.value),
        });
      }
    }
    return {
      ...new TableOfPrizes(),
      id: this.editForm.get('id')!.value,
      name: this.editForm.get('name')!.value,
      userId: this.editForm.get('userId')!.value,
      prizes /* property shorthand */,
    };
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.editForm.markAllAsTouched();
    if (this.editForm.valid) {
      this.isSaving = true;
      const tableOfPrizes = this.createModelFromView();
      if (tableOfPrizes.id) {
        this.subscribeToSaveResponse(this.tableOfPrizesService.update(tableOfPrizes));
      } else {
        this.subscribeToSaveResponse(this.tableOfPrizesService.create(tableOfPrizes));
      }
    }
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

  trackId(index: number, item: ITableOfPrizes): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  findImageById(id: number): IImage {
    return this.images.find(i => i.id === id)!;
  }

  findImageUrlById(id: number): string | null {
    const image = this.findImageById(id);
    if (image && image.url) return image.url;
    return null;
  }
}
