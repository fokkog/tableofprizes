import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TableOfPrizesSharedModule } from 'app/shared/shared.module';
import { TableOfPrizesComponent } from './table-of-prizes.component';
import { TableOfPrizesDetailComponent } from './table-of-prizes-detail.component';
import { TableOfPrizesPublicComponent } from './table-of-prizes-public.component';
import { TableOfPrizesUpdateComponent } from './table-of-prizes-update.component';
import { TableOfPrizesDeleteDialogComponent } from './table-of-prizes-delete-dialog.component';
import { tableOfPrizesRoute } from './table-of-prizes.route';

@NgModule({
  imports: [TableOfPrizesSharedModule, RouterModule.forChild(tableOfPrizesRoute)],
  declarations: [
    TableOfPrizesComponent,
    TableOfPrizesDetailComponent,
    TableOfPrizesPublicComponent,
    TableOfPrizesUpdateComponent,
    TableOfPrizesDeleteDialogComponent,
  ],
  entryComponents: [TableOfPrizesDeleteDialogComponent],
})
export class TableOfPrizesTableOfPrizesModule {}
