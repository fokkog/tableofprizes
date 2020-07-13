import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IImage } from 'app/shared/model/image.model';
import { ImageService } from './image.service';

@Component({
  selector: 'jhi-image-update',
  templateUrl: './image-update.component.html',
})
export class ImageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({});

  constructor(protected imageService: ImageService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ image }) => {
      this.createViewFromModel(image);
    });
  }

  createViewFromModel(image: IImage): void {
    this.editForm = this.fb.group({
      id: [image.id],
      name: [image.name, [Validators.required, Validators.maxLength(100)]],
      url: [image.url, [Validators.required, Validators.maxLength(1000), Validators.pattern('^https://[^\\s]*$')]],
      userId: [image.userId],
    });
  }

  private createModelFromView(): IImage {
    // Form value has identical shape to model
    return this.editForm.value;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.editForm.markAllAsTouched();
    if (this.editForm.valid) {
      this.isSaving = true;
      const image = this.createModelFromView();
      if (image.id) {
        this.subscribeToSaveResponse(this.imageService.update(image));
      } else {
        this.subscribeToSaveResponse(this.imageService.create(image));
      }
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImage>>): void {
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
}
