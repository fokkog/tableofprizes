import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TableOfPrizesTestModule } from '../../../test.module';
import { TableOfPrizesDetailComponent } from 'app/entities/table-of-prizes/table-of-prizes-detail.component';
import { TableOfPrizes } from 'app/shared/model/table-of-prizes.model';

describe('Component Tests', () => {
  describe('TableOfPrizes Management Detail Component', () => {
    let comp: TableOfPrizesDetailComponent;
    let fixture: ComponentFixture<TableOfPrizesDetailComponent>;
    const route = ({ data: of({ tableOfPrizes: new TableOfPrizes(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TableOfPrizesTestModule],
        declarations: [TableOfPrizesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TableOfPrizesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TableOfPrizesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tableOfPrizes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tableOfPrizes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
