import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'image',
        loadChildren: () => import('./image/image.module').then(m => m.TableOfPrizesImageModule),
      },
      {
        path: 'table-of-prizes',
        loadChildren: () => import('./table-of-prizes/table-of-prizes.module').then(m => m.TableOfPrizesTableOfPrizesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class TableOfPrizesEntityModule {}
